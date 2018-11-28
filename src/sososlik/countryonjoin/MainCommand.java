package sososlik.countryonjoin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class MainCommand extends BaseCommand {

	public static final String COMMAND_BASE_NAME = "countryonjoin";
	
	public MainCommand(Plugin plugin) {
		super(plugin);
	}

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
				
				if(!checkAccess(sender, "reload")) break;

				if(args.length > 1)
				{
					this.replyError(sender, TOO_MANY_ARGUMENTS_TEXT);
					return false;
				}
								
				this.replyInfo(sender, "Reloading the configuration...");
				
				plugin.reload();
				
				this.replyInfo(sender, "Configuration reloaded.");
					
				break;
			default:
				this.replyError(sender, "Unknown sub-command!");
				return false;
			}
		}
		
		return true;
    	
    }
	
}
