package de.greenman1805.lobbyextra;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

public class DroppedShards implements Listener {
	public static List<DroppedShards> shards = new ArrayList<DroppedShards>();
	Hologram holo;

	public DroppedShards(Location startFrom, int radius) {
		if (shards.size() <= 30) {
			Location randomLocation = getRandomLocation(startFrom, radius);
			holo = HologramsAPI.createHologram(Main.plugin, randomLocation.add(0.5D, 1D, 0.5D));
			holo.insertTextLine(0, "§910 Shards");
			holo.appendItemLine(new ItemStack(Material.PRISMARINE_CRYSTALS, 1));
			shards.add(this);
			Bukkit.getPluginManager().registerEvents(this, Main.plugin);
		}
	}
	
	public static void startDroppingShards() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {

			@Override
			public void run() {
				new DroppedShards(new Location(Bukkit.getWorld("world"), -368, 255, -952), 50);

			}

		}, 20 * 5, 20 * 20);
	}

	public Location getRandomLocation(Location startFrom, int radius) {
		Random random = new Random();
		Location output = startFrom.clone();
		output.add(random.nextInt(radius) - random.nextInt(radius), random.nextInt(radius) - random.nextInt(radius), random.nextInt(radius) - random.nextInt(radius));
		output.setY(255);
		while (output.getBlockY() > 0) {
			Material type = output.getBlock().getType();
			if (type == Material.AIR) {
				output.subtract(0, 1, 0);
			} else if (type == Material.WATER || type == Material.LAVA|| type == Material.ACACIA_LEAVES|| type == Material.BIRCH_LEAVES || type == Material.DARK_OAK_LEAVES || type == Material.JUNGLE_LEAVES || type == Material.OAK_LEAVES || type == Material.SPRUCE_LEAVES) {
				return getRandomLocation(startFrom, radius);
			} else {
				output.add(0, 1, 0);
				return output;
			}
		}
		return getRandomLocation(startFrom, radius);
	}


	@EventHandler
	public void MoveListener(PlayerMoveEvent e) {
		if (holo != null) {
			Player p = e.getPlayer();
			if (holo.getLocation().getBlockX() == p.getLocation().getBlockX() && holo.getLocation().getBlockZ() == p.getLocation().getBlockZ()) {
				remove();
				Main.econ.depositPlayer(p, 10);
				p.sendMessage(" §c+ §a10 Shards");
			}
		}

	}

	public void remove() {
		holo.delete();
		shards.remove(this);
		holo = null;
	}

	public static void removeAll() {
		for (DroppedShards s : shards) {
			s.holo.delete();
		}
	}

}
