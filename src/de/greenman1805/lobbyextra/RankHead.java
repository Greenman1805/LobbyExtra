package de.greenman1805.lobbyextra;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import de.greenman1805.ranks.RankGUI;

public class RankHead implements Listener {
	public ItemStack head;

	public RankHead(UUID uuid) {
		createHead(uuid);
		Bukkit.getPluginManager().registerEvents(this, Main.plugin);
	}

	private void createHead(UUID uuid) {
		head = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta skull = (SkullMeta) head.getItemMeta();
		skull.setDisplayName("§6§lRänge §8» §7Rechtsklick §8«");
		skull.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
		head.setItemMeta(skull);
	}

	@EventHandler
	public void itemInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();

		if (p.getInventory().getItemInMainHand().getType() == head.getType()) {
			new RankGUI(p);
			e.setCancelled(true);
		}
		p.updateInventory();
		if (!e.getPlayer().hasPermission("lobbyextra.admin")) {
			e.setCancelled(true);
		}
	}

}
