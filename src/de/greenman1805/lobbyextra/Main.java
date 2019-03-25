package de.greenman1805.lobbyextra;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {
	public static Economy econ = null;
	public static Main plugin;

	public static NavigatorGUI navGUI;
	public static ElytraBoost eb;

	@Override
	public void onEnable() {
		if (!setupEconomy()) {
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		plugin = this;
		new LobbyListener();
		navGUI = new NavigatorGUI("§8» §9Navigator");
		eb = new ElytraBoost();

		new NavigatorItem("§f» §aSurvival", "Survival", Material.DIAMOND_PICKAXE, (short) 0, 13, "1.13.2", 25583);
		new NavigatorItem("§f» §9Citybuild", "Citybuild", Material.SANDSTONE, (short) 0, 15, "1.13.2", 25584);
		new NavigatorItem("§f» §6GunPvP", "GunPvP", Material.GOLDEN_HORSE_ARMOR, (short) 0, 11, "1.12.2", 25582);

		DroppedShards.startDroppingShards();
		updatingServerPlayerCount();
	}

	public static void setItemName(ItemStack item, String name, List<String> lore_list) {
		ItemMeta meta;
		meta = item.getItemMeta();
		meta.setLore(lore_list);
		meta.setDisplayName(name);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(meta);
	}

	@Override
	public void onDisable() {
		DroppedShards.removeAll();
	}

	public void updatingServerPlayerCount() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

			@Override
			public void run() {
				for (NavigatorItem nav : NavigatorItem.items) {
					updateServerPlayerCount(nav);
				}
				for (Player p : Bukkit.getOnlinePlayers()) {
					ScoreboardAPI.updateScoreboard(p);
				}
			}

		}, 20 * 4, 20 * 4);
	}

	private void updateServerPlayerCount(final NavigatorItem nav) {
		Bukkit.getScheduler().runTaskAsynchronously(this, new Runnable() {

			@Override
			public void run() {
				try (Socket socket = new Socket("127.0.0.1", nav.port)) {
					socket.setSoTimeout(1000);

					DataOutputStream out = new DataOutputStream(socket.getOutputStream());
					DataInputStream in = new DataInputStream(socket.getInputStream());

					out.write(0xFE);

					int b;
					StringBuffer str = new StringBuffer();
					while ((b = in.read()) != -1) {
						if (b != 0 && b > 16 && b != 255 && b != 23 && b != 24) {
							str.append((char) b);
						}
					}

					final String[] data = str.toString().split(ChatColor.COLOR_CHAR + "");
					Bukkit.getScheduler().runTask(Main.plugin, new Runnable() {

						@Override
						public void run() {
							nav.updatePlayerCount(Integer.parseInt(data[1]));
						}
						
					});
				} catch (UnknownHostException e) {
				} catch (InterruptedIOException e) {
				} catch (IOException e) {
				}
			}
			
		});
	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

}
