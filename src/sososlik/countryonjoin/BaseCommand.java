package sososlik.countryonjoin;

import org.bukkit.ChatColor;
//import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public abstract class BaseCommand implements CommandExecutor 
{
	public static final String ACCESS_DENIED_TEXT = "You don't have permission to use that command!";
	public static final String TOO_MANY_ARGUMENTS_TEXT = "Too many arguments!";
	
	protected Plugin plugin;
	
    public BaseCommand(Plugin plugin) {
		this.plugin = plugin;
	}
	
	protected boolean checkAccess(CommandSender sender, String commandName) {
		if(!sender.hasPermission(Plugin.PERMISSIONS_BASE + ".command." + commandName))
		{
			this.replyError(sender, ACCESS_DENIED_TEXT);
			return false;
		}
		return true;
	}
	
	protected void replyError(CommandSender sender, String text)
	{
		sender.sendMessage(ChatColor.RED + text);
	}
	
	protected void replyInfo(CommandSender sender, String text)
	{
		sender.sendMessage(text);
	}
	
}
