package com.abstractionalpha.minecraft.plugins.manhunt;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;

import static java.lang.Thread.sleep;

public class Listeners implements Listener {

    private ManHunt manHunt;

    public Listeners(ManHunt manHunt) {
        this.manHunt = manHunt;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        String playername = e.getPlayer().getName();

        if (manHunt.tracked() && !manHunt.verifySpeedrunner(playername)) {
            World world = e.getPlayer().getWorld();

            ItemStack compass = manHunt.getCompassItemStack(world, e.getPlayer());
            e.getPlayer().getInventory().addItem(compass);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        String playername = e.getEntity().getPlayer().getName();

        if (manHunt.verifySpeedrunner(playername)) {
            manHunt.setNull();
        } else {
            Iterator<ItemStack> drops = e.getDrops().iterator();
            while(drops.hasNext()) {
                ItemStack current = drops.next();
                if(current.getType() == Material.COMPASS) {
                    drops.remove();
                }
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        String playername = e.getPlayer().getName();

        if (manHunt.tracked() && !manHunt.verifySpeedrunner(playername)) {
            World world = e.getPlayer().getWorld();

            ItemStack compass = manHunt.getCompassItemStack(world, e.getPlayer());
            e.getPlayer().getInventory().addItem(compass);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        String playername = e.getPlayer().getName();

        if (manHunt.tracked()) {
            if (manHunt.verifySpeedrunner(playername)) {
                String worldname = e.getPlayer().getWorld().toString();

                int[] coords = new int[3];
                coords[0] = e.getPlayer().getLocation().getBlockX();
                coords[1] = e.getPlayer().getLocation().getBlockY();
                coords[2] = e.getPlayer().getLocation().getBlockZ();

                manHunt.setCoordinates(worldname, coords);
            }

            Object[] onlinePlayers = manHunt.getServer().getOnlinePlayers().toArray();

            for (Object player : onlinePlayers) {
                String hunterName = ((Player) player).getName();

                if (!manHunt.verifySpeedrunner(hunterName)) {
                    World world = ((Player) player).getWorld();

                    for (int i = 0; i < 36; i++) {
                        if (!(((Player) player).getInventory().getItem(i) == null) && !(((Player) player).getInventory().getItem(i) == new ItemStack(Material.AIR))) {
                            if (((Player) player).getInventory().getItem(i).getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Tracker")) {
                                ItemStack compass = manHunt.getCompassItemStack(world, e.getPlayer());
                                ((Player) player).getInventory().setItem(i, compass);
                            }
                        }
                    }
                }
            }
        }
    }
}
