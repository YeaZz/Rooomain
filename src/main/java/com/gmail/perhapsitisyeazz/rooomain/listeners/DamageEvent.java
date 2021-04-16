package com.gmail.perhapsitisyeazz.rooomain.listeners;

import com.gmail.perhapsitisyeazz.rooomain.Rooomain;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageEvent implements Listener {

	public Rooomain main;
	public DamageEvent(Rooomain main) {
		this.main = main;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void playerDamageEvent(EntityDamageByEntityEvent event) {
		Entity victim = event.getEntity();
		Entity attacker = event.getDamager();
		if (victim instanceof Player && attacker instanceof Player)
			event.setCancelled(true);
	}
}
