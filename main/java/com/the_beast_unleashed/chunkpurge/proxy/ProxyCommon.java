package com.the_beast_unleashed.chunkpurge.proxy;


import com.the_beast_unleashed.chunkpurge.ModChunkPurge;
import com.the_beast_unleashed.chunkpurge.commands.CommandChunkPurge;
import com.the_beast_unleashed.chunkpurge.events.HandlerWorldTick;
import com.the_beast_unleashed.chunkpurge.operators.HandlerConfig;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class ProxyCommon
{
	
	public void preInit(FMLPreInitializationEvent event)
	{
		
		ModChunkPurge.log = event.getModLog();
		ModChunkPurge.config = new HandlerConfig(event.getSuggestedConfigurationFile());
		
	}
	
	public void load(FMLInitializationEvent event)
	{

	}
	
	public void postInit(FMLPostInitializationEvent event)
	{
		FMLCommonHandler.instance().bus().register(new HandlerWorldTick());
		
	}
	
	public void serverLoad(FMLServerStartingEvent event)
	{
		
		event.registerServerCommand(new CommandChunkPurge());
		
	}
	
}
