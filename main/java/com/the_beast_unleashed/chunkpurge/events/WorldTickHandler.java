package com.the_beast_unleashed.chunkpurge.events;

import java.util.EnumSet;
import java.util.logging.Level;

import com.the_beast_unleashed.chunkpurge.ChunkPurgeMod;
import com.the_beast_unleashed.chunkpurge.WorldChunkUnloader;

import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.ChunkProviderServer;
import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;

public class WorldTickHandler implements IScheduledTickHandler
{

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{
		
		if (type.contains(TickType.WORLD) && tickData[0] instanceof WorldServer)
		{
			
			WorldServer world = (WorldServer) tickData[0];			
			if (world == null) return;
			
			WorldChunkUnloader worldChunkUnloader = new WorldChunkUnloader(world);
			worldChunkUnloader.populateWatchedChunks();
			worldChunkUnloader.populateChunksToUnload();
			worldChunkUnloader.unloadChunks();
			
		}		
		
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData)
	{
		
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

	/* 
	 * Schedule to run every 6000 world ticks. Unloading chunks will cause a tps-spike so
	 * don't want this to be too frequent.
	 */
	@Override
	public int nextTickSpacing()
	{		
		return 6000;
	}

}
