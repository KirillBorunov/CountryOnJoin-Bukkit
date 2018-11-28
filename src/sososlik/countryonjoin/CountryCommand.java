package sososlik.countryonjoin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import sososlik.countryonjoin.Plugin.CountryInfo;

public class CountryCommand extends BaseCommand {

	public static final String COMMAND_BASE_NAME = "country";
	
	public CountryCommand(Plugin plugin) {
		super(plugin);
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
    {
		if(!checkAccess(sender, COMMAND_BASE_NAME)) return true;
		
		if(args.length == 0)
		{
			this.replyError(sender, "You must specify a player!");
			return false;
		}
		if(args.length > 1)
		{
			this.replyError(sender, TOO_MANY_ARGUMENTS_TEXT);
			return false;
		}
		
		Player player = this.plugin.getServer().getPlayer(args[0]);
		
		if(player == null) {
			this.replyError(sender, "Cannot find player!");
			return true;
		}
		
		CountryInfo playerCountryInfo = this.plugin.getPlayerCountryInfo(player);
		
		if(playerCountryInfo == null || playerCountryInfo.isUnknown())
		{
			this.replyError(sender, "Player country is unknown.");
		} else {
			this.replyInfo(sender, "Player country: (" + playerCountryInfo.getCode() + ") " + playerCountryInfo.getLocalizedName());
		}
		
		return true;
    }

}
