package net.syndicraft.moneyistime.config;

import net.republicraft.rccore.api.config.ConfigurationFile;
import net.syndicraft.moneyistime.MoneyIsTime;
import net.syndicraft.moneyistime.signs.Sign;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

import static net.syndicraft.moneyistime.MoneyIsTime.*;

public class Data extends ConfigurationFile {
	
	private HashMap<Integer, Sign> signs = new HashMap<>();
	public Data(JavaPlugin plugin) {
		super(plugin, "data.yml", "");
	}


	@Override
	public void validateSettings() {
	
	}
	
	@Override
	public void loadSettings() {
		if(this.signs == null){
			//System.out.println("Signs is null");
			this.signs = new HashMap<>();
		}
		ConfigurationSection signSection = this.getConfiguration().getConfigurationSection("PLUGIN_DATA.SIGNS");
		if (signSection == null) return;
		//plugin.getLogger().info("ConfigSecKeys: "+signSection.getKeys(false));
		for (String key : signSection.getKeys(false)) {
			ConfigurationSection sign = signSection.getConfigurationSection(key);
			//plugin.getLogger().info("DEBUG SIGN: " + sign);
			if (sign == null) continue;
			int id = Integer.parseInt(key);
			//plugin.getLogger().info("ID: " + id);
			if (id == -1) continue;
			String world = sign.getString("WORLD", "");
			//plugin.getLogger().info("WORLD: " + world);
			int x = sign.getInt("X", -1);
			int y = sign.getInt("Y", -1);
			int z = sign.getInt("Z", -1);
			//System.out.println(world + " " + x + " " + y + " " + z);
			if (x == -1 || y == -1 || z == -1) continue;
			this.signs.put(id, new Sign(world, x, y, z, id));
			p_signs.put(id,new Sign(world,x,y,z,id));
			p_location.put(id,new Location(Bukkit.getWorld(world),x,y,z));
		}
		//plugin.getLogger().info("SIGNS: " + this.signs);
		//plugin.getLogger().info("PSIGNS: "+ p_signs.values());
		//plugin.getLogger().info("PLOCS: "+ p_location.values());
	}
	
	
	public long getTime(UUID uuid) {
		ConfigurationSection userSection = this.getConfiguration().getConfigurationSection("PLUGIN_DATA.USERS");
		if (userSection == null) return 0;
		return userSection.getLong(uuid.toString(), 0);
	}
	
	public void writeTime(UUID uuid, long time) {
		ConfigurationSection userSection = this.getConfiguration().getConfigurationSection("PLUGIN_DATA.USERS");
		if (userSection == null) userSection = this.getConfiguration().createSection("PLUGIN_DATA.USERS");
		userSection.set(uuid.toString(), time);
		this.save();
		this.reloadSettings();
	}
	
	public boolean isSign(Location location) {
		for (Sign sign : p_signs.values()) {
			if (sign.getLocation().equals(location)) return true;
		}
		return false;
	}
	
	public Sign getSign(Location location) {
		for (Sign sign : p_signs.values()) {
			if (sign.getLocation().equals(location)) return sign;
		}
		return null;
	}
	
	public boolean isAcceptableButton(Location location) {
		//plugin.getLogger().info("SIGNS: " + p_signs.values());
		//plugin.getLogger().info("SINGLOC: " + p_signs.get(0).getLocation());
        for (Sign sign : p_signs.values()) {
			//System.out.println("SIGN LOCATION "+sign.getLocation() + " " + location);
			if (sign.getLocation().getBlockY()-1 == location.getBlockY()) return true;
		}
		return false;
	}
	
	public void deleteSign(Sign sign) {
		this.getConfiguration().set("PLUGIN_DATA.SIGNS." + sign.getSignID(), null);
		save();
		reloadSettings();
	}
	
	public void addSign(Sign sign) {
		this.getConfiguration().set("PLUGIN_DATA.SIGNS." + sign.getSignID() + ".WORLD", sign.getLocation().getWorld().getName());
		this.getConfiguration().set("PLUGIN_DATA.SIGNS." + sign.getSignID() + ".X", sign.getLocation().getBlockX());
		this.getConfiguration().set("PLUGIN_DATA.SIGNS." + sign.getSignID() + ".Y", sign.getLocation().getBlockY());
		this.getConfiguration().set("PLUGIN_DATA.SIGNS." + sign.getSignID() + ".Z", sign.getLocation().getBlockZ());
		save();
		reloadSettings();
	}
	
	
}
