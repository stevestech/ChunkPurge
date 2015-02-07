package com.the_beast_unleashed.chunkpurge.events;

import com.the_beast_unleashed.chunkpurge.ModChunkPurge;
import com.the_beast_unleashed.chunkpurge.operators.WorldChunkUnloader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class HandlerWorldTick
{

	private int tickTimer = 0;

	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event) {
		if(tickTimer++ < ModChunkPurge.config.chunkUnloadDelay || !ModChunkPurge.config.enabled) {
			return;
		}

		WorldChunkUnloader worldChunkUnloader = new WorldChunkUnloader(event.world);
		worldChunkUnloader.unloadChunks();

		tickTimer = 0;
	}

}
