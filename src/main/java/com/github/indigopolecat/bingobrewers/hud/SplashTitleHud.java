package com.github.indigopolecat.bingobrewers.hud;

import com.github.indigopolecat.bingobrewers.BingoBrewersConfig;

public class SplashTitleHud extends TitleHud {
    public SplashTitleHud(String hub, boolean dungeonHub, boolean isPrivate) {
        super(1000L * BingoBrewersConfig.getConfig().splashConfig.alertDisplayTime, getMessage(hub, dungeonHub, isPrivate), BingoBrewersConfig.getConfig().alertTextColorHex);
    }

    private static String getMessage(String hub, boolean dungeonHub, boolean isPrivate) {
        String message = "Splash in Hub " + hub;
        if (isPrivate) {
            // first because if someone ever does a private dungeonhub splash it makes more sense to say private hub than dungeon hub
            message = "Splash in Private Hub (/p join " + hub + ")";
        } else if (dungeonHub) {
            message = "Splash in Dungeon Hub " + hub;
        }
        return message;
    }
}
