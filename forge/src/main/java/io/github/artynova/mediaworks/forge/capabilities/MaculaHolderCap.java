package io.github.artynova.mediaworks.forge.capabilities;

import io.github.artynova.mediaworks.api.logic.macula.MaculaHolder;
import io.github.artynova.mediaworks.logic.macula.Macula;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class MaculaHolderCap implements MaculaHolder {
    private final Macula base;

    public MaculaHolderCap(ServerPlayerEntity owner) {
        this.base = new Macula(owner);
    }

    @Override
    public @NotNull Macula unwrap() {
        return base;
    }
}
