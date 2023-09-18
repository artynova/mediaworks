package io.github.artynova.mediaworks.casting.iota;

import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.api.spell.iota.IotaType;
import at.petrak.hexcasting.api.utils.HexUtils;
import io.github.artynova.mediaworks.logic.macula.Visage;
import io.github.artynova.mediaworks.logic.macula.VisageSerializer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

public class VisageIota extends Iota {
    public static IotaType<VisageIota> TYPE = new IotaType<>() {
        @Override
        public VisageIota deserialize(NbtElement tag, ServerWorld world) throws IllegalArgumentException {
            return VisageIota.deserialize(tag);
        }

        @Override
        public Text display(NbtElement tag) {
            return VisageIota.deserialize(tag).display();
        }

        @Override
        public int color() {
            return 0xFF_FFAA00;
        }
    };

    public VisageIota(Visage visage) {
        super(TYPE, visage);
    }

    public static VisageIota deserialize(NbtElement element) throws IllegalArgumentException {
        NbtCompound compound = HexUtils.downcast(element, NbtCompound.TYPE);
        return new VisageIota(VisageSerializer.deserializeVisage(compound));
    }

    public Text display() {
        return getVisage().displayOnStack().formatted(Formatting.GOLD);
    }

    public Visage getVisage() {
        return (Visage) this.payload;
    }

    @Override
    public boolean isTruthy() {
        return true;
    }

    @Override
    public boolean toleratesOther(Iota that) {
        return typesMatch(this, that) && that instanceof VisageIota visageIota && this.getVisage().equals(visageIota.getVisage());
    }

    @Override
    public @NotNull NbtElement serialize() {
        return VisageSerializer.serializeVisage(getVisage());
    }
}
