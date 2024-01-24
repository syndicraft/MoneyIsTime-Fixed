package net.syndicraft.moneyistime.signs;

import net.syndicraft.moneyistime.MoneyIsTime;
import net.syndicraft.moneyistime.config.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import static net.syndicraft.moneyistime.MoneyIsTime.p_location;
import static net.syndicraft.moneyistime.MoneyIsTime.p_signs;

public class Sign {
	
	private int signID;
	private Location location;
	
	public Sign(String worldName, int x, int y, int z, int signID) {
		World world = Bukkit.getWorld(worldName);
		if (world == null) return;
		this.location = new Location(world, x, y, z);
		this.signID = signID;
	}
	
	public Sign(Location location) {
		this.location = location;
		this.signID = MoneyIsTime.getNextAvailableInt();
		MoneyIsTime.getData().addSign(this);
	}
	
	public Location getLocation() {
		if (p_location != null){
			for (Location loc : p_location.values()){
				return this.location = loc;
			}
		}else{
			return this.location;
		}
		return this.location;
	}
	
	public int getSignID() {
		return signID;
	}
}
