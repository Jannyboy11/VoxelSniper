package com.thevoxelbox.voxelsniper;

import com.google.common.base.Preconditions;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Configuration storage defining global configurations for VoxelSniper.
 */
public class VoxelSniperConfiguration
{
    public static final String CONFIG_IDENTIFIER_LITESNIPER_MAX_BRUSH_SIZE = "litesniper-max-brush-size";
    public static final String CONFIG_IDENTIFIER_UNDO_CACHE_SIZE = "undo-cache-size";
    public static final String CONFIG_IDENTIFIER_LITESNIPER_RESTRICTED_ITEMS = "litesniper-restricted-items";
    public static final String CONFIG_IDENTIFIER_MESSAGE_ON_LOGIN_ENABLED = "message-on-login-enabled";
    public static final int DEFAULT_LITESNIPER_MAX_BRUSH_SIZE = 5;
    public static final int DEFAULT_UNDO_CACHE_SIZE = 20;
    public static final boolean DEFAULT_MESSAGE_ON_LOGIN_ENABLED = true;
    private FileConfiguration configuration;

    /**
     * @param configuration Configuration that is going to be used.
     */
    public VoxelSniperConfiguration(FileConfiguration configuration)
    {
        this.configuration = configuration;
    }

    /**
     * Returns the maximum amount of snipes stored in the undo cache of snipers.
     *
     * @return the maximum amount of snipes stored in the undo cache of snipers
     */
    public int getUndoCacheSize()
    {
        return configuration.getInt(CONFIG_IDENTIFIER_UNDO_CACHE_SIZE, DEFAULT_UNDO_CACHE_SIZE);
    }

    /**
     * Set the maximum amount of snipes stored in the undo cache of snipers.
     *
     * @param size size of undo cache
     */
    public void setUndoCacheSize(int size)
    {
        configuration.set(CONFIG_IDENTIFIER_UNDO_CACHE_SIZE, size);
    }

    /**
     * Returns maximum size of brushes that LiteSnipers can use.
     *
     * @return maximum size
     */
    public int getLiteSniperMaxBrushSize()
    {
        return configuration.getInt(CONFIG_IDENTIFIER_LITESNIPER_MAX_BRUSH_SIZE, DEFAULT_LITESNIPER_MAX_BRUSH_SIZE);
    }

    /**
     * Set maximum size of brushes that LiteSnipers can use.
     *
     * @param size maximum size
     */
    public void setLiteSniperMaxBrushSize(int size)
    {
        configuration.set(CONFIG_IDENTIFIER_LITESNIPER_MAX_BRUSH_SIZE, size);
    }

    /**
     * Returns List of restricted Litesniper Items.
     *
     * @return List of restricted Litesniper Items
     */
    public Set<Material> getLiteSniperRestrictedItems()
    {
        return configuration.getStringList(CONFIG_IDENTIFIER_LITESNIPER_RESTRICTED_ITEMS).stream()
                .map(Material::matchMaterial)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(Material.class)));
    }

    /**
     * Set new list of restricted Litesniper Items.
     *
     * @param restrictedItems List of restricted Litesniper Items
     */
    public void setLiteSniperRestrictedItems(Set<Material> restrictedItems)
    {
        Preconditions.checkNotNull(restrictedItems, "Restricted items must be a list.");
        List<String> configValue = restrictedItems.stream().map(Material::toString).collect(Collectors.toList());
        configuration.set(CONFIG_IDENTIFIER_LITESNIPER_RESTRICTED_ITEMS, configValue);
    }

    /**
     * Returns if the login message is enabled.
     *
     * @return true if message on login is enabled, false otherwise.
     */
    public boolean isMessageOnLoginEnabled()
    {
        return configuration.getBoolean(CONFIG_IDENTIFIER_MESSAGE_ON_LOGIN_ENABLED, DEFAULT_MESSAGE_ON_LOGIN_ENABLED);
    }

    /**
     * Set the message on login to be enabled or disabled.
     *
     * @param enabled Message on Login enabled
     */
    public void setMessageOnLoginEnabled(boolean enabled)
    {
        configuration.set(CONFIG_IDENTIFIER_MESSAGE_ON_LOGIN_ENABLED, enabled);
    }
}
