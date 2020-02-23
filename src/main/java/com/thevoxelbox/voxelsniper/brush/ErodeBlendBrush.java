package com.thevoxelbox.voxelsniper.brush;

import com.thevoxelbox.voxelsniper.Message;
import com.thevoxelbox.voxelsniper.SnipeData;

public class ErodeBlendBrush extends Brush {

    private ErodeBrush erodeBrush = new ErodeBrush();
    private BlendBallBrush blendBallBrush = new BlendBallBrush();

    public ErodeBlendBrush() {
        this.setName("Erode BlendBall");
    }

    @Override
    public void arrow(SnipeData snipeData) {
        blendBallBrush.excludeAir = false;
        erodeBrush.setTargetBlock(getTargetBlock());
        blendBallBrush.setTargetBlock(getTargetBlock());
        erodeBrush.arrow(snipeData);
        blendBallBrush.arrow(snipeData);
    }

    @Override
    public void powder(SnipeData snipeData) {
        blendBallBrush.excludeAir = false;
        erodeBrush.setTargetBlock(getTargetBlock());
        blendBallBrush.setTargetBlock(getTargetBlock());
        erodeBrush.powder(snipeData);
        blendBallBrush.arrow(snipeData);
    }

    @Override
    public void info(Message vm) {
        //TODO do something better here...
        erodeBrush.info(vm);
        blendBallBrush.info(vm);
    }

    @Override
    public void parameters(String[] args, SnipeData snipeData) {
        //TODO this doesn't seem right
        erodeBrush.parameters(args, snipeData);
        blendBallBrush.parameters(args, snipeData);
    }

    @Override
    public String getPermissionNode() {
        return "voxelsniper.brush.erodeblend";
    }

}
