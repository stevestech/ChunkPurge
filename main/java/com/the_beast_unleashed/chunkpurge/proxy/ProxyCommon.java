package com.the_beast_unleashed.chunkpurge.proxy;

import java.util.logging.Logger;

import com.the_beast_unleashed.chunkpurge.ChunkPurgeMod;
import com.the_beast_unleashed.chunkpurge.events.WorldTickHandler;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ProxyCommon
{
	
	public void preInit()
	{
		ChunkPurgeMod.log = Logger.getLogger(ChunkPurgeMod.MODID);		
		ChunkPurgeMod.log.setParent(FMLLog.getLogger());
		ChunkPurgeMod.log.setUseParentHandlers(true);        
	}
	
	public void load()
	{

	}
	
	public void postInit()
	{
		TickRegistry.registerScheduledTickHandler(new WorldTickHandler(), Side.SERVER);
	}
	
}
