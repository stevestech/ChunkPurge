package com.the_beast_unleashed.chunkpurge;

import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;

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
	private HashSet<ChunkCoordIntPair> playerWatchedChunks;
	
	public WorldChunkUnloader (WorldServer world)
	{
		
		this.world = world;
		
	}
	
	/*
	 * Populate the playerWatchedChunks set with all the chunks that are currently within any player's
	 * view distance.
	 */	
	public void populateWatchedChunks()
	{
		
		playerWatchedChunks = new HashSet<ChunkCoordIntPair>();
		ListIterator playerIterator = world.playerEntities.listIterator();
		int viewRadius = MinecraftServer.getServer().getConfigurationManager().getViewDistance();
		
		while(playerIterator.hasNext())
		{
			
			Object objectPlayer = playerIterator.next();
			
			if (objectPlayer instanceof EntityPlayerMP && !(objectPlayer instanceof FakePlayer))
			{
				
				EntityPlayerMP player = (EntityPlayerMP) objectPlayer;
				int minX = player.chunkCoordX - viewRadius;
				int maxX = player.chunkCoordX + viewRadius;
				int minZ = player.chunkCoordZ - viewRadius;
				int maxZ = player.chunkCoordZ + viewRadius;
				
				for (int x = minX; x <= maxX; ++x)
				{
					
					for (int z = minZ; z <= maxZ; ++z)
					{
						
						playerWatchedChunks.add(new ChunkCoordIntPair(x, z));
						
					}
					
				}
				
			}
			
		}	
		
	}
	
	/*
	 * Fill the chunksToUnload set with all the currently-loaded chunks that are not currently within any
	 * player's view distance.
	 * 
	 * ChunkProviderServer.loadedChunks is a private field, so an access transformer is required to access it.
	 */	
	public void populateChunksToUnload()
	{

		chunksToUnload = new HashSet<ChunkCoordIntPair>();
		
		if (this.world.getChunkProvider() instanceof ChunkProviderServer)
		{
			List<Chunk> loadedChunks = ((ChunkProviderServer) this.world.getChunkProvider()).loadedChunks;
			
			for (Chunk chunk : loadedChunks)
			{
				
				if (!playerWatchedChunks.contains(chunk.getChunkCoordIntPair()))
				{
					
					chunksToUnload.add(chunk.getChunkCoordIntPair());
					
				}
				
			}			
			
		}
		
	}
	
	/*
	 * Iterate through chunksToUnload and add these chunks to the unloading queue. Chunks that are on the queue will be unloaded
	 * by unloadQueuedChunks on the next world tick.
	 */
	public void unloadChunks()
	{
		
		ChunkPurgeMod.log.log(Level.INFO, "Queueing " + String.valueOf(chunksToUnload.size())
				+ " chunks for unload in dimension " + this.world.provider.getDimensionName()
				+ " (" + String.valueOf(this.world.provider.dimensionId) + ")");
		
		for (ChunkCoordIntPair coord : chunksToUnload)
		{
			
			if (this.world.getChunkProvider() instanceof ChunkProviderServer)
			{
				
				((ChunkProviderServer) this.world.getChunkProvider()).unloadChunksIfNotNearSpawn(coord.chunkXPos, coord.chunkZPos);
				
			}
			
		}
		
	}
	
}
