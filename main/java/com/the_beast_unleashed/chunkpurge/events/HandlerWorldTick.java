package com.the_beast_unleashed.chunkpurge.events;

import java.util.EnumSet;
import java.util.logging.Level;

import com.the_beast_unleashed.chunkpurge.ModChunkPurge;
import com.the_beast_unleashed.chunkpurge.operators.WorldChunkUnloader;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.ChunkProviderServer;
import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;

public class HandlerWorldTick implements IScheduledTickHandler
{

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{
		
		if (ModChunkPurge.config.enabled
				&& type.contains(TickType.WORLD)
				&& tickData[0] instanceof WorldServer)
		{
			
			WorldChunkUnloader worldChunkUnloader = new WorldChunkUnloader((WorldServer) tickData[0]);
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

	@Override
	public int nextTickSpacing()
	{		
		return ModChunkPurge.config.chunkUnloadDelay;
	}

}
