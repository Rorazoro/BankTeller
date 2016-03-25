package BankTeller.event.player;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;

public class SignClick implements Listener {
	
	public static Economy econ = BankTeller.BankTeller.econ;
	public static Permission perms = BankTeller.BankTeller.perms;
	public static Chat chat = BankTeller.BankTeller.chat;
	
	HashMap<String, Long> SignCache = new HashMap<String, Long>();
	HashMap<String, Long> ServiceCache = new HashMap<String, Long>();
	HashMap<String, Long> TransferCache = new HashMap<String, Long>();
	HashMap<String, Long> TransferAmountCache = new HashMap<String, Long>();
	HashMap<String, Long> WithdrawCache = new HashMap<String, Long>();
	HashMap<String, Long> DepositCache = new HashMap<String, Long>();
	
	@EventHandler
	public void onSignClick(PlayerInteractEvent event) {
		//checks if event was started by player
		if(!(event.getPlayer() instanceof Player)) {
			return;
		}
		
		//checks if the event involves a block. If not, cancels event
		if (!event.hasBlock()) {
			return;
		}
		
		//checks for right click
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		
		Block block = event.getClickedBlock();
		
		//checks if the state of the block is a instance of sign
		if (!(block.getState() instanceof Sign)) {
			return;
		}
		
		Sign sign = (Sign) block.getState();
		Player player = event.getPlayer();
		
		//checks if first line of sign is [bankteller]
		if(!(sign.getLine(0).equalsIgnoreCase("§6[bankteller]"))) {
			return;
		}
		
		/*
		 * look for gold nuggets, gold ingots, and gold blocks
		 * Nugget: 371
		 * Ingot: 266
		 * Block: 41
		 */
		int balanceInv = countInv(player, 371) + (countInv(player, 266) * 9) + (countInv(player, 41) * 81);
		int balance = (int) econ.getBalance(player) - balanceInv;
		
		//Check if this is the "open right click"
		if(sign.getLine(2).equalsIgnoreCase("§aopen")) {
			sign.setLine(2, "§cOccupied");
			player.sendMessage(ChatColor.GOLD + "[************* Thank you for using BankTeller! *************]");
			player.sendMessage(ChatColor.GREEN + "Bank Account: " + ChatColor.GOLD + balance);
			player.sendMessage(ChatColor.GREEN + "Inventory: " + ChatColor.GOLD + balanceInv + "\n \n");
			
			player.sendMessage(ChatColor.GREEN + "Please choose a service: (Type first letter or whole word)\n \n");
			
			player.sendMessage(ChatColor.YELLOW + "              DEPOSIT   " + ChatColor.RED + "   WITHDRAW   " + ChatColor.BLUE + "   TRANSFER\n \n");
			
			SignCache.put(player.getName(), System.currentTimeMillis());
			ServiceCache.put(player.getName(), System.currentTimeMillis());
		}
		
	}
	
	//handles player input after click on bankteller sign
	@EventHandler
	public void SignInput(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		String message = event.getMessage().toLowerCase();
		
		if (SignCache.containsKey(player.getName())) {
			
			if (ServiceCache.containsKey(player.getName())) {
				ServiceInput(player, message);
			}
			
			else if (DepositCache.containsKey(player.getName())) {
				DepositInput(player, message);
			}
			
			else if (WithdrawCache.containsKey(player.getName())) {
				WithdrawInput(player, message);
			}
			
			else {
				player.sendMessage("Error! Player not in any cache!");
			}
			event.setCancelled(true);
		}
		return;
	}
	
	public void ServiceInput (Player player, String message) {
		
		if (ServiceCache.containsKey(player.getName())) {
			if (message.contentEquals("d") || message.contentEquals("deposit")) {
				player.sendMessage("Please enter the amount you would like to deposit:");
				DepositCache.put(player.getName(), System.currentTimeMillis());
				player.sendMessage("Deposit fired");
				
			} else if (message.contentEquals("w") || message.contentEquals("withdraw")) {
				player.sendMessage("Please enter the amount you would like to withdraw:");
				WithdrawCache.put(player.getName(), System.currentTimeMillis());
				player.sendMessage("Withdraw fired");
				
			} else if (message.contentEquals("t") || message.contentEquals("transfer")) {
				player.sendMessage("Please enter the of the player you are transfering to:");
				TransferCache.put(player.getName(), System.currentTimeMillis());
				player.sendMessage("Transfer fired");
				
			} else {
				player.sendMessage("Invalid Input. Try again");
				player.sendMessage("Invalid Input fired");
			}
			ServiceCache.remove(player.getName());
		}
	}
	
	//Handles player input for depositing
	public void DepositInput(Player player, String message) {
		
		player.sendMessage("DepositInput Started");
		
		if (DepositCache.containsKey(player.getName())) {
			if (message.matches("\\d+")) {
				
				int Inv = countInv(player, 371) + (countInv(player, 266) * 9) + (countInv(player, 41) * 81);
				int amount = Integer.parseInt(message);
				
				if ((Inv - amount) >= 0) {
					deposit(player, Inv, amount);
					
				} else {
					player.sendMessage("Invalid Input. You don't have " + message + " Gold in your inventory");
				}
			} else {
				player.sendMessage("Invalid Input. Must be a number.");
			}
			DepositCache.remove(player.getName());
		}
	}
	
	public void WithdrawInput(Player player, String message) {
		
		if (WithdrawCache.containsKey(player.getName())) {
			if (message.matches("\\d+")) {
				
				int Inv = countInv(player, 371) + (countInv(player, 266) * 9) + (countInv(player, 41) * 81);
				int amount = Integer.parseInt(message);
				
				if (((econ.getBalance(player) - Inv) - amount) >= 0) {
					withdraw(player, Inv, amount);
					
				} else {
					player.sendMessage("Invalid Input. You don't have " + message + " Gold in your account");
				}
			} else {
				player.sendMessage("Invalid Input. Must be a number.");
			}
			WithdrawCache.remove(player.getName());
		}
	}
	
	public void TransferInput() {
		
	}

	public static int countInv(Player player, int id)
	{
        PlayerInventory inventory = player.getInventory();
        ItemStack[] items = inventory.getContents();
        int has = 0;
        
        for (ItemStack item : items)
        {
            if ((item != null) && (item.getTypeId() == id) && (item.getAmount() > 0))
            {
            	has += item.getAmount();
            }
        }
        return has;
    }
	
	public static int[] convertToItems(int amount) {
		int items[] = {0, 0, 0};
		
		items[0] = amount / 81;
		amount = amount % 81;
		
		items[1] = amount / 9;
		amount = amount % 9;
		
		items[2] = amount;
		
		return items;
	}
	
	public static void deposit(Player player, int InvAmount, int amount) {
		
		PlayerInventory Inv = player.getInventory();
		InvAmount = InvAmount - amount;
		
		int items[] = {41, 266, 371};
		int itemamounts[] = {0, 0, 0};
		
		for (int i = 0; i < 3; i++) {
			itemamounts[i] = countInv(player, items[i]);
		}
		
		int newInv[] = convertToItems(InvAmount);
		
		Inv.remove(Material.GOLD_NUGGET);
		Inv.remove(Material.GOLD_INGOT);
		Inv.remove(Material.GOLD_BLOCK);
		
		ItemStack item = new ItemStack(Material.GOLD_BLOCK, newInv[0]);
		Inv.addItem(item);
		item = new ItemStack(Material.GOLD_INGOT, newInv[1]);
		Inv.addItem(item);
		item = new ItemStack(Material.GOLD_NUGGET, newInv[2]);
		Inv.addItem(item);
		player.updateInventory();
		
		EconomyResponse teller = econ.depositPlayer(player, amount);
		if (teller.transactionSuccess()) {
			player.sendMessage("You deposited " + teller.amount + " Gold");
		} 
		else {
			player.sendMessage("An error has occured: " + teller.errorMessage);
		}
	}
	
	public static void withdraw(Player player, int InvAmount, int amount) {
		
		PlayerInventory Inv = player.getInventory();
		InvAmount = InvAmount + amount;
		
		int newInv[] = convertToItems(InvAmount);
		
		Inv.remove(Material.GOLD_NUGGET);
		Inv.remove(Material.GOLD_INGOT);
		Inv.remove(Material.GOLD_BLOCK);
		
		ItemStack item = new ItemStack(Material.GOLD_BLOCK, newInv[0]);
		Inv.addItem(item);
		item = new ItemStack(Material.GOLD_INGOT, newInv[1]);
		Inv.addItem(item);
		item = new ItemStack(Material.GOLD_NUGGET, newInv[2]);
		Inv.addItem(item);
		player.updateInventory();
		
		EconomyResponse teller = econ.withdrawPlayer(player, amount);
		if (teller.transactionSuccess()) {
			player.sendMessage("You withdrew " + teller.amount + " Gold");
		} 
		else {
			player.sendMessage("An error has occured: " + teller.errorMessage);
		}
	}
	
	public static void transfer() {
		
	}
}
