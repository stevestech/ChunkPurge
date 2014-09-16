package com.the_beast_unleashed.chunkpurge.operators;

import java.io.File;

import com.the_beast_unleashed.chunkpurge.ChunkPurgeMod;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

public class ConfigHandler
{
	
	private Configuration config;
	private Properties properties;
	
	public int chunkUnloadDelay;
	public String commandChunkPurge;
	public boolean enabled;
	public boolean debug;
	
	public ConfigHandler(File configurationFile)
	{
		
		config = new Configuration(configurationFile);
		config.load();
		properties = new Properties();
		
		properties.enabled = config.get(Configuration.CATEGORY_GENERAL, "enable", true,
				"Setting to false will prevent any attempts to unload chunks."
				+ "\nChange in game with /chunkpurge enable <true|false>"
				+ "\nDefault: true");
		
		enabled = properties.enabled.getBoolean(true);
		
		properties.chunkUnloadDelay = config.get(Configuration.CATEGORY_GENERAL, "chunkUnloadDelay", 600,
				"The number of ticks to wait between chunk unloading attempts. 0 to disable."
				+ "\nChange in game with /chunkpurge delay <ticks>"
				+ "\nDefault: 600");
		
		chunkUnloadDelay = properties.chunkUnloadDelay.getInt(600);
		
		properties.debug = config.get(Configuration.CATEGORY_GENERAL, "debug", false,
				"Logs the number of chunks unloaded from each dimension, and how long is spent calculating which chunks to unload."
				+ "\nChange in game with /chunkpurge debug <true|false>"
				+ "\nDefault: false");
		
		debug = properties.debug.getBoolean(false);
		
		commandChunkPurge = config.get(Configuration.CATEGORY_GENERAL, "commandChunkPurge", "chunkpurge",
				"Which command to register for in game configuration."
				+ "\nDefault: chunkpurge").getString();
		
		config.save();
		
		if (chunkUnloadDelay == 0)
		{
			
			chunkUnloadDelay = 600;
			enabled = false;
			
		}
		
	}
	
	public void saveConfig()
	{
		
		properties.chunkUnloadDelay.set(chunkUnloadDelay);
		properties.debug.set(debug);
		properties.enabled.set(enabled);
		config.save();
		
	}
	
	
	private class Properties
	{
		
		Property chunkUnloadDelay;
		Property enabled;
		Property debug;
		
	}
	
}
