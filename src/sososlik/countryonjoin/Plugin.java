package sososlik.countryonjoin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import static java.nio.file.StandardCopyOption.*;

import sososlik.countryonjoin.GeoIP.NotFoundException;


public class Plugin extends JavaPlugin implements org.bukkit.event.Listener
{
	
	public static final String GEOIP_DB_FILENAME = "GeoLite2-Country.mmdb"; //NOTE: ensure this name is synced with the default name in the Config.java
	public static final String README_FILENAME = "README.txt";
	public static final String CONFIG_FILENAME = "config.yml";
	public static final String MESSAGES_FILENAME_PATTERN = "messages\\..\\.yml";
	public static final String MESSAGES_BASEDIR = "messages";
	public static final String MESSAGES_FILENAME_FORMAT = "messages.%s.yml";
	public static final String COUNTRYNAMES_FILENAME_PATTERN = "countrynames\\..\\.yml";
	public static final String COUNTRYNAMES_BASEDIR = "countrynames";
	public static final String COUNTRYNAMES_FILENAME_FORMAT = "countrynames.%s.yml";
	public static final String PERMISSIONS_BASE = "countryonjoin";
	public static final String UNKNOWN_COUNTRY_KEY = "unknown";

	private Config config;
	private GeoIP geoIP;
	private MessageFormat withCountryformatter;
	private MessageFormat withoutCountryformatter;
	
	@Override
	public void onEnable() 
	{
		
		File dataDir = this.getDataFolder();
		
		if (!dataDir.exists())
		{
			try
			{
				dataDir.mkdir();
			}
			catch(Exception e)
			{
				this.handleSevereError(e, "Error creating the plugin data directory \"" + dataDir.getAbsolutePath() + "\".");
				return;
			}
		}
		
		
		File configFile = new File(dataDir, CONFIG_FILENAME);

		if(!configFile.exists())
		{
			try(InputStream rs = this.getResourceAsStream(CONFIG_FILENAME))
			{
				Files.copy(rs, configFile.toPath());
			} catch (Exception e)
			{
				this.handleSevereError(e, "Error extracting the file \"" + CONFIG_FILENAME +"\" to \"" + configFile.getAbsolutePath() + "\".");
				return;
			}
		}
		

		File messagesBaseDir = new File(dataDir, MESSAGES_BASEDIR);
		
		if(!messagesBaseDir.exists())
		{
			try
			{
				messagesBaseDir.mkdir();
			}
			catch (Exception e)
			{
				this.handleSevereError(e, "Error creating \"" + messagesBaseDir.getAbsolutePath() + "\" directory.");
				return;
			}
		}
		
		try(FileSystem fs = FileSystems.newFileSystem(Plugin.class.getResource("/" + MESSAGES_BASEDIR).toURI(), Collections.<String, Object>emptyMap()))
		{
	        Iterator<Path> it = Files.walk(fs.getPath("/" + MESSAGES_BASEDIR), 1).iterator();

	        while(it.hasNext())
	        {
	        	Path p = it.next();
	        	
	        	if(p.toString().equals("/" + MESSAGES_BASEDIR))
	        	{
	        		continue; //The first entry appears to be always the directory which we are enumerating...
	        	}
	        	
	        	File f = new File(messagesBaseDir, p.getFileName().toString());
	        	
	        	//NOTE: From version 1.3.0 we replace default files to make them updateable, custom files should have a custom name.
	        	//TODO: check modification date and size to avoid unnecessary write.
	        	if(f.exists())
	        	{
	        		f.setWritable(true);
	        	}
        		try (InputStream rs = this.getResourceAsStream(MESSAGES_BASEDIR + "/" + f.getName()))
        		{
        			Files.copy(rs, f.toPath(), REPLACE_EXISTING);
        			f.setReadOnly(); //advice against modifications
        		}
        		catch (Exception e)
        		{
        			this.getLogger().severe("Error extracting the file \"" + MESSAGES_BASEDIR + "/" + f.getName() + "\" to \"" + f.getAbsolutePath() + "\".");
    				throw e;
        		}
	        }
		}
		catch (Exception e)
		{
			this.handleSevereError(e, "Error extracting the directory \"" + MESSAGES_BASEDIR + "\" to \"" + messagesBaseDir.getAbsolutePath() + "\".");
			return;
		}
		
		
		File countrynamesBaseDir = new File(dataDir, COUNTRYNAMES_BASEDIR);
		
		if(!countrynamesBaseDir.exists())
		{
			try
			{
				countrynamesBaseDir.mkdir();
			}
			catch (Exception e)
			{
				this.handleSevereError(e, "Error creating \"" + countrynamesBaseDir.getAbsolutePath() + "\" directory.");
				return;
			}
		}
		
		try(FileSystem fs = FileSystems.newFileSystem(Plugin.class.getResource("/" + COUNTRYNAMES_BASEDIR).toURI(), Collections.<String, Object>emptyMap()))
		{
	        Iterator<Path> it = Files.walk(fs.getPath("/" + COUNTRYNAMES_BASEDIR), 1).iterator();

	        while(it.hasNext())
	        {
	        	Path p = it.next();
	        	
	        	if(p.toString().equals("/" + COUNTRYNAMES_BASEDIR))
	        	{
	        		continue; //The first entry appears to be always the directory which we are enumerating...
	        	}
	        	
	        	File f = new File(countrynamesBaseDir, p.getFileName().toString());
	        	
	        	//NOTE: From version 1.3.0 we replace default files to make them updateable, custom files should have a custom name.
	        	//TODO: check modification date and size to avoid unnecessary write.
	        	if(f.exists())
	        	{
	        		f.setWritable(true);
	        	}
        		try (InputStream rs = this.getResourceAsStream(COUNTRYNAMES_BASEDIR + "/" + f.getName()))
        		{
        			Files.copy(rs, f.toPath(), REPLACE_EXISTING);
        			f.setReadOnly(); //advice against modifications
        		}
        		catch (Exception e)
        		{
        			this.getLogger().severe("Error extracting the file \"" + COUNTRYNAMES_BASEDIR + "/" + f.getName() + "\" to \"" + f.getAbsolutePath() + "\".");
    				throw e;
        		}
	        }
		}
		catch (Exception e)
		{
			this.handleSevereError(e, "Error extracting the directory \"" + COUNTRYNAMES_BASEDIR + "\" to \"" + countrynamesBaseDir.getAbsolutePath() + "\".");
			return;
		}
		
		
		File geoipdbFile = new File(dataDir, GEOIP_DB_FILENAME);
		
		//NOTE: From version 1.3.0 we replace default files to make them updateable, custom files should have a custom name.
		//TODO: check modification date and size to avoid unnecessary write.
    	if(geoipdbFile.exists())
    	{
    		geoipdbFile.setWritable(true);
    	}
		try(InputStream rs = this.getResourceAsStream(GEOIP_DB_FILENAME))
		{
			Files.copy(rs, geoipdbFile.toPath(), REPLACE_EXISTING);
			geoipdbFile.setReadOnly(); //advice against modifications
		} 
		catch (Exception e) 
		{
			this.handleSevereError(e, "Error extracting the file \"" + GEOIP_DB_FILENAME + "\" to \"" + geoipdbFile.getAbsolutePath() + "\".");
			return;
		}
		
		
		File readmeFile = new File(dataDir, README_FILENAME);
		
		//NOTE: From version 1.3.0 we replace default files to make them updateable, custom files should have a custom name.
		//TODO: check modification date and size to avoid unnecessary write.
    	if(readmeFile.exists())
    	{
    		readmeFile.setWritable(true);
    	}
		new java.util.Date (readmeFile.lastModified() * 1000);
		try(InputStream rs = this.getResourceAsStream(README_FILENAME))
		{
			Files.copy(rs, readmeFile.toPath(), REPLACE_EXISTING);
			readmeFile.setReadOnly(); //advice against modifications
		} 
		catch (Exception e) 
		{
			this.handleSevereError(e, "Error extracting the file \"" + README_FILENAME + "\" to \"" + readmeFile.getAbsolutePath() + "\".");
			return; 
			//the README file is critical because legal reasons (maxmind's license requires to mention their website in the product)
		}
		
		
		this.reload(); //the first time it means 'load' and if fail should not disable the plugin
		
		
		if(this.config.getEnablePlaceholderAPIHook() && Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
		{
			if(this.config.getDebug())
			{
				this.getLogger().info("Hooking into \"PlaceholderAPI\".");
			}	
			new Placeholders(this).register();
		}
		
		
		this.getServer().getPluginManager().registerEvents(this, this);
		this.getCommand(MainCommand.COMMAND_BASE_NAME).setExecutor(new MainCommand(this));
		this.getCommand(CountryCommand.COMMAND_BASE_NAME).setExecutor(new CountryCommand(this));
	}
	
	public void handleSevereError(Exception e, String messageToUser) {
		this.getLogger().severe(messageToUser);
		e.printStackTrace();
		this.setEnabled(false);
	}
	
	public void reload()
	{
		//means 'load' on first time call 
		//NOTE: don't disable the plugin here, user can edit the config/message files and use 'reload' command for resolve the problem

		boolean firstTime = this.config == null;
		
		
		File dataDir = this.getDataFolder();
		
		
		File configFile = new File(dataDir, CONFIG_FILENAME);

		if(firstTime)
		{
			this.config = new sososlik.countryonjoin.Config();
		}
		
		String defaultMessageCulture = this.config.getMessagesCulture();
		String defaultCountrynamesCulture = this.config.getCountrynamesCulture();
		String defaultGeoIPDB = this.config.getGeoIPDB();
		
		try
		{
			YamlConfiguration config = new YamlConfiguration();
			
			config.load(configFile);

			this.config.setReplaceDefaultJoinMessage(config.getBoolean("replaceDefaultJoinMessage", this.config.getReplaceDefaultJoinMessage()));
			this.config.setBroadcastOnUnknownCountry(config.getBoolean("broadcastOnUnknownCountry", this.config.getBroadcastOnUnknownCountry()));
			this.config.setBroadcastAltJoinMsgOnUnknownCountry(config.getBoolean("broadcastAltJoinMsgOnUnknownCountry", this.config.getBroadcastAltJoinMsgOnUnknownCountry()));
			this.config.setMessagesCulture(config.getString("messages-culture", defaultMessageCulture));
			this.config.setCountrynamesCulture(config.getString("countrynames-culture", defaultCountrynamesCulture));
			this.config.setGeoIPDB(config.getString("geoIPDB", defaultGeoIPDB));
			this.config.setDebug(config.getBoolean("debug", this.config.getDebug()));
			
			boolean newValueForEnablePlaceholderAPIHook = config.getBoolean("enablePlaceholderAPIHook", this.config.getEnablePlaceholderAPIHook());
			if(firstTime) {
				this.config.setEnablePlaceholderAPIHook(newValueForEnablePlaceholderAPIHook);
			} else {
				if(this.config.getEnablePlaceholderAPIHook() != newValueForEnablePlaceholderAPIHook) {
					this.getLogger().warning("The change to the setting \"EnablePlaceholderAPIHook\" requires a server restart.");
				}
			}
			
		} catch (Exception e)
		{
			this.getLogger().severe("Error loading the \"" + configFile.getName() + "\" file.");
			e.printStackTrace();
		}

		File messagesBaseDir = new File(dataDir, MESSAGES_BASEDIR);
		
		File messagesFile = new File(messagesBaseDir, String.format(MESSAGES_FILENAME_FORMAT, this.config.getMessagesCulture()));

		if(!messagesFile.exists()) {
			this.getLogger().severe("Error loading the messages file \"" + messagesFile.getName() + "\", the default one will be used.");
			messagesFile = new File(messagesBaseDir, String.format(MESSAGES_FILENAME_FORMAT, defaultMessageCulture));
		}
		
		try (InputStreamReader sr = new InputStreamReader(new FileInputStream(messagesFile), "UTF8"))
		{
			YamlConfiguration messages = new YamlConfiguration();
			messages.load(sr);
			
			this.config.setJoinWithCountryMessage(messages.getString("joinWithCountry"));
			this.config.setJoinWithoutCountryMessage(messages.getString("joinWithoutCountry"));
		} catch (Exception e)
		{
			this.getLogger().severe("Error loading the \"" + messagesFile.getName() + "\" file.");
			e.printStackTrace();
		}
		
		
		File countrynamesBaseDir = new File(dataDir, COUNTRYNAMES_BASEDIR);
		
		File countrynamesFile = new File(countrynamesBaseDir, String.format(COUNTRYNAMES_FILENAME_FORMAT, this.config.getCountrynamesCulture()));

		if(!countrynamesFile.exists()) {
			this.getLogger().severe("Error loading the country names file \"" + countrynamesFile.getName() + "\", the default one will be used.");
			countrynamesFile = new File(countrynamesBaseDir, String.format(COUNTRYNAMES_FILENAME_FORMAT, defaultCountrynamesCulture));
		}
		
		try(InputStreamReader sr = new InputStreamReader(new FileInputStream(countrynamesFile), "UTF8")) 
		{
			YamlConfiguration countrynames = new YamlConfiguration();
			countrynames.load(sr);

			this.config.getCountryNames().clear(); //need that because a reloaded file maybe not contains all the keys that previous
			
			for(String key : countrynames.getKeys(false))
			{
				this.config.getCountryNames().put(key, countrynames.getString(key));
			}
			
		} catch (Exception e) 
		{
			this.getLogger().severe("Error loading the \"" + countrynamesFile.getName() + "\" file.");
			e.printStackTrace();
		}
		
		
		File geoipdbFile = new File(dataDir, this.config.getGeoIPDB());
		
		if(!geoipdbFile.exists()) {
			this.getLogger().severe("Error loading the geoIP DB file \"" + geoipdbFile.getName() + "\", the default one will be used.");
			geoipdbFile = new File(dataDir, defaultGeoIPDB);
		}
		
		if(this.geoIP != null)
		{
			try 
			{
				this.geoIP.close();
			}
			catch (Exception e) 
			{
				this.getLogger().severe("Error closing the \"" + geoipdbFile.getName() + "\" file.");
				e.printStackTrace();
			}
		}

		try
		{
			this.geoIP = new GeoIP(geoipdbFile);
		}
		catch (IOException e)
		{			
			this.getLogger().severe("Error loading the \"" + geoipdbFile.getName() + "\" file.");
			e.printStackTrace();
		}
		
		
		this.withCountryformatter = null;
		this.withoutCountryformatter = null;
		
	}

	private MessageFormat getWithCountryFormatter() 
	{
		if(this.withCountryformatter == null) this.withCountryformatter = new MessageFormat(this.config.getJoinWithCountryMessage());
		return this.withCountryformatter;
	}
	
	private MessageFormat getWithoutCountryFormatter() 
	{
		if(this.withoutCountryformatter == null) this.withoutCountryformatter = new MessageFormat(this.config.getJoinWithoutCountryMessage());
		return this.withoutCountryformatter;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) 
	{
	
		if(this.config.getDebug())
		{
			this.getLogger().info("Player \"" + event.getPlayer().getDisplayName() + "\" joined with address \"" + event.getPlayer().getAddress().getAddress().toString() + "\".");
		}	
		
		
		CountryInfo playerCountryInfo = this.getPlayerCountryInfo(event.getPlayer());

		
		if(playerCountryInfo.isUnknown() && !this.config.getBroadcastOnUnknownCountry())
		{
			if(this.config.getDebug())
			{
				this.getLogger().info("No broadcast message for player \"" + event.getPlayer().getDisplayName() + "\" with address \"" + event.getPlayer().getAddress().getAddress().toString() + "\" due to configuration for unknown countries.");
			}	
			return;
		}
		
		String message = null;
		
		try
		{
			String resultText;
			if(playerCountryInfo.isUnknown() && this.config.getBroadcastAltJoinMsgOnUnknownCountry())
			{
				resultText = this.getWithoutCountryFormatter().format(new Object[] {event.getPlayer().getDisplayName()});
			}
			else
			{
				resultText = this.getWithCountryFormatter().format(new Object[] {event.getPlayer().getDisplayName(), playerCountryInfo.getLocalizedName()});
			}
			message = ChatColor.translateAlternateColorCodes('&', resultText);
		}
		catch (Exception e)
		{
			this.getLogger().severe("Unknown error creating a join message for player \"" + event.getPlayer().getName() + "\" with address \"" + event.getPlayer().getAddress().getAddress().toString() + "\".");
			e.printStackTrace();
		}
		
		
		if (message == null || message.isEmpty())
		{
			this.getLogger().severe("The created join message for player \"" + event.getPlayer().getName() + "\" with address \"" + event.getPlayer().getAddress().getAddress().toString() + "\" is unexpectedly empty.");
		}
		else
		{
			if(this.config.getReplaceDefaultJoinMessage())
			{
				event.setJoinMessage(message);
			}
			else
			{
				this.getServer().broadcastMessage(message);
			}
		}
		
	}
	
	public class CountryInfo {
		private String code;
		private String localizedName;
		
		public CountryInfo(String code, String localizedName) {
			super();
			this.code = code;
			this.localizedName = localizedName;
		}

		public String getCode() {
			return this.code;
		}

		public String getLocalizedName() {
			return this.localizedName;
		}
		
		public boolean isUnknown() {
			return this.code == UNKNOWN_COUNTRY_KEY;
		}
		
	}
	
	public CountryInfo getPlayerCountryInfo(Player player) {
		
		InetAddress playerAddress = player.getAddress().getAddress();
		
		String playerCountryKey = UNKNOWN_COUNTRY_KEY;
		String playerCountryDisplayName = "";
		
		//player with the "hide" permission do not reveal the country
		if(player.hasPermission(PERMISSIONS_BASE + ".hide"))
		{
			if(this.config.getDebug())
			{
				this.getLogger().info("Hiding the country for player \"" + player.getDisplayName() + "\" with address \"" + playerAddress.toString() + "\" due to permissions.");
			}	
		}
		else
		{
			try
			{
				playerCountryKey = this.geoIP.findCountryCode(playerAddress);
			}
			catch (NotFoundException e)
			{
				if(this.config.getDebug())
				{
					this.getLogger().warning("Country not found for player \"" + player.getDisplayName() + "\" with address \"" + playerAddress.toString() + "\".");
				}	
			}
			catch (Exception e)
			{
				this.getLogger().severe("Unknown error on country lookup for player \"" + player.getName() + "\" with address \"" + playerAddress.toString() + "\".");
				e.printStackTrace();
			}
		}
		
		playerCountryDisplayName = this.getLocalizedCountryName(playerCountryKey);
		
		return new CountryInfo(playerCountryKey, playerCountryDisplayName);
		
	}
	
	public String getLocalizedCountryName(String code) {
		
		String result = this.config.getCountryNames().get(code);
		
		if(result == null || result == "")
		{
			result = code;
			this.getLogger().warning("Country name not defined for \"" + code + "\" key.");
		}
		
		return result;
	}
	
	public GeoIP getGeoIP() {
		return this.geoIP;
	}
	
    private final InputStream getResourceAsStream(String name)
    {
        return getClass().getClassLoader().getResourceAsStream(name);
    }
	
}
