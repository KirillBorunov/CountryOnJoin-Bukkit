package sososlik.countryonjoin;

import java.util.HashMap;

public class Config
{
	//NOTE: ensure these defaults are synced with the hardcoded config.yml resource
	//TODO: we need to generate the config.yml from this class (how to add the comments?)
	private boolean replaceDefaultJoinMessage = true;
	private boolean broadcastOnUnknownCountry = true;
	private boolean broadcastAltJoinMsgOnUnknownCountry = true;
	private String joinWithCountryMessage = "&e{0}&r&e joined from &c{1}&r&e";
	private String joinWithoutCountryMessage = "&e{0}&r&e joined";
	private HashMap<String, String> countryNames;
	private String messagesCulture = "en-US";
	private String countrynamesCulture = "en-US";
	private boolean enablePlaceholderAPIHook = true;
	private boolean debug = false;
	private String geoIPDB = "GeoLite2-Country.mmdb";

	public Config()
	{
		this.countryNames = new HashMap<String, String>();
	}
	
	public boolean getReplaceDefaultJoinMessage() {
		return replaceDefaultJoinMessage;
	}
	public void setReplaceDefaultJoinMessage(boolean replaceDefaultJoinMessage) {
		this.replaceDefaultJoinMessage = replaceDefaultJoinMessage;
	}
	public boolean getBroadcastAltJoinMsgOnUnknownCountry() {
		return broadcastAltJoinMsgOnUnknownCountry;
	}
	public void setBroadcastAltJoinMsgOnUnknownCountry(boolean broadcastAltJoinMsgOnUnknownCountry) {
		this.broadcastAltJoinMsgOnUnknownCountry = broadcastAltJoinMsgOnUnknownCountry;
	}
	public String getJoinWithoutCountryMessage() {
		return joinWithoutCountryMessage;
	}
	public void setJoinWithoutCountryMessage(String joinWithoutCountryMessage) {
		this.joinWithoutCountryMessage = joinWithoutCountryMessage;
	}
	public boolean getDebug() {
		return debug;
	}
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	public String getMessagesCulture() {
		return messagesCulture;
	}
	public void setMessagesCulture(String messagesCulture) {
		this.messagesCulture = messagesCulture;
	}
	public String getCountrynamesCulture() {
		return countrynamesCulture;
	}
	public void setCountrynamesCulture(String countrynamesCulture) {
		this.countrynamesCulture = countrynamesCulture;
	}
	public boolean getBroadcastOnUnknownCountry() {
		return broadcastOnUnknownCountry;
	}
	public void setBroadcastOnUnknownCountry(boolean broadcastOnUnknownCountry) {
		this.broadcastOnUnknownCountry = broadcastOnUnknownCountry;
	}
	public String getJoinWithCountryMessage() {
		return joinWithCountryMessage;
	}
	public void setJoinWithCountryMessage(String joinWithCountryMessage) {
		this.joinWithCountryMessage = joinWithCountryMessage;
	}
	public HashMap<String, String> getCountryNames() {
		return countryNames;
	}
	public void setCountryNames(HashMap<String, String> countryNames) {
		this.countryNames = countryNames;
	}
	public boolean getEnablePlaceholderAPIHook() {
		return this.enablePlaceholderAPIHook;
	}
	public void setEnablePlaceholderAPIHook(boolean enablePlaceholderAPIHook) {
		this.enablePlaceholderAPIHook = enablePlaceholderAPIHook;
	}
	public String getGeoIPDB() {
		return geoIPDB;
	}
	public void setGeoIPDB(String geoIPDB) {
		this.geoIPDB = geoIPDB;
	}
	
}
