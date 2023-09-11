package io.github.artynova.mediaworks.fabric.cc;

import dev.onyxstudios.cca.api.v3.component.Component;
import io.github.artynova.mediaworks.macula.Macula;
import io.github.artynova.mediaworks.macula.MaculaSerializer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class MaculaComp implements Component {
    private final ServerPlayerEntity owner;
    private Macula macula = new Macula();

    public MaculaComp(ServerPlayerEntity owner) {
        this.owner = owner;
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag) {
        macula = MaculaSerializer.getMacula(tag, owner.getWorld());
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag) {
        MaculaSerializer.putMacula(tag, macula, owner.getWorld());
    }

    public Macula getMacula() {
        return macula;
    }

    public void setMacula(Macula macula) {
        this.macula = macula;
    }
}
