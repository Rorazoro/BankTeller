package BankTeller.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;

public class BankTellerAdmin implements CommandExecutor {

	public static Economy econ = BankTeller.BankTeller.econ;
	public static Permission perms = BankTeller.BankTeller.perms;
	public static Chat chat = BankTeller.BankTeller.chat;
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (command.getName().equalsIgnoreCase("btadmin") && args.length == 2) {
			
			double amount = Double.parseDouble(args[0]);
			Player target = Bukkit.getPlayer(args[1]);
			OfflinePlayer targetoff;
			
			//checks if player is online, offline, or even exists on the server
			if (target == null || !target.isOnline()) {
				targetoff = Bukkit.getOfflinePlayer(args[1]);
				if (!targetoff.hasPlayedBefore()) {
					sender.sendMessage("Player doesn't exist on this server!");
					return false;
				}
				add(sender, amount, targetoff);
			} else {
				add(sender, amount, target);
			}
			
			return true;
			
		} else {
			sender.sendMessage("Invalid Command");
			return false;
		}
	}
	
	public void add(CommandSender sender, double amount, OfflinePlayer targetoff) {
		EconomyResponse teller = econ.depositPlayer(targetoff, amount);
		
		if (teller.transactionSuccess()) {
			sender.sendMessage("You gave " + teller.amount + " Gold to " + targetoff.getName());
		} 
		else {
			sender.sendMessage("An error has occured: " + teller.errorMessage);
		}
		
	}
}
