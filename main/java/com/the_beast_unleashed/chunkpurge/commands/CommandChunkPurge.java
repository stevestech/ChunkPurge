package com.the_beast_unleashed.chunkpurge.commands;

import java.util.ArrayList;
import java.util.List;

import com.the_beast_unleashed.chunkpurge.ModChunkPurge;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class CommandChunkPurge implements ICommand
{
	
	private void sendCommandUsage(ICommandSender icommandsender)
	{
		ChatComponentText msg = new ChatComponentText(EnumChatFormatting.RED + this.getCommandUsage(icommandsender));
		icommandsender.addChatMessage(msg);
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

				ChatComponentText msg = new ChatComponentText(EnumChatFormatting.GREEN + "chunkUnloadDelay: " + String.valueOf(ModChunkPurge.config.chunkUnloadDelay));
				icommandsender.addChatMessage(msg);

			}
			
			else if (args[0].equalsIgnoreCase("debug"))
			{

				ChatComponentText msg = new ChatComponentText(EnumChatFormatting.GREEN + "Debug mode: " + String.valueOf(ModChunkPurge.config.debug));
				icommandsender.addChatMessage(msg);
				
			}
			
			else if (args[0].equalsIgnoreCase("enable"))
			{

				ChatComponentText msg = new ChatComponentText(EnumChatFormatting.GREEN + "Enabled: " + String.valueOf(ModChunkPurge.config.enabled));
				icommandsender.addChatMessage(msg);
				
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

						ChatComponentText msg = new ChatComponentText(EnumChatFormatting.GREEN + "ChunkPurge has been disabled.");
						icommandsender.addChatMessage(msg);
						
						ModChunkPurge.config.saveConfig();
						
					}
					
					else
					{
						
						ModChunkPurge.config.chunkUnloadDelay = Integer.valueOf(args[1]);

						ChatComponentText msg = new ChatComponentText(EnumChatFormatting.GREEN + "chunkUnloadDelay: " + args[1]);
						icommandsender.addChatMessage(msg);
						
						ModChunkPurge.config.saveConfig();
						
					}
					
				}
				
				catch (NumberFormatException ex)
				{

					ChatComponentText msg = new ChatComponentText(EnumChatFormatting.RED + "This does not look like an integer to me: " + args[1]);
					icommandsender.addChatMessage(msg);
					
				}
				
			}
			
			else if (args[0].equalsIgnoreCase("debug"))
			{
				
				if (args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("false"))
				{
					
					ModChunkPurge.config.debug = Boolean.valueOf(args[1]);

					ChatComponentText msg = new ChatComponentText(EnumChatFormatting.GREEN + "Debug mode: " + String.valueOf(ModChunkPurge.config.debug));
					icommandsender.addChatMessage(msg);
					
					ModChunkPurge.config.saveConfig();
					
				}
				
				else
				{

					ChatComponentText msg = new ChatComponentText(EnumChatFormatting.RED + "This does not look like true or false to me: " + args[1]);
					icommandsender.addChatMessage(msg);
					
				}
				
			}
			
			else if (args[0].equalsIgnoreCase("enable"))
			{
				
				if (args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("false"))
				{
					
					ModChunkPurge.config.enabled = Boolean.valueOf(args[1]);

					ChatComponentText msg = new ChatComponentText(EnumChatFormatting.GREEN + "Enabled: " + String.valueOf(ModChunkPurge.config.enabled));
					icommandsender.addChatMessage(msg);
					
					ModChunkPurge.config.saveConfig();
					
				}
				
				else
				{

					ChatComponentText msg = new ChatComponentText(EnumChatFormatting.RED + "This does not look like true or false to me: " + args[1]);
					icommandsender.addChatMessage(msg);
					
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
