package com.tehgamingcrew.bukkit.Marriage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.entity.CraftOcelot;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;

public class MarriageSneakListener implements Listener {
	
    public static Entity getTarget(final Player player) {
        assert player != null;
        Entity target = null;
        double targetDistanceSquared = 0;
        final double radiusSquared = 1;
        final Vector l = player.getEyeLocation().toVector(), n = player.getLocation().getDirection().normalize();
        final double cos45 = Math.cos(Math.PI / 4);
        for (final LivingEntity other : player.getWorld().getEntitiesByClass(LivingEntity.class)) {
            if (other == player)
                continue;
            if (target == null || targetDistanceSquared > other.getLocation().distanceSquared(player.getLocation())) {
                final Vector t = other.getLocation().add(0, 1, 0).toVector().subtract(l);
                if (n.clone().crossProduct(t).lengthSquared() < radiusSquared && t.normalize().dot(n) >= cos45) {
                    target = other;
                    targetDistanceSquared = target.getLocation().distanceSquared(player.getLocation());
                }
            }
        }
        return target;
    }
	
    //*Handles onPlayerSneak*
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerSneak(PlayerToggleSneakEvent event) {
		
		if (getTarget(event.getPlayer()) instanceof CraftPlayer) {
		Player player = event.getPlayer();
		Player target = (Player) getTarget(player);
		
		int px = (int) Math.round(player.getLocation().getX());
		int py = (int) Math.round(player.getLocation().getY());
		int pz = (int) Math.round(player.getLocation().getZ());
		
		int tx = (int) Math.round(target.getLocation().getX());
		int ty = (int) Math.round(target.getLocation().getY());
		int tz = (int) Math.round(target.getLocation().getZ());
		
		if (Math.abs(px-tx)>=4 && Math.abs(py-ty)>=4 && Math.abs(pz-tz)>=4) {
			return;
		}
		
		//if (Math.abs(px-tx)<=3 && Math.abs(py-ty)<=3 && Math.abs(pz-tz)<=3) {
		if (target.isSneaking() == true && player.isSneaking() == false && player.isFlying() == false && target.isFlying() == false && getTarget(target) == player) {
				Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + player.getName() + ChatColor.GREEN +" just kissed " + ChatColor.YELLOW + target.getName());
				
				//ocelot stuff
				CraftPlayer p = (CraftPlayer) player;
				CraftPlayer t = (CraftPlayer) target;
				
                CraftOcelot op = (CraftOcelot) player.getWorld().spawn(player.getLocation(), Ocelot.class);
                CraftOcelot ot = (CraftOcelot) target.getWorld().spawn(target.getLocation(), Ocelot.class);
                t.getHandle().world.broadcastEntityEffect(ot.getHandle(), (byte) 7);
                p.getHandle().world.broadcastEntityEffect(op.getHandle(), (byte) 7);
                op.remove();
                ot.remove();

				}
		}
	}
}
