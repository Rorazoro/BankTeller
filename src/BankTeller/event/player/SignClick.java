package BankTeller.event.player;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignClick implements Listener {
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
		
		event.setCancelled(true);
		player.sendMessage("You clicked a BankTeller!");
	}
}
