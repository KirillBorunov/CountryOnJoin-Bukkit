package sososlik.countryonjoin;

import java.net.InetAddress;
import java.text.MessageFormat;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.model.CountryResponse;

public class Listener implements org.bukkit.event.Listener
{
	private static Listener instance;
	private MessageFormat withCountryformatter;
	private MessageFormat withoutCountryformatter;
		
	public static Listener getInstance()
	{
		return instance;
	}
			
	public Listener() 
	{
		instance = this;
	}

	private MessageFormat getWithCountryFormatter() 
	{
		if(this.withCountryformatter == null)
		{
			this.withCountryformatter = new MessageFormat(Plugin.getInstance().getMyConfig().getJoinWithCountryMessage());
		}
		return this.withCountryformatter;
	}
	
	private MessageFormat getWithoutCountryFormatter() 
	{
		if(this.withoutCountryformatter == null)
		{
			this.withoutCountryformatter = new MessageFormat(Plugin.getInstance().getMyConfig().getJoinWithoutCountryMessage());
		}
		return this.withoutCountryformatter;
	}
	
	public void reload()
	{
		this.withCountryformatter = null;
		this.withoutCountryformatter = null;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) 
	{
	
		InetAddress playerAddress = event.getPlayer().getAddress().getAddress();
		
		String playerCountryKey = Plugin.UNKNOWN_COUNTRY_KEY;
		String playerCountryDisplayName;
		
		
		if(Plugin.getInstance().getMyConfig().getDebug())
		{
			Plugin.getInstance().getLogger().info("Player " + event.getPlayer().getDisplayName() + " joined with address " + playerAddress.toString());
		}	
		
		
		//player with the "hide" permission do not reveal the country
		if(event.getPlayer().hasPermission(Plugin.PERMISSIONS_BASE + ".hide"))
		{
			if(Plugin.getInstance().getMyConfig().getDebug())
			{
				Plugin.getInstance().getLogger().info("Hiding the country for player " + event.getPlayer().getDisplayName() + " with address " + playerAddress.toString());
			}	
		}
		else
		{
			try
			{
				CountryResponse res = Plugin.getInstance().getGeoIPDBReader().country(playerAddress);
				
				if(res != null)
				{
					playerCountryKey = res.getCountry().getIsoCode();
				}
			}
			catch (AddressNotFoundException e)
			{
				if(Plugin.getInstance().getMyConfig().getDebug())
				{
					Plugin.getInstance().getLogger().warning("Country not found for player " + event.getPlayer().getDisplayName() + " joined with address " + playerAddress.toString());
				}	
			}
			catch (Exception e)
			{
				Plugin.getInstance().getLogger().severe("Error on country lookup for player \"" + event.getPlayer().getName() + "\" with address \"" + playerAddress.toString() + "\".");
				e.printStackTrace();
			}
		}

		
		if(playerCountryKey.equals(Plugin.UNKNOWN_COUNTRY_KEY) && !Plugin.getInstance().getMyConfig().getBroadcastOnUnknownCountry())
		{
			return;
		}
		
		
		playerCountryDisplayName = Plugin.getInstance().getMyConfig().getCountryNames().get(playerCountryKey);
		
		if(playerCountryDisplayName == null)
		{
			playerCountryDisplayName = playerCountryKey;
			Plugin.getInstance().getLogger().warning("Country name not defined for \"" + playerCountryKey + "\" key.");
		}
		
		
		String message = null;
		
		try
		{
			String resultText;
			if(playerCountryKey.equals(Plugin.UNKNOWN_COUNTRY_KEY) && Plugin.getInstance().getMyConfig().getBroadcastAltJoinMsgOnUnknownCountry())
			{
				resultText = this.getWithoutCountryFormatter().format(new Object[] {event.getPlayer().getDisplayName()});
			}
			else
			{
				resultText = this.getWithCountryFormatter().format(new Object[] {event.getPlayer().getDisplayName(), playerCountryDisplayName});
			}
			message = ChatColor.translateAlternateColorCodes('&', resultText);
		}
		catch (Exception e)
		{
			Plugin.getInstance().getLogger().severe("Error creating a join message for player \"" + event.getPlayer().getName() + "\" with address \"" + playerAddress.toString() + "\".");
			e.printStackTrace();
		}
		
		
		if (message == null || message.isEmpty())
		{
			Plugin.getInstance().getLogger().warning("The resulting join message is empty for player \"" + event.getPlayer().getName() + "\" with address \"" + playerAddress.toString() + "\".");
		}
		else
		{
			if(Plugin.getInstance().getMyConfig().getReplaceDefaultJoinMessage())
			{
				event.setJoinMessage(message);
			}
			else
			{
				Plugin.getInstance().getServer().broadcastMessage(message);
			}
		}
		
	}
	
}
