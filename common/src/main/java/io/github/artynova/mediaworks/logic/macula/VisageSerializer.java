package io.github.artynova.mediaworks.logic.macula;

import io.github.artynova.mediaworks.registry.MediaworksRegistries;
import io.github.artynova.mediaworks.util.NbtHelpers;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtLong;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VisageSerializer {
    // visage tags
    public static final String VISAGE_TYPE_TAG = "type";
    public static final String VISAGE_DATA_TAG = "data";
    // entry tags
    public static final String INSTANCE_TAG = "instance";
    public static final String ORIGIN_TAG = "origin";
    public static final String END_TIME_TAG = "end_time";

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

    public static <T extends Visage> NbtCompound serializeVisage(@NotNull T visage) {
        VisageType<T> type = (VisageType<T>) visage.getType();
        Identifier typeId = MediaworksRegistries.getVisageTypeId(type);
        if (typeId == null) {
            throw new IllegalArgumentException("Trying to serialize a visage of an unregistered type: " + type.getClass().getTypeName());
        }
        NbtCompound compound = new NbtCompound();
        compound.put(VISAGE_TYPE_TAG, NbtString.of(typeId.toString()));
        compound.put(VISAGE_DATA_TAG, type.serializeData(visage));
        return compound;
    }

    /**
     * @return Data for a {@link Visage} to be rendered, or a "garbage" if a drawable visage could not be parsed (e.g. it is invalid).
     */
    public static @NotNull Visage deserializeVisage(@NotNull NbtCompound compound) {
        VisageType<?> type = parseTypeFromTag(compound);
        if (type == null) return Visage.makeGarbageVisage(); // if stored id does not correspond to a valid type
        if (!compound.contains(VISAGE_DATA_TAG)) return Visage.makeGarbageVisage();
        Visage visage = type.deserializeData(compound.getCompound(VISAGE_DATA_TAG));
        return visage == null ? Visage.makeGarbageVisage() : visage;
    }

    public static NbtCompound serializeEntry(@NotNull VisageEntry entry) {
        NbtCompound compound = new NbtCompound();
        compound.put(INSTANCE_TAG, serializeVisage(entry.getVisage()));
        compound.put(ORIGIN_TAG, NbtHelpers.serializeVec3i(entry.getOrigin()));
        compound.put(END_TIME_TAG, NbtLong.of(entry.getEndTime()));
        return compound;
    }

    /**
     * @return Data for a {@link Visage} to be rendered, or null if a drawable visage could not be parsed (e.g. it is invalid or has timed out).
     */
    public static VisageEntry deserializeEntry(@NotNull NbtCompound compound) {
        Visage visage = deserializeVisage(compound.getCompound(INSTANCE_TAG));
        Vec3i origin = NbtHelpers.deserializeVec3i(compound.getList(ORIGIN_TAG, NbtElement.INT_TYPE));
        long endTime = compound.getLong(END_TIME_TAG);
        return new VisageEntry(visage, origin, endTime);
    }
}
