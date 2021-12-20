package com.gmail.perhapsitisyeazz.yeazzzsurvival.listeners;

import com.gmail.perhapsitisyeazz.yeazzzsurvival.YeazzzSurvival;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.manager.PlayerDataManager;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.objects.PlayerData;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.utils.Mod;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record ModRelatedEvent(YeazzzSurvival main) implements Listener {

    @EventHandler
    private void damageEvent(EntityDamageByEntityEvent event) {
        Entity victimEntity = event.getEntity(), attackerEntity = event.getDamager();
        if (victimEntity instanceof Player victim) {
            PlayerData victimData = PlayerDataManager.getPlayerData(victim);
            if (attackerEntity instanceof Player attacker) {
                PlayerData attackerData = PlayerDataManager.getPlayerData(attacker);
                if (victimData.getMod() == Mod.PACIFIQUE || attackerData.getMod() == Mod.PACIFIQUE)
                    event.setCancelled(true);
            } else if (attackerEntity instanceof Projectile projectile && projectile.getShooter() instanceof Player attacker) {
                PlayerData attackerData = PlayerDataManager.getPlayerData(attacker);
                if (victimData.getMod() == Mod.PACIFIQUE || attackerData.getMod() == Mod.PACIFIQUE)
                    event.setCancelled(true);
            } else if (attackerEntity instanceof AreaEffectCloud cloud && cloud.getSource() instanceof Player attacker) {
                PlayerData attackerData = PlayerDataManager.getPlayerData(attacker);
                if (victimData.getMod() == Mod.PACIFIQUE || attackerData.getMod() == Mod.PACIFIQUE)
                    event.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void lingeringPotion(AreaEffectCloudApplyEvent event) {
        if (event.getEntity().getSource() instanceof Player player)
            runPotionCode(player, event.getAffectedEntities(), getDebuff(event.getEntity().getCustomEffects()));
    }

    @EventHandler
    private void splashPotion(PotionSplashEvent event) {
        if (event.getEntity().getShooter() instanceof Player player)
            runPotionCode(player, (List<LivingEntity>) event.getAffectedEntities(), getDebuff((List<PotionEffect>) event.getPotion().getEffects()));
    }

    private void runPotionCode(Player player, List<LivingEntity> affectedEntities, List<PotionEffectType> effects) {
        if (affectedEntities.isEmpty() || effects.isEmpty())
            return;
        List<Player> affectedPlayers = getPacificAffectedPlayers(player, affectedEntities);
        if (affectedPlayers.isEmpty())
            return;
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player affectedPlayer : affectedPlayers)
                    for (PotionEffectType effectType : effects)
                        affectedPlayer.removePotionEffect(effectType);
            }
        };
        runnable.runTaskLater(YeazzzSurvival.getInstance(), 1L);
    }

    private List<Player> getPacificAffectedPlayers(Player player, List<LivingEntity> affectedEntities) {
        List<Player> affectedPlayers = new ArrayList<>();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData.getMod() == Mod.PACIFIQUE)
            for (LivingEntity affectedEntity : affectedEntities) {
                if (affectedEntity instanceof Player affectedPlayer && affectedEntity != player)
                    affectedPlayers.add(affectedPlayer);
            }
        else
            for (LivingEntity affectedEntity : affectedEntities) {
                if (affectedEntity instanceof Player affectedPlayer && affectedEntity != player) {
                    PlayerData affectedPlayerData = PlayerDataManager.getPlayerData(affectedPlayer);
                    if (affectedPlayerData.getMod() == Mod.PACIFIQUE)
                        affectedPlayers.add(affectedPlayer);
                }
            }
        return affectedPlayers;
    }

    private List<PotionEffectType> getDebuff(List<PotionEffect> effects) {
        List<PotionEffectType> debuffList = Arrays.asList(PotionEffectType.SLOW, PotionEffectType.SLOW_DIGGING, PotionEffectType.HARM, PotionEffectType.CONFUSION, PotionEffectType.BLINDNESS, PotionEffectType.HUNGER, PotionEffectType.POISON, PotionEffectType.WITHER, PotionEffectType.WEAKNESS);
        List<PotionEffectType> returnedEffectsTypes = new ArrayList<>();
        for (PotionEffect effect : effects)
            if (debuffList.contains(effect.getType()))
                returnedEffectsTypes.add(effect.getType());
        return returnedEffectsTypes;
    }
}
