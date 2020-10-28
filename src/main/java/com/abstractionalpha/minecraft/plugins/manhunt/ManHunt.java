package com.abstractionalpha.minecraft.plugins.manhunt;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class ManHunt extends JavaPlugin {

    private SpeedRunner speedRunner;

    @Override
    public void onEnable() {
        this.speedRunner = getNull();
        getServer().getPluginManager().registerEvents(new Listeners(this), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("manhunt")) {
            if (args.length == 1) {
                this.speedRunner = new SpeedRunner(args[0], sender, this);
            } else {
                sender.sendMessage(ChatColor.RED + "[ManHunt] Usage: /manhunt <speedrunner>");
            }
        }

        return true;
    }

    public SpeedRunner getNull() {
        SpeedRunner s = new SpeedRunner();
        return s;
    }

    public void setNull() {
        this.speedRunner = getNull();
    }

    public int[] getCoordinates(String world) {
        if (world.contains("_nether")) {
            return speedRunner.getNetherCoords();
        } else if (world.contains("_the_end")) {
            return speedRunner.getEndCoords();
        } else {
            return speedRunner.getOverworldCoords();
        }
    }

    public void setCoordinates(String world, int[] coords) {
        if (world.contains("_nether")) {
            speedRunner.setNetherCoords(coords);
        } else if (world.contains("_the_end")) {
            speedRunner.setEndCoords(coords);
        } else {
            speedRunner.setOverworldCoords(coords);
        }
    }

    public boolean verifySpeedrunner(String playername) {
        if (speedRunner.getPlayername().equalsIgnoreCase(playername)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean tracked() {
        return speedRunner.getTracked();
    }

    public ItemStack getCompassItemStack(World world, Player player) {
        int[] coords = getCoordinates(world.toString());

        Location loc = player.getLocation();
        loc.setX(coords[0]);
        loc.setY(coords[1]);
        loc.setZ(coords[2]);
        loc.setWorld(world);

        ItemStack c = new ItemStack(Material.COMPASS);
        CompassMeta cm = (CompassMeta) c.getItemMeta();
        cm.setLodestoneTracked(false);
        cm.setLodestone(loc);
        cm.setDisplayName(ChatColor.GREEN + "Tracker");

        c.setItemMeta(cm);

        return c;
    }
}
