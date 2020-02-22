/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thevoxelbox.voxelsniper.brush.perform;

import com.thevoxelbox.voxelsniper.Message;
import com.thevoxelbox.voxelsniper.SnipeData;

import java.util.List;

/**
 * @author Voxel
 */
public interface Performer
{

    public void parse(String[] args, SnipeData snipeData);

    public void showInfo(Message vm);

    public default List<String> tabComplete(String[] args, SnipeData snipeData) {
        return null;
    }

}
