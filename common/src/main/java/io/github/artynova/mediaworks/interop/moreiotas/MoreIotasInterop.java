package io.github.artynova.mediaworks.interop.moreiotas;

import at.petrak.hexcasting.api.spell.iota.Iota;
import dev.architectury.platform.Platform;
import net.minecraft.text.Text;
import ram.talia.moreiotas.api.spell.iota.StringIota;

public class MoreIotasInterop {
    public static final String MOD_ID = "moreiotas";

    public static boolean isPresent() {
        return Platform.isModLoaded(MOD_ID);
    }

    /**
     * Only use this when MoreIotas is loaded and the iota is known to be a {@link StringIota}!
     *
     * @param iota an iota that is known to actually be a {@link StringIota}.
     * @return text representation with processed formatting codes.
     */
    public static Text captureStringIota(Iota iota) {
        if (!(iota instanceof StringIota stringIota)) return null;
        return Text.literal(stringIota.getString().replaceAll("&([1-9a-fk-orA-FK-OR])", "§$1"));
    }

    /**
     * Only use this when MoreIotas is loaded!
     *
     * @param iota any iota.
     * @return whether it is an instance of {@link StringIota}.
     */
    public static boolean isStringIota(Iota iota) {
        return iota instanceof StringIota;
    }
}
