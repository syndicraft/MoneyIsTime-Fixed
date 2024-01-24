package net.syndicraft.moneyistime.config;

import net.republicraft.rccore.api.config.ConfigurationFile;
import org.bukkit.plugin.java.JavaPlugin;

public class MainConfig extends ConfigurationFile {
	
	private double payOut;
	private int payOutInterval;
	
	public MainConfig(JavaPlugin plugin) {
		super(plugin, "main.yml", "");
	}
	
	@Override
	public void validateSettings() {
	
	}
	
	@Override
	public void loadSettings() {
		this.payOut = this.getConfiguration().getDouble("PAY_OUT", 0.0);
		this.payOutInterval = this.getConfiguration().getInt("PAY_OUT_INTERVAL", 0);
	}
	
	public double getPayOut() {
		return payOut;
	}
	
	public int getPayOutInterval() {
		return payOutInterval;
	}
	
	
	
	
	
}
