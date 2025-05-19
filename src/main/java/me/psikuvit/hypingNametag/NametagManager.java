package me.psikuvit.hypingNametag;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Objects;

public class NametagManager {

    private final HypingNametag plugin;

    public NametagManager(HypingNametag plugin) {
        this.plugin = plugin;
    }

    public void initTag(@NotNull Player player) {
        removeNametag(player);
        player.getScheduler().execute(plugin, () -> {
            Component nameComponent = player.name();

            Location loc = player.getLocation();

            TextDisplay nametag = player.getWorld().spawn(loc, TextDisplay.class, textDisplay -> {
                textDisplay.text(nameComponent);
                textDisplay.setBillboard(Display.Billboard.CENTER);
                textDisplay.setShadowed(true);
                textDisplay.setSeeThrough(true);
                textDisplay.setViewRange(64.0f);
                textDisplay.setInterpolationDelay(1);
                textDisplay.setInterpolationDuration(5);
                textDisplay.addScoreboardTag("nametag_" + player.getUniqueId());
                textDisplay.setAlignment(TextDisplay.TextAlignment.CENTER);

                Transformation transformation = new Transformation(
                        new Vector3f(0, 0.3f, 0),
                        new Quaternionf(),
                        new Vector3f(1, 1, 1),
                        new Quaternionf()
                );
                textDisplay.setTransformation(transformation);
            });
            player.addPassenger(nametag);
        }, () -> plugin.getLogger().info("Failed to spawn nametag for " + player.getName()), 1L);

    }

    public void updateNametag(Player player, Component name) {
        try {
            TextDisplay nametag = null;
            for (Entity entity : player.getPassengers()) {
                if (entity instanceof TextDisplay display) nametag = display;
            }
            Objects.requireNonNull(nametag).text(name);
        } catch (NullPointerException e) {
            plugin.getLogger().info("Failed to update nametag for " + player.getName() + ": " + e.getMessage());
        }
    }

    public void removeNametag(@NotNull Player player) {
        player.getScheduler().execute(plugin, () -> player.getWorld().getEntitiesByClass(TextDisplay.class).stream()
                .filter(display -> display.getScoreboardTags().contains("nametag_" + player.getUniqueId()))
                .forEach(Display::remove), () -> plugin.getLogger().info("Failed to remove nametag for " + player.getName()), 1L);
    }
}
