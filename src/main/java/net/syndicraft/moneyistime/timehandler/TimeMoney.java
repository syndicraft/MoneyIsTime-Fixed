package net.syndicraft.moneyistime.timehandler;

public class TimeMoney {
	
	private final long startTime = System.currentTimeMillis();
	
	public long calculateTime() {
		return (System.currentTimeMillis() - startTime) / 1000;
	}
	
}
