package com.the_beast_unleashed.chunkpurge.operators;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;

import com.the_beast_unleashed.chunkpurge.ChunkPurgeMod;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.common.FakePlayer;

/*
 * A class to handle the unloading of excess chunks from a WorldServer.
 * Excess loaded chunks are those that are currently not within a player's view distance,
 * forced by a chunk loader, or loaded by the world's spawn area.
 * 
 * Calling unloadChunksIfNotNearSpawn will not queue any spawn-loaded chunks for unloading, and
 * unloadQueuedChunks will not unload any chunks that are force-loaded. Therefore we
 * only need to find the set of loaded chunks that are not player-loaded, and pass those
 * to unloadChunksIfNotNearSpawn. 
 */
public class WorldChunkUnloader
{
	
	private WorldServer world;
	private HashSet<ChunkCoordIntPair> chunksToUnload;
	private long initialTime;
	
	public WorldChunkUnloader (WorldServer world)
	{
		
		this.world = world;
		
	}
	
	
	/*
	 * A flood fill algorithm to find the shape of the loaded chunks surrounding a player-occupied chunk, or seed. 
	 * 
	 * 1. Set Q to the empty queue.
	 * 2. If the color of node is not equal to target-color, return.
	 * 3. Add node to Q.
	 * 4. For each element N of Q:
	 * 5.     If the color of N is equal to target-color:
	 * 6.         Set w and e equal to N.
	 * 7.         Move w to the west until the color of the node to the west of w no longer matches target-color.
	 * 8.         Move e to the east until the color of the node to the east of e no longer matches target-color.
	 * 9.         For each node n between w and e:
	 * 10.             Set the color of n to replacement-color.
	 * 11.             If the color of the node to the north of n is target-color, add that node to Q.
	 * 12.             If the color of the node to the south of n is target-color, add that node to Q.
	 * 13. Continue looping until Q is exhausted.
	 * 14. Return
	 */
	private HashSet<ChunkCoordIntPair> groupedChunksFinder(HashSet<ChunkCoordIntPair> loadedChunks, ChunkCoordIntPair seed)
	{
		
		LinkedList<ChunkCoordIntPair> queue = new LinkedList<ChunkCoordIntPair>();
		HashSet<ChunkCoordIntPair> groupedChunks = new HashSet<ChunkCoordIntPair>();
		
		if (!loadedChunks.contains(seed)) return groupedChunks;
		queue.add(seed);
		
		while (!queue.isEmpty())
		{
			
			ChunkCoordIntPair chunk = queue.remove();
			
			if (!groupedChunks.contains(chunk))
			{
				int west, east;
				
				for (west = chunk.chunkXPos;
						loadedChunks.contains(new ChunkCoordIntPair(west-1, chunk.chunkZPos));
						--west);
				
				for (east = chunk.chunkXPos;
						loadedChunks.contains(new ChunkCoordIntPair(east+1, chunk.chunkZPos));
						++east);
				
				for (int x = west; x <= east; ++x)
				{
					
					groupedChunks.add(new ChunkCoordIntPair(x, chunk.chunkZPos));
					
					if (loadedChunks.contains(new ChunkCoordIntPair(x, chunk.chunkZPos+1)))
					{
						queue.add(new ChunkCoordIntPair (x, chunk.chunkZPos+1));
					}
					
					if (loadedChunks.contains(new ChunkCoordIntPair(x, chunk.chunkZPos-1)))
					{
						queue.add(new ChunkCoordIntPair (x, chunk.chunkZPos-1));
					}
					
				}
				
			}
			
		}
		
		return groupedChunks;
	}
	
	/*
	 * Populate chunksToUnload with chunks that are isolated from all players.
	 * 
	 * Use a flood-fill algorithm to find the set of all loaded chunks in the world which link back
	 * to a player-occupied chunk through other loaded chunks. The idea is to find the isolated chunks
	 * which do NOT link back to a player, and unload those. 
	 * 
	 * This is a better alternative to simply unloading all chunks outside of a player's view radius.
	 * Unloading chunks while not unloading their neighbours would result in tps-spikes due to the breaking
	 * of energy nets and the like. This approach should reduce the severity of those tps-spikes.
	 * 
	 * ChunkProviderServer.loadedChunks is a private field, so an access transformer is required to access it. 
	 */
	private void populateChunksToUnload()
	{
		
		chunksToUnload = new HashSet<ChunkCoordIntPair>();
		
		if (world.getChunkProvider() instanceof ChunkProviderServer)
		{
			HashSet<ChunkCoordIntPair> loadedChunks = new HashSet<ChunkCoordIntPair>();
			HashSet<ChunkCoordIntPair> playerLoadedChunks = new HashSet<ChunkCoordIntPair>();
			List<EntityPlayerMP> listPlayers = world.playerEntities;
			
			// Want to deal with chunk coordinates, not chunk objects.
			for (Chunk chunk : (List<Chunk>) ((ChunkProviderServer) world.getChunkProvider()).loadedChunks)
			{
				
				loadedChunks.add(chunk.getChunkCoordIntPair());
				
			}
			
			for (EntityPlayerMP player : listPlayers)
			{
				
				if (!(player instanceof FakePlayer))
				{
					
					ChunkCoordIntPair playerChunkCoords = new ChunkCoordIntPair(player.chunkCoordX, player.chunkCoordZ);
					playerLoadedChunks.addAll(groupedChunksFinder(loadedChunks, playerChunkCoords));
					
				}
				
			}
			
			for (ChunkCoordIntPair coord : loadedChunks)
			{
				
				if (!playerLoadedChunks.contains(coord))
				{
					
					chunksToUnload.add(coord);
					
				}
				
			}
			
		}
		
	}
	
	/*
	 * Analyse the chunks that are currently loaded in this world. Select loaded chunks that are isolated from player-loaded
	 * chunks, and queue these isolated chunks for unloading.
	 */
	public void unloadChunks()
	{
		initialTime = MinecraftServer.getSystemTimeMillis();
		
		populateChunksToUnload();
		
		if (this.world.getChunkProvider() instanceof ChunkProviderServer)
		{
			
			for (ChunkCoordIntPair coord : chunksToUnload)
			{
				
				((ChunkProviderServer) this.world.getChunkProvider()).unloadChunksIfNotNearSpawn(coord.chunkXPos, coord.chunkZPos);
				
			}
			
		}
		
		if (ChunkPurgeMod.config.debug)
		{
			
			ChunkPurgeMod.log.log(Level.INFO, "Queued " + String.valueOf(chunksToUnload.size())
					+ " chunks for unload in dimension " + this.world.provider.getDimensionName()
					+ " (" + String.valueOf(this.world.provider.dimensionId)
					+ ") in " + String.valueOf(MinecraftServer.getSystemTimeMillis() - this.initialTime)
					+ " milliseconds.");
			
		}
		
	}
	
}
