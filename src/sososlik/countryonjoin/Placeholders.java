package sososlik.countryonjoin;

import sososlik.countryonjoin.Plugin;
import sososlik.countryonjoin.Plugin.CountryInfo;

import org.bukkit.entity.Player;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class Placeholders extends PlaceholderExpansion {

	public static final String PLACEHOLDERS_BASE = "countryonjoin";
	
	private Plugin plugin;
	
	public Placeholders(Plugin plugin) {
		super();
		this.plugin = plugin;
	}

	@Override
    public boolean persist() {
        // This tells PlaceholderAPI to not unregister this hook when /papi reload is executed
        return true;
    }
	
	@Override
    public String getPlugin() {
        return null;
    }
	
	@Override
	public String getAuthor() {
		return plugin.getDescription().getAuthors().get(0);
	}

	@Override
	public String getIdentifier() {
		return PLACEHOLDERS_BASE;
	}

	@Override
	public String getVersion() {
		return plugin.getDescription().getVersion();
	}
	
	@Override
    public String onPlaceholderRequest(Player p, String identifier) {
		switch (identifier) {
		case "player_country_name":
			if(p == null) return "";
			return this.plugin.getPlayerCountryInfo(p).getLocalizedName();
		case "player_country_code":
			if(p == null) return "";
			CountryInfo playerCountryInfo = this.plugin.getPlayerCountryInfo(p);
			if(playerCountryInfo.isUnknown()) return "";
			return playerCountryInfo.getCode();
		default:
			return null;
		}
	}

}
