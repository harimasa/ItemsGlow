package ru.harimasa.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import dev.isxander.yacl3.gui.controllers.ColorController;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.awt.*;

public class IGScreen {
    public static final ConfigClassHandler<IGConfig> CONFIG = ConfigClassHandler.createBuilder(IGConfig.class)
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("itemsglow.json"))
                    .build())
            .build();

    @SuppressWarnings("deprecation")
    public static Screen configScreen(Screen parent) {
        return YetAnotherConfigLib.create(CONFIG, ((defaults, config, builder) -> builder
                .title(Text.translatable("itemsglow.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("itemsglow.title"))
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("itemsglow.title"))
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.translatable("itemsglow.enable"))
                                        .description(OptionDescription.of(Text.translatable("itemsglow.enable")))
                                        .binding(defaults.enable, () -> config.enable, newVal -> config.enable = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.translatable("itemsglow.color"))
                                        .binding(defaults.color, () -> config.color, value -> config.color = value)
                                        .customController(opt -> new ColorController(opt, true))
                                        .build())
                                .build())
                        .build())
        )).generateScreen(parent);
    }
}