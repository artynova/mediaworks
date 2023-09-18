package io.github.artynova.mediaworks.networking.macula;

import dev.architectury.networking.NetworkManager;
import io.github.artynova.mediaworks.client.macula.MaculaClient;
import io.github.artynova.mediaworks.logic.macula.Macula;
import io.github.artynova.mediaworks.networking.NbtCompoundMsg;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

import java.util.function.Supplier;

public class SyncMaculaContentS2CMsg extends NbtCompoundMsg {
    public SyncMaculaContentS2CMsg(Macula macula) {
        super(wrapMacula(macula));
    }

    public SyncMaculaContentS2CMsg(PacketByteBuf buf) {
        super(buf);
    }

    private static NbtCompound wrapMacula(Macula macula) {
        NbtCompound compound = new NbtCompound();
        macula.writeToNbt(compound);
        return compound;
    }

    @Override
    public void apply(Supplier<NetworkManager.PacketContext> contextSupplier) {
        contextSupplier.get().queue(() -> MaculaClient.syncFromServer(compound));
    }
}
