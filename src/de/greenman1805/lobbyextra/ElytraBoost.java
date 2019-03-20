package de.greenman1805.lobbyextra;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ElytraBoost implements Listener {
	public ItemStack item = new ItemStack(Material.FIREWORK_ROCKET, 1);

	public ElytraBoost() {
		Main.setItemName(item, "§a§lElytraBoost §8» §7Rechtsklick §8«", null);
		Bukkit.getPluginManager().registerEvents(this, Main.plugin);
	}

	@EventHandler
	public void itemInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (p.getInventory().getItemInMainHand().getType() == item.getType()) {
			useElytraBoost(p);
			e.setCancelled(true);
		}
		p.updateInventory();
		if (!e.getPlayer().hasPermission("lobbyextra.admin")) {
			e.setCancelled(true);
		}
	}

	private void useElytraBoost(Player p) {
		Vector v = p.getLocation().getDirection().multiply(2.0D);
		p.setVelocity(v);
	}

}
