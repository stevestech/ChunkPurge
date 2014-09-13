package com.the_beast_unleashed.chunkpurge.proxy;

import java.util.logging.Logger;

import com.the_beast_unleashed.chunkpurge.ChunkPurge;
import com.the_beast_unleashed.chunkpurge.events.ScheduledTickHandler;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ProxyCommon
{
	
	public void preInit()
	{
		ChunkPurge.log = Logger.getLogger(ChunkPurge.MODID);		
		ChunkPurge.log.setParent(FMLLog.getLogger());
		ChunkPurge.log.setUseParentHandlers(true);        
	}
	
	public void load()
	{

	}
	
	public void postInit()
	{
		TickRegistry.registerScheduledTickHandler(new ScheduledTickHandler(), Side.SERVER);
	}
	
}
