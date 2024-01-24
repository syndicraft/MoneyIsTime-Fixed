package net.syndicraft.moneyistime.essentials;

import com.earth2me.essentials.User;
import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import net.ess3.api.IUser;
import net.ess3.api.MaxMoneyException;
import net.republicraft.rccore.api.Text;
import net.syndicraft.moneyistime.MoneyIsTime;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

public class Eco {
	
	public static void pay(Player player, double amount) {
		User user = MoneyIsTime.getEssentials().getUser(player);
		try {
			Economy.add(user, BigDecimal.valueOf(amount));
		} catch (NoLoanPermittedException e) {
			user.getBase().sendMessage(Text.color("&cPlease contact a server administrator immediately if you receive this issue!\n&6Error Code #SCBC-NLPE"));
		} catch (MaxMoneyException e) {
			user.getBase().sendMessage(Text.color("&cYou have reached the maximum amount of money! Please contact a server administrator."));
		}
	}
	
}
