package com.github.indigopolecat.bingobrewers.hud;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import lombok.*;

import static com.github.indigopolecat.bingobrewers.hud.HudManager.activeHuds;

public class TitleHud implements Hud, TimedHud {
    @Getter private final long startTime = System.currentTimeMillis();
    @Getter private final long displayTime;
    @Getter private final String title;
    public int color;
    private float scale = 1;

    // written out rather than generated with @AllArgsConstructor, as lombok would pull `scale` into the
    // generated constructor and silently change the signature every subclass calls
    public TitleHud(long displayTime, String title, int color) {
        this.displayTime = displayTime;
        this.title = title;
        this.color = color;
    }

    public TitleHud(TitleHud hud) {
        displayTime = hud.displayTime;
        title = hud.title;
        color = hud.color;
        scale = hud.scale;
    }

    public void setScale(float scale) {
        if(scale < 0.1 || scale > 30) throw new IllegalArgumentException("scale is <0.1 or >30");
        this.scale = scale;
    }

    public void render(GuiGraphicsExtractor graphics, DeltaTracker tickCounter) {
        // set alpha to 255 if not provided in color
        if((color & 0xFF000000) == 0) color |= 0xFF000000;
        
        Minecraft mc = Minecraft.getInstance();
        if(mc.level == null || mc.player == null) return;
        
        Window window = mc.getWindow();
        int width = window.getGuiScaledWidth();
        int height = window.getGuiScaledHeight();
        
        Font font = mc.font;

        graphics.pose().pushMatrix();
        graphics.pose().scale(scale);

        // positions are worked out in screen space and then divided by the scale, since the matrix multiplies them back up
        int x = (int) ((width - font.width(title) * scale) / 2 / scale);
        int y = (int) (((height / 2) - font.lineHeight * scale - 5) / scale);
        graphics.text(font, title, x, y, color, true);

        graphics.pose().popMatrix();
    }

    public static void addTitleHud(TitleHud hud) {
        // remove any other title huds when initializing
        for (Hud activeHud : activeHuds) {
            if (activeHud instanceof TitleHud) {
                activeHuds.remove(activeHud);
            }
        }

        HudManager.addNewHud(hud);
    }
}
