package de.greenman1805.lobbyextra;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class NavigatorGUI implements Listener {
	public String title;
	public ItemStack navItem = new ItemStack(Material.COMPASS, 1);
	public ItemStack black_gap = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
	public ItemStack blue_gap = new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1);
	Inventory inv = null;

	public NavigatorGUI(String title) {
		this.title = title;
		Bukkit.getPluginManager().registerEvents(this, Main.plugin);
		inv = Bukkit.createInventory(null, 27, title);
		Main.setItemName(black_gap, " ", null);
		Main.setItemName(blue_gap, " ", null);
		Main.setItemName(navItem, "§9§lNavigator §8» §7Rechtsklick §8«", null);
		for (int i = 0; i < 9; i++) {
			inv.setItem(i, black_gap);
		}
		for (int i = 9; i < 18; i++) {
			inv.setItem(i, blue_gap);
		}
		for (int i = 18; i < 27; i++) {
			inv.setItem(i, black_gap);
		}
	}

	public void add(NavigatorItem navi, int place) {
		inv.setItem(place, navi.item);
	}

	public void openInventory(Player p) {
		p.openInventory(inv);
	}

	@EventHandler
	public void itemInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (p.getInventory().getItemInMainHand().getType() == navItem.getType()) {
			openInventory(p);
			e.setCancelled(true);
		}
		p.updateInventory();
		if (!e.getPlayer().hasPermission("lobbyextra.admin")) {
			e.setCancelled(true);
		}
	}



}
