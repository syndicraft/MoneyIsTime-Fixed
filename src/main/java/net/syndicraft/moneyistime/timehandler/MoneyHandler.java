package net.syndicraft.moneyistime.timehandler;

import net.syndicraft.moneyistime.MoneyIsTime;
import net.syndicraft.moneyistime.config.Data;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class MoneyHandler {

	private static HashMap<Player, Long> loadedMoney = new HashMap<>();
	
	public static void playerCheckIn(Player player) {
		if (loadedMoney.containsKey(player)) return;
		loadedMoney.put(player, System.currentTimeMillis());
	}
	
	public static void playerCheckOut(Player player) {
		if (!loadedMoney.containsKey(player)) return;
		long time = System.currentTimeMillis() - loadedMoney.get(player);
		loadedMoney.remove(player);
		MoneyIsTime.getData().writeTime(player.getUniqueId(), MoneyIsTime.getData().getTime(player.getUniqueId()) + time);
	}
	
	public static void checkOutAllPlayers() {
		for (Player player : loadedMoney.keySet()) playerCheckOut(player);
	}
}
