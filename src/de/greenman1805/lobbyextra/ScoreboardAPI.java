package de.greenman1805.lobbyextra;

import org.bukkit.entity.Player;

import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;

public class ScoreboardAPI {

	public static void updateScoreboard(Player p) {
		Sidebar sidebar = new Sidebar(" §9Skyshard§f.de ", Main.plugin);
		
		sidebar.addEmpty();
		
		for (NavigatorItem nav : NavigatorItem.items) {
			sidebar.addEntry(new SidebarString(nav.name));
			sidebar.addEntry(new SidebarString("§f» " + nav.playerCount +" Spieler"));
			sidebar.addEmpty();
		}

		SidebarString line1 = new SidebarString("§f» §9Shards:");
		SidebarString line2 = new SidebarString("§f» §f" + Main.econ.getBalance(p));
		sidebar.addEntry(line1);
		sidebar.addEntry(line2);

		sidebar.showTo(p);
	}

}
