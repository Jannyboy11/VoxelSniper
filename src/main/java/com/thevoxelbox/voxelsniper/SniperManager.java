package com.thevoxelbox.voxelsniper;

import com.google.common.collect.Maps;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

/**
 *
 */
public class SniperManager
{
    private Map<UUID, Sniper> sniperInstances = Maps.newHashMap();
    private VoxelSniper plugin;

    public SniperManager(VoxelSniper plugin)
    {
        this.plugin = plugin;
    }

    public Sniper getSniperForPlayer(UUID player) {
        return sniperInstances.computeIfAbsent(player, uuid -> new Sniper(plugin, uuid));
    }

    public Sniper getSniperForPlayer(Player player)
    {
        return getSniperForPlayer(player.getUniqueId());
    }

    public boolean removeSniper(UUID player) {
        return sniperInstances.remove(player) != null;
    }

    public boolean removeSniper(Player player) {
        return removeSniper(player.getUniqueId());
    }

}
