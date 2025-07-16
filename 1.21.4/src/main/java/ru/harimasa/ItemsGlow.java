package ru.harimasa;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import ru.harimasa.config.IGScreen;
import ru.harimasa.mixin.HandledScreenAccessor;

public class ItemsGlow implements ClientModInitializer {
	private static boolean wasKeyPressed = false;

	@Override
	public void onInitializeClient() {
		IGScreen.CONFIG.load();
		ColorManager.loadColors();

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (!IGScreen.CONFIG.instance().enable) {
				wasKeyPressed = false;
				return;
			}
			if (client.currentScreen == null) {
				wasKeyPressed = false;
				return;
			}

			boolean ctrlPressed = InputUtil.isKeyPressed(client.getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_CONTROL);
			boolean altPressed = InputUtil.isKeyPressed(client.getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_ALT);

			if (ctrlPressed && altPressed) {
				if (!wasKeyPressed) wasKeyPressed = true;
				if (client.currentScreen instanceof HandledScreenAccessor accessor) {
					Slot hoveredSlot = accessor.getFocusedSlot();
					if (hoveredSlot != null) {
						ItemStack stack = hoveredSlot.getStack();
						if (!stack.isEmpty()) {
							Identifier itemId = Registries.ITEM.getId(stack.getItem());
							ColorManager.toggleItemColor(itemId);
							ColorManager.saveColors();
						}
					}
				}
			} else {
				wasKeyPressed = false;
			}
		});
	}
}