package sososlik.countryonjoin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BaseCommand implements CommandExecutor 
{
	
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
    {
		if(args.length == 0)
		{
			this.replyError(sender, "You must specify a sub-command!");
			return false;
		}
		else
		{
			switch(args[0].toLowerCase())
			{
			case "reload":
				
				if(sender.hasPermission(Plugin.PERMISSIONS_BASE + ".command.reload"))
				{
					if(args.length > 1)
					{
						this.replyError(sender, "Too many arguments!");
						return false;
					}
									
					this.replyInfo(sender, "Reloading the configuration...");
					
					Plugin.getInstance().reload();
					Listener.getInstance().reload();
					
					this.replyInfo(sender, "Configuration reloaded.");
					
				}
				else
				{
					this.replyError(sender, "You don't have permission to use that command!");
				}
				
				break;
			default:
				this.replyError(sender, "Unknown sub-command!");
				return false;
			}
		}
		
		return true;
    	
    }
	
	private void replyError(CommandSender sender, String text)
	{
		sender.sendMessage(ChatColor.RED + text);
	}
	
	private void replyInfo(CommandSender sender, String text)
	{
		sender.sendMessage(text);
	}
	
}
