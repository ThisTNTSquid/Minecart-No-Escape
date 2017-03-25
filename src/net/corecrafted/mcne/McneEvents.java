package net.corecrafted.mcne;

import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class McneEvents implements Listener {

	@EventHandler
	public void onDismount(VehicleExitEvent e) {
		if (e.getVehicle() instanceof RideableMinecart){
			if (e.getExited() instanceof Player){
				Player player = (Player) e.getExited();
				if (McneCommands.lockedPlayer.contains(player)){
					e.setCancelled(true);
				}
					
			}
		}
	}
}
