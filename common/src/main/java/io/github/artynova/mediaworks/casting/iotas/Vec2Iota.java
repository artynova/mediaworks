package io.github.artynova.mediaworks.casting.iotas;

import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.api.spell.iota.IotaType;
import at.petrak.hexcasting.api.utils.HexUtils;
import at.petrak.hexcasting.api.utils.NBTHelper;
import io.github.artynova.mediaworks.util.NbtHelpers;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec2f;
import org.jetbrains.annotations.NotNull;

public class Vec2Iota extends Iota {
    public static IotaType<Vec2Iota> TYPE = new IotaType<>() {
        @Override
        public Vec2Iota deserialize(NbtElement tag, ServerWorld world) throws IllegalArgumentException {
            return Vec2Iota.deserialize(tag);
        }

        @Override
        public Text display(NbtElement tag) {
            return Vec2Iota.deserialize(tag).display();
        }

        @Override
        public int color() {
            return 0xFF_FF3030;
        }
    };

    public Vec2Iota(Vec2f vector) {
        super(TYPE, vector);
    }

    public static Vec2Iota deserialize(NbtElement tag) throws IllegalArgumentException {
        NbtList list = HexUtils.downcast(tag, NbtList.TYPE);
        return new Vec2Iota(NbtHelpers.deserializeVec2(list));
    }

    public Text display() {
        Vec2f vec = getVec2();
        return Text.literal(String.format("(%.2f, %.2f)", vec.x, vec.y)).formatted(Formatting.RED);
    }

    public Vec2f getVec2() {
        return (Vec2f) this.payload;
    }

    @Override
    public boolean isTruthy() {
        Vec2f vec = getVec2();
        return vec.x != 0.0 && vec.y != 0.0;
    }

    @Override
    public boolean toleratesOther(Iota that) {
        return typesMatch(this, that) && that instanceof Vec2Iota vec2Iota && this.getVec2().equals(vec2Iota.getVec2());
    }

    @Override
    public @NotNull NbtElement serialize() {
        return NbtHelpers.serializeVec2(getVec2());
    }
}
