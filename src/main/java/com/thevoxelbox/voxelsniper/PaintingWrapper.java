package com.thevoxelbox.voxelsniper;

import org.bukkit.Art;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;

import java.util.Locale;

/**
 * Painting state change handler.
 *
 * @author Piotr
 */
public final class PaintingWrapper
{
    private static final Art[] ALL_ART = Art.values();

    private PaintingWrapper()
    {
    }


    /**
     * The paint method used to scroll or set a painting to a specific type.
     *
     * @param p
     *         The player executing the method
     * @param auto
     *         Scroll automatically? If false will use 'choice' to try and set the painting
     * @param back
     *         Scroll in reverse?
     * @param choice
     *         Chosen index to set the painting to
     */
    public static void paint(final Player p, final boolean auto, final boolean back, final Art choice)
    {


        Location targetLocation = p.getTargetBlock(null, 4).getLocation();
        Chunk paintingChunk = p.getTargetBlock(null, 4).getLocation().getChunk();

        Double bestDistanceMatch = 50D;
        Painting bestMatch = null;

        for (Entity entity : paintingChunk.getEntities())
        {
            if (entity.getType() == EntityType.PAINTING)
            {
                Double distance = targetLocation.distanceSquared(entity.getLocation());

                if (distance <= 4 && distance < bestDistanceMatch)
                {
                    bestDistanceMatch = distance;
                    bestMatch = (Painting) entity;
                }
            }
        }

        if (bestMatch != null)
        {
            if (auto)
            {
                Art bestMatchArt = bestMatch.getArt();
                int ordinal = bestMatchArt.ordinal() + (back ? -1 : 1) % ALL_ART.length;
                if (ordinal < 0) ordinal += ALL_ART.length;
                final Art art = ALL_ART[ordinal];

                bestMatch.setArt(art);
                p.sendMessage(ChatColor.GREEN + "Painting set to ID: " + (art.toString().toLowerCase(Locale.ENGLISH)));
            }
            else
            {
                Art art = choice;

                bestMatch.setArt(art);
                p.sendMessage(ChatColor.GREEN + "Painting set to ID: " + choice);
            }
        }

        //what if there is no match? well, then just nothing happens I guess :-)
    }
}
