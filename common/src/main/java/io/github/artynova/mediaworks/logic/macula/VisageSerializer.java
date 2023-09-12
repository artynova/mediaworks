package io.github.artynova.mediaworks.logic.macula;

import io.github.artynova.mediaworks.registry.MediaworksRegistries;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtLong;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VisageSerializer {
    public static final String VISAGE_TYPE_TAG = "type";
    public static final String END_TIME_TAG = "end_time";
    public static final String VISAGE_DATA_TAG = "data";

    @Nullable
    public static VisageType<?> parseTypeFromTag(NbtCompound tag) {
        if (!tag.contains(VISAGE_TYPE_TAG, NbtElement.STRING_TYPE)) {
            return null;
        }
        String typeKey = tag.getString(VISAGE_TYPE_TAG);
        if (!Identifier.isValid(typeKey)) {
            return null;
        }
        Identifier typeId = new Identifier(typeKey);
        return MediaworksRegistries.getVisageType(typeId);
    }

    public static <T extends Visage> NbtCompound serialize(@NotNull T visage) {
        VisageType<T> type = (VisageType<T>) visage.getType();
        Identifier typeId = MediaworksRegistries.getVisageTypeId(type);
        if (typeId == null) {
            throw new IllegalArgumentException("Trying to serialize a visage of an unregistered type: " + type.getClass().getTypeName());
        }
        NbtCompound compound = new NbtCompound();
        compound.put(VISAGE_TYPE_TAG, NbtString.of(typeId.toString()));
        compound.put(END_TIME_TAG, NbtLong.of(visage.getEndTime()));
        compound.put(VISAGE_DATA_TAG, type.serializeData(visage));
        return compound;
    }

    /**
     * @return Data for a {@link Visage} to be rendered, or null if a drawable visage could not be parsed (e.g. it is invalid or has timed out).
     */
    public static @Nullable Visage deserialize(@NotNull NbtCompound compound, long currentTime) {
        VisageType<?> type = parseTypeFromTag(compound);
        if (type == null) return null; // if stored id does not correspond to a valid type
        if (!compound.contains(VISAGE_DATA_TAG)) return null;
        long endTime = -1;
        if (compound.contains(END_TIME_TAG)) endTime = compound.getLong(END_TIME_TAG);
        Visage visage = type.deserializeData(compound.getCompound(VISAGE_DATA_TAG));
        if (visage == null) return null; // if visage is invalid
        visage.setEndTime(endTime);
        if (visage.hasTimedOut(currentTime)) return null;
        return visage;
    }
}
