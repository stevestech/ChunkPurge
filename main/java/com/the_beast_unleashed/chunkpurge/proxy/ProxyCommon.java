package com.the_beast_unleashed.chunkpurge.proxy;

import java.util.logging.Logger;

import net.minecraftforge.common.Configuration;

import com.the_beast_unleashed.chunkpurge.ChunkPurgeMod;
import com.the_beast_unleashed.chunkpurge.commands.ChunkPurgeCommand;
import com.the_beast_unleashed.chunkpurge.events.WorldTickHandler;
import com.the_beast_unleashed.chunkpurge.operators.ConfigHandler;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ProxyCommon
{
	
	public void preInit(FMLPreInitializationEvent event)
	{
		
		ChunkPurgeMod.log = event.getModLog();
		ChunkPurgeMod.config = new ConfigHandler(event.getSuggestedConfigurationFile());
		
	}
	
	public void load(FMLInitializationEvent event)
	{

	}
	
	public void postInit(FMLPostInitializationEvent event)
	{
		
		TickRegistry.registerScheduledTickHandler(new WorldTickHandler(), Side.SERVER);
		
	}
	
	public void serverLoad(FMLServerStartingEvent event)
	{
		
		event.registerServerCommand(new ChunkPurgeCommand());
		
	}
	
}
