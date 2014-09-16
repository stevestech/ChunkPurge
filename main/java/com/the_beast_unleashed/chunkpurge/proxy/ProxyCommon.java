package com.the_beast_unleashed.chunkpurge.proxy;

import java.util.logging.Logger;

import net.minecraftforge.common.Configuration;

import com.the_beast_unleashed.chunkpurge.ModChunkPurge;
import com.the_beast_unleashed.chunkpurge.commands.CommandChunkPurge;
import com.the_beast_unleashed.chunkpurge.events.HandlerWorldTick;
import com.the_beast_unleashed.chunkpurge.operators.HandlerConfig;

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
		
		ModChunkPurge.log = event.getModLog();
		ModChunkPurge.config = new HandlerConfig(event.getSuggestedConfigurationFile());
		
	}
	
	public void load(FMLInitializationEvent event)
	{

	}
	
	public void postInit(FMLPostInitializationEvent event)
	{
		
		TickRegistry.registerScheduledTickHandler(new HandlerWorldTick(), Side.SERVER);
		
	}
	
	public void serverLoad(FMLServerStartingEvent event)
	{
		
		event.registerServerCommand(new CommandChunkPurge());
		
	}
	
}
