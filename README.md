ChunkPurge
==========

A Minecraft Forge mod to periodically unload chunks from the server. Attempts to recreate the functionality of Cauldron's ChunkGC.

Download
========

Not required on the client, although may work in single player.

Minecraft 1.6.4:  
http://the-beast-unleashed.com/files/ChunkPurge-1.6.4-1.2.jar

Description
===========

This mod works by periodically scanning the loaded chunks in each world, and identifying those which are isolated from any valid chunk watchers. Those chunks are then scheduled for unloading. Valid chunk watchers are players, chunkloading tickets, and the spawn areas of certain worlds. 

By only unloading isolated chunks, we avoid breaking energy nets and other multi-chunk objects. Partially unloading these would cause significant lag spikes. A flood fill algorithm is used to identify which chunks are still linked to chunk watchers, and the remaining chunks are unloaded.

Warning
=======

This mod has not been well-tested and results may be unpredictable. Please report any issues that you encounter.

Commands
========

These configuration options can also be edited in ChunkPurge.cfg. For all commands omit the parameter to view the current setting.

```
/chunkpurge chunkUnloadDelay [ticks]
```
The number of ticks to wait between chunk unloading attempts. 0 to disable.
Default: 600

```
/chunkpurge debug [true|false]
```
Logs to console the number of chunks unloaded from each dimension, and how long is spent calculating which chunks to unload.
Default: false

```
/chunkpurge enable [true|false]
```
Setting to false will prevent any attempts to unload chunks.
Default: true
