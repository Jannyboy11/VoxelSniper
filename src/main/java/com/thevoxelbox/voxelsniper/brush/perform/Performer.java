/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thevoxelbox.voxelsniper.brush.perform;

import com.thevoxelbox.voxelsniper.Message;
import com.thevoxelbox.voxelsniper.SnipeData;
import com.thevoxelbox.voxelsniper.brush.IBrush;

/**
 * @author Voxel
 */
public interface Performer extends IBrush
{

    public void parse(String[] args, SnipeData snipeData);

    public void showInfo(Message vm);

}
