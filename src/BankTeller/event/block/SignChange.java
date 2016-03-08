package BankTeller.event.block;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChange implements Listener {
	
	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		
		//Checks if a sign was made with [bankteller] on it
		if (event.getLine(0).equalsIgnoreCase("[bankteller]")) {
			event.setLine(0, "§6[BankTeller]");
			event.setLine(2, "§aOpen");
		}
	}
}
