package com.gmail.perhapsitisyeazz.yeazzzsurvival.listeners;

import com.gmail.perhapsitisyeazz.yeazzzsurvival.YeazzzSurvival;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public record AntiTntEvent(YeazzzSurvival main) implements Listener {

    @EventHandler
    private void tntEvent(EntityExplodeEvent event) {
        if (event.getEntity().getType() == EntityType.PRIMED_TNT)
            event.setCancelled(true);
    }
}
