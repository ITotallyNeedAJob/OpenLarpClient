package com.larpclient.mixin;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({HandledScreen.class})
public interface HandledScreenHooksAccessor {
   @Accessor("field_2776")
   int larpLeftPos();

   @Accessor("field_2800")
   int larpTopPos();

   @Accessor("field_2792")
   int larpImageWidth();

   @Accessor("field_2779")
   int larpImageHeight();
}
