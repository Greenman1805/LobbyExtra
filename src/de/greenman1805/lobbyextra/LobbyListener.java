package de.greenman1805.lobbyextra;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public class LobbyListener implements Listener {

	public LobbyListener() {
		Bukkit.getPluginManager().registerEvents(this, Main.plugin);
	}

	@EventHandler
	public void playerJoin(PlayerJoinEvent e) {
		final Player p = e.getPlayer();
		p.getInventory().clear();
		p.getInventory().setItem(6, Main.eb.item);
		p.getInventory().setItem(4, Main.navGUI.navItem);
		p.getInventory().setItem(2, new RankHead(p.getUniqueId()).head);
		p.getInventory().setChestplate(new ItemStack(Material.ELYTRA, 1));
		p.updateInventory();
	}

	@EventHandler
	public void disableOffHand(PlayerSwapHandItemsEvent e) {
		if (!e.getPlayer().hasPermission("lobbyextra.admin")) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void disableEntityInteract(PlayerInteractAtEntityEvent e) {
		if (!e.getPlayer().hasPermission("lobbyextra.admin")) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void disableEntityDamage(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			if (!p.hasPermission("lobbyextra.admin")) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void PlayerQuitListener(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
	}

	@EventHandler
	public void PlayerJoinListener(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		ScoreboardAPI.updateScoreboard(p);
	}

	@EventHandler
	public void cancelPlayerDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void cancelItemMove(InventoryClickEvent e) {
		if (!e.getWhoClicked().hasPermission("lobbyextra.admin")) {
			Player p = (Player) e.getWhoClicked();
			if (e.getCurrentItem() != null) {
				e.setCancelled(true);
				p.updateInventory();
			}
		}
	}

	@EventHandler
	public void cancelItemDrop(PlayerDropItemEvent e) {
		if (!e.getPlayer().hasPermission("lobbyextra.admin")) {
			if (e.getItemDrop().getItemStack().getType() == Material.WRITABLE_BOOK) {
				e.getPlayer().sendMessage("§aSchreibe erst deinen Wunsch in das Buch und signiere es!");
			}
			if (e.getItemDrop().getItemStack().getType() != Material.WRITTEN_BOOK) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void getBook(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.SIGN) {
			Sign sign = (Sign) e.getClickedBlock().getState();
			if (sign.getLine(0).contains("Klick")) {
				ItemStack book = new ItemStack(Material.WRITABLE_BOOK);
				p.getInventory().setItem(8, book);
				p.updateInventory();
			}
		}
	}

	@EventHandler
	public void PlayerPickupItem(EntityPickupItemEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (!p.hasPermission("lobbyextra.admin")) {
				e.setCancelled(true);
			}
		}
	}

}
