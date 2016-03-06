package BankTeller;

import java.util.logging.Logger;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import BankTeller.event.player.SignClick;

public class BankTeller extends JavaPlugin {
	
	//Fires when the plugin is first enabled
	@Override
	public void onEnable() {
		//Loads info about the plugin from the plugin.yml
		PluginDescriptionFile pdFile = getDescription();
		//Used to log messages to console
    	Logger logger = getLogger();
    	
    	//****Register commands, events, config, and permissions here****
    	registerEvents();
    	
    	//logs to console that the plugin has been enabled
    	logger.info(pdFile.getName() + " has been enabled (V." + pdFile.getVersion() + ")");
	}
	
	//Fires when the plugin is being disabled
	@Override
	public void onDisable() {
		//Loads info about the plugin from the plugin.yml
		PluginDescriptionFile pdFile = getDescription();
		//Used to log messages to console
    	Logger logger = getLogger();
    	
    	//logs to console that the plugin has been disabled
    	logger.info(pdFile.getName() + " has been disabled (V." + pdFile.getVersion() + ")");
	}
	
	//Used to register events
	private void registerEvents() {
    	PluginManager pm = getServer().getPluginManager();
    	
    	pm.registerEvents(new SignClick(), this);
	}
}
