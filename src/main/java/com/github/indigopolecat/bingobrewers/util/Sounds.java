package com.github.indigopolecat.bingobrewers.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

/**
 * The sounds bundled in assets/bingobrewers/sounds.json
 */
public class Sounds {
    // Deliberately not added to the SOUND_EVENT registry: SoundManager resolves a SoundInstance against the
    // client's asset sound registry (loaded from sounds.json), so an unregistered event is enough to play a
    // bundled ogg without adding an entry to a registry that gets synced with the server.
    public static final SoundEvent SPLASH_NOTIFICATION = SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("bingobrewers", "splash_notification"));

    /**
     * Plays a sound as an unattenuated UI sound, i.e. at a constant volume regardless of where the player is.
     *
     * @param sound the {@link SoundEvent} to play
     * @param volume the volume to play at, where 1 is the sound's original volume. Nothing is played at 0 or below
     */
    public static void playUI(SoundEvent sound, float volume) {
        if(volume <= 0) return;

        Minecraft mc = Minecraft.getInstance();
        // splash notifications arrive on the network thread, the sound engine may only be touched on the client thread
        mc.execute(() -> mc.getSoundManager().play(SimpleSoundInstance.forUI(sound, 1.0F, volume)));
    }
}
