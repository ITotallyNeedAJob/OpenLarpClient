package com.larpclient;

import net.minecraft.client.render.entity.state.PlayerEntityRenderState;

public interface ArmReach {
   void larpRestoreWalkRig();

   void larpApplyJoints(float[] var1, PlayerEntityRenderState var2);
}
