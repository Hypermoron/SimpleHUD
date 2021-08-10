package com.scrappymc.simplehud;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Display {

    public static void enable(JavaPlugin plugin) {
        Boolean coords = plugin.getConfig().getBoolean("coords.enabled");
        Boolean time = plugin.getConfig().getBoolean("time.enabled");

        if (coords | time) {
            Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                for (World world : Bukkit.getServer().getWorlds()) {

                    // Skips all players in the world if it is blacklisted/not whitelisted
                    if (plugin.getConfig().getBoolean("worlds.limit-worlds")
                            && (plugin.getConfig().getStringList("worlds.whitelist").contains(world.getName())
                                ^ plugin.getConfig().getBoolean("worlds.blacklist-mode"))) {
                        continue;
                    }

                    for (Player player : world.getPlayers()) {
                        boolean canSeeCoords = false;
                        boolean canSeeTime = false;

                        if (coords) {
                            if (player.hasPermission("hud.coords.override")) {
                                canSeeCoords = true;
                            } else if (player.hasPermission("hud.coords")) {
                                if (plugin.getConfig().getBoolean("coords.require-compass-in-hand")) {
                                    if (player.getInventory().getItemInMainHand().getType().equals(Material.COMPASS) ||
                                            player.getInventory().getItemInOffHand().getType().equals(Material.COMPASS)) {
                                        canSeeCoords = true;
                                    }
                                } else if (plugin.getConfig().getBoolean("coords.require-compass-in-inv")) {
                                    if (player.getInventory().contains(Material.COMPASS)) {
                                        canSeeCoords = true;
                                    }
                                } else {
                                    canSeeCoords = true;
                                }
                            }
                        }

                        if (time) {
                            if (player.hasPermission("hud.time.override")) {
                                canSeeTime = true;
                            } else if (player.hasPermission("hud.time")) {
                                if (plugin.getConfig().getBoolean("time.require-clock-in-hand")) {
                                    if (player.getInventory().getItemInMainHand().getType().equals(Material.CLOCK) ||
                                            player.getInventory().getItemInOffHand().getType().equals(Material.CLOCK)) {
                                        canSeeTime = true;
                                    }
                                } else if (plugin.getConfig().getBoolean("time.require-clock-in-inv")) {
                                    if (player.getInventory().contains(Material.CLOCK)) {
                                        canSeeTime = true;
                                    }
                                } else {
                                    canSeeTime = true;
                                }
                            }
                        }

                        Boolean intercardinals = plugin.getConfig().getBoolean("coords.intercardinal-directions");
                        ChatColor coordsLetterColor = ChatColor.valueOf(plugin.getConfig().getString("coords.letter-color"));
                        ChatColor coordsNumberColor = ChatColor.valueOf(plugin.getConfig().getString("coords.number-color"));
                        ChatColor timeColor = ChatColor.valueOf(plugin.getConfig().getString("time.color"));

                        if (canSeeTime && canSeeCoords) {
                            player.sendActionBar(Component.text(timeColor
                                    + timeText(world.getTime()) + "  "
                                    + coordsLetterColor + "XYZ: "
                                    + coordsNumberColor + locationText(player.getLocation()) + "  "
                                    + coordsLetterColor + directionText(player.getLocation().getYaw(), intercardinals)));
                        } else if (canSeeTime) {
                            player.sendActionBar(Component.text(timeColor + timeText(world.getTime())));
                        } else if (canSeeCoords) {
                            player.sendActionBar(Component.text(coordsLetterColor + "XYZ: "
                                    + coordsNumberColor + locationText(player.getLocation()) + "  "
                                    + coordsLetterColor + directionText(player.getLocation().getYaw(), intercardinals)));
                        }
                    }
                }
            }, 0L, plugin.getConfig().getInt("update-interval", 5));
        }
    }

    private static String timeText(long time) {
        long hours = time / 1000L + 6L;
        hours %= 24L;
        String hh = Long.toString(hours);
        if (hh.length() == 1) {
            hh = "0" + hh;
        }
        long minutes = time % 1000L * 60L / 1000L;
        String mm = Long.toString(minutes);
        if (mm.length() == 1) {
            mm = "0" + mm;
        }
        return hh + ":" + mm;
    }

    private static String locationText(Location location) {
        return location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ();
    }

    private static String directionText(float yaw, Boolean intercardinals) {
        if (yaw < 0F) {
            yaw += 360F;
        }

        if (intercardinals) {
            if (yaw > 22.5F && yaw <= 67.5F) {
                return "SW";
            } else if (yaw > 67.5F && yaw <= 112.5F) {
                return "W";
            } else if (yaw > 112.5F && yaw <= 157.5F) {
                return "NW";
            } else if (yaw > 157.5F && yaw <= 202.5F) {
                return "N";
            } else if (yaw > 202.5F && yaw <= 247.5F) {
                return "NE";
            } else if (yaw > 247.5F && yaw <= 292.5F) {
                return "E";
            } else if (yaw > 292.5F && yaw <= 337.5F) {
                return "SE";
            } else {
                return "S";
            }
        } else {
            if (yaw > 45F && yaw <= 135F) {
                return "W";
            } else if (yaw > 135F && yaw <= 225F) {
                return "N";
            } else if (yaw > 225F && yaw <= 315F) {
                return "E";
            } else {
                return "S";
            }
        }
    }

}
