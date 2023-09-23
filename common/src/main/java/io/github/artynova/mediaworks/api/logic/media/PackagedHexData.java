package io.github.artynova.mediaworks.api.logic.media;

import at.petrak.hexcasting.api.addldata.ADHexHolder;
import at.petrak.hexcasting.api.addldata.ADMediaHolder;
import org.jetbrains.annotations.Nullable;

/**
 * Stores information about a "packaged hex" item.
 *
 * @param hexHolder   the hex holder, always present.
 * @param mediaHolder the media holder, not necessarily present.
 */
public record PackagedHexData(ADHexHolder hexHolder, @Nullable ADMediaHolder mediaHolder) {
}

