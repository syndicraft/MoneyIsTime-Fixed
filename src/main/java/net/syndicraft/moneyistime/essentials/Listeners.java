package net.syndicraft.moneyistime.essentials;

import com.earth2me.essentials.User;
import io.papermc.paper.event.player.PlayerOpenSignEvent;
import net.ess3.api.events.AfkStatusChangeEvent;
import net.ess3.api.events.SignCreateEvent;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.BaseComponent;
import net.republicraft.rccore.api.Text;
import net.syndicraft.moneyistime.MoneyIsTime;
import net.syndicraft.moneyistime.signs.Sign;
import net.syndicraft.moneyistime.timehandler.MoneyHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Listeners implements Listener {
	
	@EventHandler
	public void onPlayerAFKChange(AfkStatusChangeEvent event) {
		if (!event.getAffected().getBase().hasPermission("syndicraft.moneyistime")) return;
		if (event.getAffected().getBase().hasPermission("syndicraft.moneyistime.afkexempt")) return;
		if (event.getValue()) {
			if (!(event.getAffected() instanceof User user)) return;
			MoneyHandler.playerCheckOut(event.getAffected().getBase());
		} else {
			if (!(event.getAffected() instanceof User user)) return;
			MoneyHandler.playerCheckIn(event.getAffected().getBase());
		}
	}
	
	@EventHandler
	public void onPlayerConnect(PlayerJoinEvent event) {
		if (!event.getPlayer().hasPermission("syndicraft.moneyistime")) return;
		MoneyHandler.playerCheckIn(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerDisconnect(PlayerQuitEvent event) {
		if (!event.getPlayer().hasPermission("syndicraft.moneyistime")) return;
		MoneyHandler.playerCheckOut(event.getPlayer());
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onSignInteract(PlayerOpenSignEvent event) {
		if (!event.getPlayer().hasPermission("syndicraft.moneyistime.checkout")) return;
		if (!MoneyIsTime.getData().isSign(event.getSign().getLocation())) return;
		MoneyHandler.playerCheckOut(event.getPlayer());
		long time = MoneyIsTime.getData().getTime(event.getPlayer().getUniqueId());
		if (time <= 999) {
			event.getPlayer().sendMessage(Text.color("&7[&6Syndicraft&7] &cYou have no money to check out yet."));
			return;
		}
		long previousTime = time % 1000;
		long seconds = (time/1000);
		MoneyIsTime.getData().writeTime(event.getPlayer().getUniqueId(), previousTime);
		double payOut = (MoneyIsTime.getMainConfig().getPayOut() * seconds)/MoneyIsTime.getMainConfig().getPayOutInterval();
		event.getPlayer().sendMessage(Text.color("&7[&6Syndicraft&7] &aYou checked out &6$" + payOut + "&a!"));
		Eco.pay(event.getPlayer(), payOut);
		MoneyHandler.playerCheckIn(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onButtonInteract(PlayerInteractEvent event) {
		List<Material> buttons = List.of(new Material[]{
				Material.STONE_BUTTON,
				Material.OAK_BUTTON,
				Material.SPRUCE_BUTTON,
				Material.BIRCH_BUTTON,
				Material.JUNGLE_BUTTON,
				Material.ACACIA_BUTTON,
				Material.DARK_OAK_BUTTON,
				Material.CRIMSON_BUTTON,
				Material.WARPED_BUTTON
		});
		if (event.getClickedBlock() == null) return;
		Player player = event.getPlayer();
		player.sendMessage("Block not null");
		if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
		player.sendMessage("Block right-clicked");
		if (!buttons.contains(event.getClickedBlock().getType())) return;
		player.sendMessage("Block is button");
		if (!event.getPlayer().hasPermission("syndicraft.moneyistime.checkout")) return;
		player.sendMessage("Has permission");
		if (!MoneyIsTime.getData().isAcceptableButton(event.getClickedBlock().getLocation())) return;
		player.sendMessage("Is acceptable button");
		MoneyHandler.playerCheckOut(event.getPlayer());
		long time = MoneyIsTime.getData().getTime(event.getPlayer().getUniqueId());
		if (time <= 999) {
			event.getPlayer().sendMessage(Text.color("&7[&6Syndicraft&7] &cYou have no money to check out yet."));
			return;
		}
		long previousTime = time % 1000;
		long seconds = (time/1000);
		MoneyIsTime.getData().writeTime(event.getPlayer().getUniqueId(), previousTime);
		double payOut = (MoneyIsTime.getMainConfig().getPayOut() * seconds)/MoneyIsTime.getMainConfig().getPayOutInterval();
		event.getPlayer().sendMessage(Text.color("&7[&6Syndicraft&7] &aYou checked out &6$" + payOut + "&a!"));
		Eco.pay(event.getPlayer(), payOut);
		MoneyHandler.playerCheckIn(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onSignBreak(BlockBreakEvent event) {
		if (event.isCancelled()) return;
		if (!MoneyIsTime.getData().isSign(event.getBlock().getLocation())) return;
		MoneyIsTime.getData().deleteSign(MoneyIsTime.getData().getSign(event.getBlock().getLocation()));
	}

	/*
	@EventHandler(priority = EventPriority.MONITOR)
	public void onSignPlace(BlockPlaceEvent event) {
		System.out.println("Sign placed");
		List<Material> signs = List.of(new Material[]{
				Material.OAK_WALL_SIGN,
				Material.SPRUCE_WALL_SIGN,
				Material.BIRCH_WALL_SIGN,
				Material.JUNGLE_WALL_SIGN,
				Material.ACACIA_WALL_SIGN,
				Material.DARK_OAK_WALL_SIGN,
				Material.CRIMSON_WALL_SIGN,
				Material.WARPED_WALL_SIGN
		});
		if (event.isCancelled()) return;
		System.out.println("Sign not cancelled");
		if (!event.getPlayer().hasPermission("syndicraft.moneyistime.placeatm")) return;
		System.out.println("Has permission");
		if (MoneyIsTime.getData().isSign(event.getBlock().getLocation())) return;
		System.out.println("Not a sign");
		event.getPlayer().sendMessage(event.getBlock().toString());
		if (!signs.contains(event.getBlock().getType())) return;
		System.out.println("Sign type");
		BlockState blockState = event.getBlock().getState();
		BlockData blockData = event.getBlock().getBlockData();
		if (!(blockState instanceof org.bukkit.block.Sign sign)) return;
		System.out.println("Sign state");
		event.getPlayer().sendMessage(sign.getSide(Side.FRONT).line(0).toString());
		event.getPlayer().sendMessage(Text.plainText(sign.getSide(Side.FRONT).line(0)));
		if (!Text.plainText(sign.getSide(Side.FRONT).line(0)).equalsIgnoreCase("[MoneyIsTime]")) return;
		System.out.println("Sign text");
		SignSide signSide = sign.getSide(Side.FRONT);
		signSide.line(0, Component.empty());
		signSide.line(1, Text.color("&6[ATM]"));
		signSide.line(2, Component.empty());
		signSide.line(3, Component.empty());

		MoneyIsTime.getData().addSign(new Sign(event.getBlock().getLocation()));
	}
	*/
	@EventHandler(priority = EventPriority.MONITOR)
	public void signFinishedEvent(SignChangeEvent event) {
		if (!event.getPlayer().hasPermission("syndicraft.moneyistime.placeatm")) return;
		if (!event.getLine(0).equalsIgnoreCase("[MoneyIsTime]")) return;
		if(MoneyIsTime.getData().isSign(event.getBlock().getLocation())) return;
		event.setLine(0, "");
		event.setLine(1, color("&6[ATM]"));
		event.setLine(2, "");
		event.setLine(3, "");
		MoneyIsTime.getData().addSign(new Sign(event.getBlock().getLocation()));
	}

	private static String color(String message) {
		return color(message, "&", "#", "");
	}

	private static String color(String message, String colorChar) {
		return color(message, colorChar, "#", "");
	}

	private static String color(String message, String startTag, String endTag) {
		return color(message, "&", startTag, endTag);
	}

	private static String color(String message, String colorChar, String startTag, String endTag) {
		message = Pattern
				.compile(colorChar + "([A-Fa-fK-Ok-oRr0-9])")
				.matcher(message)
				.replaceAll("\u00A7$1");

		StringBuilder translatedColor = new StringBuilder();
		Pattern pattern = Pattern.compile(startTag + "([a-fA-F0-9]{6})" + endTag);
		for (Matcher matcher = pattern.matcher(message); matcher.find(); matcher = pattern.matcher(message)) {
			String match = matcher.group(1);
			translatedColor.append("\u00A7x");
			for (char c : match.toCharArray()) translatedColor.append("\u00A7").append(c);
			message = message.substring(0, matcher.start()) + translatedColor + message.substring(matcher.end());
			translatedColor.delete(0, translatedColor.length());
		}
		return message;
	}
}
