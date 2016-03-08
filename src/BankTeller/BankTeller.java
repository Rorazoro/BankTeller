package BankTeller;

import java.util.logging.Logger;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import BankTeller.event.block.SignChange;
import BankTeller.event.player.SignClick;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class BankTeller extends JavaPlugin {
	
	public static Economy econ = null;
	public static Permission perms = null;
	public static Chat chat = null;
	
	//Fires when the plugin is first enabled
	@Override
	public void onEnable() {
		
		//Loads info about the plugin from the plugin.yml
		PluginDescriptionFile pdFile = getDescription();
		//Used to log messages to console
    	Logger logger = getLogger();
    	
    	//**Vault setup**
    	if (!setupVaultEconomy() ) {
            logger.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupVaultPermissions();
        setupVaultChat();
    	
    	//****Register commands, events, config, and permissions here****
    	registerEvents();
    	registerCommands();
    	registerConfig();
    	registerPermissions();
    	
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
    	pm.registerEvents(new SignChange(), this);
	}
	
	//Used to register commands
	private void registerCommands() {
		
	}
	
	//Used to register permissions
	private void registerPermissions() {
		
	}
	
	//Used to register config
	private void registerConfig() {
		
	}
	
	//Used to setup Vault Economy and check for vault api
	private boolean setupVaultEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
	
	//Used to setup Vault Chat
    private boolean setupVaultChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }
    
    //Used to setup Vault Permissions
    private boolean setupVaultPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
}
