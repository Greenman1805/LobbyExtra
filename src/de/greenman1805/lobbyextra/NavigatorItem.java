package de.greenman1805.lobbyextra;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class NavigatorItem implements Listener {
	String server;
	String name;
	int port;
	ItemStack item;
	int place;
	List<String> lore_list;
	int playerCount;
	public static List<NavigatorItem> items = new ArrayList<NavigatorItem>();

	public NavigatorItem(String name, String server, Material m, short data, int place, String version, int port) {
		item = new ItemStack(m, 1);
		this.name = name;
		this.place = place;
		this.port = port;
		lore_list = new ArrayList<String>();
		lore_list.add("§7--------------");
		lore_list.add("");
		lore_list.add("§8» §2Beste Version:");
		lore_list.add("§8» §a" + version);
		lore_list.add("");
		lore_list.add("§8» §6Spieler:§e {players}");
		Main.setItemName(item, name, lore_list);
		this.server = server;
		Bukkit.getPluginManager().registerEvents(this, Main.plugin);
		items.add(this);
		updatePlayerCount(0);
	}

	public void updatePlayerCount(int playerCount) {
		ItemMeta meta;
		meta = item.getItemMeta();
		List<String> newlore = new ArrayList<String>();
		for (String s : lore_list) {
			s = s.replace("{players}", playerCount + "");
			newlore.add(s);
		}
		meta.setLore(newlore);
		item.setItemMeta(meta);
		this.playerCount = playerCount;
		Main.navGUI.add(this, place);
	}


	@SuppressWarnings("deprecation")
	@EventHandler
	public void clickedOnItem(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (e.getClickedInventory() != null) {
			if (e.getClickedInventory().getTitle().equalsIgnoreCase(Main.navGUI.title)) {
				e.setCancelled(true);
				if (e.getCurrentItem() != null) {
					if (e.getCurrentItem().getType().equals(item.getType())) {
						connectToServer(p);
						e.setCancelled(true);
					}
				}
			}
		}
	}

	public void connectToServer(Player p) {
		if (server != null) {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Connect");
			out.writeUTF(server);
			p.sendPluginMessage(Main.plugin, "BungeeCord", out.toByteArray());
		}
	}

}
