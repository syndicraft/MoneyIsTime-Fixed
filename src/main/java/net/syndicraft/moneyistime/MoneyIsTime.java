package net.syndicraft.moneyistime;

import com.earth2me.essentials.Essentials;
import net.republicraft.rccore.api.Text;
import net.syndicraft.moneyistime.config.Data;
import net.syndicraft.moneyistime.config.MainConfig;
import net.syndicraft.moneyistime.essentials.Listeners;
import net.syndicraft.moneyistime.signs.Sign;
import net.syndicraft.moneyistime.timehandler.MoneyHandler;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.events.Event;

import java.util.HashMap;
import java.util.logging.Level;

public final class MoneyIsTime extends JavaPlugin {
	
	private static MainConfig mainConfig;
	private static Data data;
	private static Essentials essentials;
	public static HashMap<Integer,Sign> p_signs;
	public static HashMap<Integer,Location> p_location;
	
	
	@Override
	public void onEnable() {
		// Plugin startup logic
		essentials = (Essentials) getServer().getPluginManager().getPlugin("Essentials");
		if (essentials == null) {
			Text.logToConsole(Level.SEVERE, "Essentials is not installed! Disabling MoneyIsTime...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		p_signs  = new HashMap<>();
		p_location = new HashMap<>();
		mainConfig = new MainConfig(this);
		data = new Data(this);
		this.getServer().getPluginManager().registerEvents(new Listeners(), this);
	}
	
	@Override
	public void onDisable() {
		// Plugin shutdown logic
		MoneyHandler.checkOutAllPlayers();
	}
	public static MainConfig getMainConfig() {
		return mainConfig;
	}
	
	public static Data getData() {
		return data;
	}
	
	public static Essentials getEssentials() {
		return essentials;
	}
	
	public static int getNextAvailableInt() {
		int i = 0;
		while (data.getConfiguration().contains("PLUGIN_DATA.SIGNS." + i)) i++;
		return i;
	}
}
