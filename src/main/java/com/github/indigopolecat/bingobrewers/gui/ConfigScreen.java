package com.github.indigopolecat.bingobrewers.gui;

import com.github.indigopolecat.bingobrewers.BingoBrewers;
import com.github.indigopolecat.bingobrewers.BingoBrewersConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigManager;
import me.shedaniel.autoconfig.gui.ConfigScreenProvider;

public class ConfigScreen implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        //? if <26.1 {
        /*return parent -> AutoConfig.getConfigScreen(BingoBrewersConfig.class, parent).get();
        *///?} else {
        return parent -> new ConfigScreenProvider<>((ConfigManager<BingoBrewersConfig>) AutoConfig.getConfigHolder(BingoBrewersConfig.class), BingoBrewers.guiRegistry, parent).get();
        //?}
    }
}
