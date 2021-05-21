package com.gmail.perhapsitisyeazz.rooomain.manager;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class TimeManager {

	public static String formattedTimeFromTick(long ticks) {
		long newTicks = (ticks + 6000L) * 72L;
		newTicks = newTicks / 20L;
		long mm = (newTicks / 60) % 60;
		long HH = (newTicks / (60 * 60)) % 24;
		return String.format("%02d:%02d", HH, mm);
	}

	public static Component getMCDayPart(long tick) {
		Component whichPart;
		if (tick >= 0L && tick < 1000L)
			whichPart = Component.text().content("☽").color(NamedTextColor.WHITE)
					.append(Component.text("/", NamedTextColor.GRAY),
							Component.text("☀", NamedTextColor.YELLOW)).build();
		else if (tick >= 1000L && tick < 10000L)
			whichPart = Component.text("☀", NamedTextColor.YELLOW);
		else if (tick >= 12000L && tick < 13000L)
			whichPart = Component.text().content("☀").color(NamedTextColor.YELLOW)
					.append(Component.text("/", NamedTextColor.GRAY),
							Component.text("☽", NamedTextColor.WHITE)).build();
		else
			whichPart = Component.text("☽", NamedTextColor.WHITE);
		return whichPart;
	}
}
