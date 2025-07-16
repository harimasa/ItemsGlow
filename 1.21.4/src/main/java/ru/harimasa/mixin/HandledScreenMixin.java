package ru.harimasa.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.harimasa.ColorManager;
import ru.harimasa.config.IGScreen;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin {
    @Inject(method = "drawSlot", at = @At("HEAD"))
    private void onDrawSlot(DrawContext context, Slot slot, CallbackInfo ci) {
        if (!IGScreen.CONFIG.instance().enable) return;
        ItemStack stack = slot.getStack();
        if (stack.isEmpty()) return;

        Identifier itemId = Registries.ITEM.getId(stack.getItem());
        if (ColorManager.isItemColored(itemId)) {
            int x = slot.x;
            int y = slot.y;
            int red = IGScreen.CONFIG.instance().color.getRed();
            int green = IGScreen.CONFIG.instance().color.getGreen();
            int blue = IGScreen.CONFIG.instance().color.getBlue();
            int alpha = IGScreen.CONFIG.instance().color.getAlpha();

            context.fill(x, y, x + 16, y + 16, ColorManager.getColor(red, green, blue, alpha));
        }
    }
}