package io.github.artynova.mediaworks.logic.media;

import at.petrak.hexcasting.api.spell.casting.CastingContext;
import at.petrak.hexcasting.api.spell.casting.CastingHarness;
import io.github.artynova.mediaworks.util.HexHelpers;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * The class that you should use to let
 */
public class MediaDiscoveryHandler {
    private static final List<Function<CastingHarness, @Nullable PackagedHexData>> PACKAGED_HEX_DISCOVERERS = new ArrayList<>();

    /**
     * Discoverers added via this method will be used to determine a custom "packaged hex" used by a cast with the
     * {@link CastingContext.CastSource#PACKAGED_HEX PACKAGED_HEX} source instead of the item in the right hand.
     * This will be used in {@link #collectCustomPackagedHex(CastingHarness)}.
     * <br/>
     * If a non-null value is returned, the hex holder is used to determine whether the packaged hex can access the inventory.
     * The media holder is used to determine internal available media, or can be {@code null} if not applicable.
     */
    public static void addCustomPackagedHexDiscoverer(Function<CastingHarness, @Nullable PackagedHexData> discoverer) {
        PACKAGED_HEX_DISCOVERERS.add(discoverer);
    }

    /**
     * Collects the custom packaged hex information for the harness, currently used for {@link HexHelpers#collectMediaHolders(List)}.
     * Return values are as such:
     * <ul>
     *     <li>{@code null} if the cast source is not a packaged hex</li>
     *     <li>The first non-null result from the {@link #PACKAGED_HEX_DISCOVERERS discoverers}, if any</li>
     *     <li>{@code null} if none of the discoverers found anything</li>
     * </ul>
     * If the method returns {@code null} when casting source is a packaged hex, standard fallback behaviour
     * of referring to the casting hand's stack is to be used.
     *
     * @param harness the casting harness
     * @return as specified by the list
     */
    public static @Nullable PackagedHexData collectCustomPackagedHex(CastingHarness harness) {
        CastingContext context = harness.getCtx();
        if (context.getSource() != CastingContext.CastSource.PACKAGED_HEX) return null;
        return PACKAGED_HEX_DISCOVERERS.stream().map(discoverer -> discoverer.apply(harness)).filter(Objects::nonNull).findFirst().orElse(null);
    }
}
