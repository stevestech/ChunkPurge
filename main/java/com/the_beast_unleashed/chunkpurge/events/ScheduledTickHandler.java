package com.the_beast_unleashed.chunkpurge.events;

import java.util.EnumSet;
import java.util.logging.Level;

import com.the_beast_unleashed.chunkpurge.ChunkPurge;

import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.ChunkProviderServer;
import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;

public class ScheduledTickHandler implements IScheduledTickHandler
{

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{
		
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData)
	{
		
		if (type.contains(TickType.WORLD) && tickData[0] instanceof WorldServer)
		{
			WorldServer world = (WorldServer) tickData[0];			
			if (world == null) return;
			
			int initialChunkCount = world.getChunkProvider().getLoadedChunkCount();
			int loadedChunksDelta = 100;
			
			((ChunkProviderServer)world.getChunkProvider()).unloadAllChunks();
			
			// Continue unloading queued chunks until less than 100 chunks are being unloaded in each operation
			while(loadedChunksDelta >= 100)
			{
				int loadedBefore = world.getChunkProvider().getLoadedChunkCount();
				world.getChunkProvider().unloadQueuedChunks();
				loadedChunksDelta = loadedBefore - world.getChunkProvider().getLoadedChunkCount();
			}
			
			int finalChunkCount = world.getChunkProvider().getLoadedChunkCount();
			int finalChunksDelta = initialChunkCount - finalChunkCount;
			
			ChunkPurge.log.log(Level.INFO, "Purged " + String.valueOf(finalChunksDelta)
					+ " chunks for: " + world.provider.getDimensionName() 
					+ " (" +  String.valueOf(world.provider.dimensionId) + ")");
			
		}		
		
	}

	@Override
	public EnumSet<TickType> ticks() 
	{
		return EnumSet.of(TickType.WORLD);
	}

	@Override
	public String getLabel()
	{
		return "ChunkPurge chunk unloader";
	}

	@Override
	public int nextTickSpacing()
	{
		// Schedule to run every 1200 world ticks
		return 1200;
	}

}
