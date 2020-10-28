package com.abstractionalpha.minecraft.plugins.manhunt;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

public class SpeedRunner {

    private static int DIMENSIONS = 3;

    /** speed runner username */
    private String playername;

    /** speed runner being tracked or not */
    private boolean tracked = false;

    /** overworld coordinates for player */
    private int[] overworldCoords = new int[DIMENSIONS];

    /** nether coordiantes for player */
    private int netherCoords[] = new int[DIMENSIONS];

    /** end coordinates for player */
    private int endCoords[] = new int[DIMENSIONS];

    /**
     * Player constructor for the SpeedRunner object. Only constructor that can be accessed via commands. This constructor
     * allows for players to define a speed runner using the CommandExecutor commands.
     *
     * @param playername
     * @param manHunt
     * @param sender
     */
    public SpeedRunner(String playername, CommandSender sender, ManHunt manHunt) {
        try {
            this.playername = playername;

            Player speedrunner = manHunt.getServer().getPlayer(playername);

            Object[] online = manHunt.getServer().getOnlinePlayers().toArray();

            if (online.length == 1) {
                throw new NullPointerException();
            }

            this.tracked = true;

            int[] coords = manHunt.getCoordinates(speedrunner.getWorld().toString());

            ItemStack c = new ItemStack(Material.COMPASS);
            CompassMeta cm = (CompassMeta) c.getItemMeta();
            cm.setLodestoneTracked(false);
            cm.setDisplayName(ChatColor.GREEN + "Tracker");

            for (Object player : online) {
                if (!((Player) player).getName().equals(playername)) {
                    World world = ((Player) player).getWorld();
                    Location loc = speedrunner.getLocation();
                    loc.setX(coords[0]);
                    loc.setY(coords[1]);
                    loc.setZ(coords[2]);
                    loc.setWorld(world);
                    cm.setLodestone(loc);

                    c.setItemMeta(cm);

                    ((Player) player).getInventory().addItem(c);
                }
            }

        } catch (NullPointerException e) {
            if (sender instanceof Player) {
                sender.sendMessage(ChatColor.RED + "[ManHunt] Player " + playername + " not found online.");
            } else {
                manHunt.getLogger().info(ChatColor.RED + "[ManHunt] Player " + playername + " not found online.");
            }
        }
    }

    /**
     * Default constructor for SpeedRunner object. This constructor is used as a hard reset in case the speed runner
     * dies or disconnects for more than one minute. Also called on startup so that the object is always there.
     */
    public SpeedRunner() {
        this.playername = null;
        this.tracked = false;
    }

    /**
     * Returns the most up-to-date overworld coordinates of the speedrunner.
     *
     * @return speedrunner overworld coordinates
     */
    public int[] getOverworldCoords() {
        return this.overworldCoords;
    }

    /**
     * Returns the most up-to-date nether coordinates of the speedrunner.
     *
     * @return speedrunner nether coordinates
     */
    public int[] getNetherCoords() {
        return this.netherCoords;
    }

    /**
     * Returns the most up-to-date end coordinates of the speedrunner.
     *
     * @return speedrunner end coordinates
     */
    public int[] getEndCoords() {
        return this.endCoords;
    }

    /**
     * Update overworld coordinate field.
     *
     * @param coords
     */
    public void setOverworldCoords(int[] coords) {
        for (int i = 0; i < DIMENSIONS; i++) {
            this.overworldCoords[i] = coords[i];
        }
    }

    /**
     * Update nether coordinate field.
     *
     * @param coords
     */
    public void setNetherCoords(int[] coords) {
        for (int i = 0; i < DIMENSIONS; i++) {
            this.netherCoords[i] = coords[i];
        }
    }

    /**
     * Update end coordinate field.
     *
     * @param coords
     */
    public void setEndCoords(int[] coords) {
        for (int i = 0; i < DIMENSIONS; i++) {
            this.endCoords[i] = coords[i];
        }
    }

    /**
     *
     */
    public String getPlayername() {
        return this.playername;
    }

    public boolean getTracked() {
        return this.tracked;
    }

}
