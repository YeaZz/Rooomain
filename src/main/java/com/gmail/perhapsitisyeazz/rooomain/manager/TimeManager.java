package com.gmail.perhapsitisyeazz.rooomain.manager;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class TimeManager {

	public static Component formattedTimeFromTick(long ticks) {
		long newTicks = (ticks + 6000L) * 72L;
		newTicks = newTicks / 20L;
		long ss = newTicks % 60;
		long mm = (newTicks / 60) % 60;
		long HH = (newTicks / (60 * 60)) % 24;
		return Component.text().color(NamedTextColor.GRAY)
				.append(Component.text(" - ", NamedTextColor.DARK_AQUA),
						Component.text(String.format("%02d:%02d:%02d", HH, mm, ss), NamedTextColor.AQUA),
						Component.space(),
						getMCDayPart(ticks)).build();
	}

	private static Component getMCDayPart(long tick) {
		Component whichPart = Component.text("☽", NamedTextColor.WHITE);
		if (tick >= 1000L && tick < 12000L) whichPart = Component.text("☀", NamedTextColor.YELLOW);
		return whichPart;
	}
}
