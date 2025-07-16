package ru.harimasa;

import com.google.gson.*;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Unique;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ColorManager {
    private static final Path CONFIG_PATH = Path.of("config/item_colors.json");
    private static final Map<Identifier, Boolean> COLORED_ITEMS = new HashMap<>();

    public static boolean isItemColored(Identifier itemId) {
        return COLORED_ITEMS.getOrDefault(itemId, false);
    }

    @Unique
    public static int getColor(int red, int green, int blue, int alpha) {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    public static void toggleItemColor(Identifier itemId) {
        COLORED_ITEMS.put(itemId, !COLORED_ITEMS.getOrDefault(itemId, false));
    }

    public static void saveColors() {
        try {
            Path configDir = CONFIG_PATH.getParent();
            if (!Files.exists(configDir)) Files.createDirectories(configDir);

            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonObject root = new JsonObject();

                JsonObject itemsObj = new JsonObject();
                COLORED_ITEMS.forEach((id, colored) -> itemsObj.addProperty(id.toString(), colored));
                root.add("items", itemsObj);

                gson.toJson(root, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadColors() {
        if (!Files.exists(CONFIG_PATH)) return;

        try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
            Gson gson = new Gson();
            JsonObject root = gson.fromJson(reader, JsonObject.class);

            if (root.has("items")) {
                JsonObject itemsObj = root.getAsJsonObject("items");
                COLORED_ITEMS.clear();
                itemsObj.entrySet().forEach(entry -> COLORED_ITEMS.put(Identifier.of(entry.getKey()), entry.getValue().getAsBoolean()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}