package com.the_beast_unleashed.chunkpurge.commands;

import java.util.ArrayList;
import java.util.List;

import com.the_beast_unleashed.chunkpurge.ModChunkPurge;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;

public class CommandChunkPurge implements ICommand
{
	
	private void sendCommandUsage(ICommandSender icommandsender)
	{
		
		ChatMessageComponent msg = ChatMessageComponent.createFromText(this.getCommandUsage(icommandsender));
		msg.setColor(EnumChatFormatting.RED);
		icommandsender.sendChatToPlayer(msg);
		
	}

	public int compareTo(ICommand par1ICommand)
	{
		return this.getCommandName().compareTo(par1ICommand.getCommandName());
	}

	public int compareTo(Object par1Obj)
	{
		return this.compareTo((ICommand)par1Obj);
	}

	@Override
	public String getCommandName()
	{
		return ModChunkPurge.config.commandChunkPurge;
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender)
	{
		return "/" + this.getCommandName() + " chunkUnloadDelay [ticks]"
				+ "\n/" + this.getCommandName() + " debug [true|false]"
				+ "\n/" + this.getCommandName() + " enable [true|false]";
	}

	@Override
	public List getCommandAliases()
	{
		return null;
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] args)
	{
		
		if (args.length == 1)
		{
			
			if (args[0].equalsIgnoreCase("chunkunloaddelay"))
			{
				
				ChatMessageComponent msg = ChatMessageComponent.createFromText("chunkUnloadDelay: " + String.valueOf(ModChunkPurge.config.chunkUnloadDelay));
				msg.setColor(EnumChatFormatting.GREEN);
				icommandsender.sendChatToPlayer(msg);
				
			}
			
			else if (args[0].equalsIgnoreCase("debug"))
			{
				
				ChatMessageComponent msg = ChatMessageComponent.createFromText("Debug mode: " + String.valueOf(ModChunkPurge.config.debug));
				msg.setColor(EnumChatFormatting.GREEN);
				icommandsender.sendChatToPlayer(msg);
				
			}
			
			else if (args[0].equalsIgnoreCase("enable"))
			{
				
				ChatMessageComponent msg = ChatMessageComponent.createFromText("Enabled: " + String.valueOf(ModChunkPurge.config.enabled));
				msg.setColor(EnumChatFormatting.GREEN);
				icommandsender.sendChatToPlayer(msg);
				
			}
			
			else
			{
				
				sendCommandUsage(icommandsender);
				
			}
			
		}
		
		else if (args.length == 2)
		{
			
			if (args[0].equalsIgnoreCase("chunkunloaddelay"))
			{
				try
				{
					
					if (Integer.valueOf(args[1]) == 0)
					{
						
						ModChunkPurge.config.chunkUnloadDelay = 600;
						ModChunkPurge.config.enabled = false;
						
						ChatMessageComponent msg = ChatMessageComponent.createFromText("ChunkPurge has been disabled.");
						msg.setColor(EnumChatFormatting.GREEN);
						icommandsender.sendChatToPlayer(msg);
						
						ModChunkPurge.config.saveConfig();
						
					}
					
					else
					{
						
						ModChunkPurge.config.chunkUnloadDelay = Integer.valueOf(args[1]);
						
						ChatMessageComponent msg = ChatMessageComponent.createFromText("chunkUnloadDelay: " + args[1]);
						msg.setColor(EnumChatFormatting.GREEN);
						icommandsender.sendChatToPlayer(msg);
						
						ModChunkPurge.config.saveConfig();
						
					}
					
				}
				
				catch (NumberFormatException ex)
				{
					
					ChatMessageComponent msg = ChatMessageComponent.createFromText("This does not look like an integer to me: " + args[1]);
					msg.setColor(EnumChatFormatting.RED);
					icommandsender.sendChatToPlayer(msg);
					
				}
				
			}
			
			else if (args[0].equalsIgnoreCase("debug"))
			{
				
				if (args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("false"))
				{
					
					ModChunkPurge.config.debug = Boolean.valueOf(args[1]);
					
					ChatMessageComponent msg = ChatMessageComponent.createFromText("Debug mode: " + String.valueOf(ModChunkPurge.config.debug));
					msg.setColor(EnumChatFormatting.GREEN);
					icommandsender.sendChatToPlayer(msg);
					
					ModChunkPurge.config.saveConfig();
					
				}
				
				else
				{
					
					ChatMessageComponent msg = ChatMessageComponent.createFromText("This does not look like true or false to me: " + args[1]);
					msg.setColor(EnumChatFormatting.RED);
					icommandsender.sendChatToPlayer(msg);
					
				}
				
			}
			
			else if (args[0].equalsIgnoreCase("enable"))
			{
				
				if (args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("false"))
				{
					
					ModChunkPurge.config.enabled = Boolean.valueOf(args[1]);
					
					ChatMessageComponent msg = ChatMessageComponent.createFromText("Enabled: " + String.valueOf(ModChunkPurge.config.enabled));
					msg.setColor(EnumChatFormatting.GREEN);
					icommandsender.sendChatToPlayer(msg);
					
					ModChunkPurge.config.saveConfig();
					
				}
				
				else
				{
					
					ChatMessageComponent msg = ChatMessageComponent.createFromText("This does not look like true or false to me: " + args[1]);
					msg.setColor(EnumChatFormatting.RED);
					icommandsender.sendChatToPlayer(msg);
					
				}
				
			}
			
			else
			{
				
				sendCommandUsage(icommandsender);
				
			}
			
		}
		
		else
		{
			
			sendCommandUsage(icommandsender);
			
		}
		
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender)
	{
		return sender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
	}

	public int getRequiredPermissionLevel()
	{
		return 2;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender icommandsender, String[] astring)
	{
		List tabCompletionOptions = new ArrayList<String>();
		tabCompletionOptions.add("chunkUnloadDelay");
		tabCompletionOptions.add("debug");
		tabCompletionOptions.add("enable");
		return tabCompletionOptions;
	}

	@Override
	public boolean isUsernameIndex(String[] astring, int i)
	{
		return false;
	}
	
}
