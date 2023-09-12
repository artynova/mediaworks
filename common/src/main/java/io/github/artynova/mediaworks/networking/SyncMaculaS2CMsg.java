package io.github.artynova.mediaworks.networking;

import dev.architectury.networking.NetworkManager;
import io.github.artynova.mediaworks.client.macula.MaculaClient;
import io.github.artynova.mediaworks.logic.macula.Macula;
import io.github.artynova.mediaworks.logic.macula.MaculaContent;
import io.github.artynova.mediaworks.logic.macula.MaculaSerializer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class SyncMaculaS2CMsg extends NbtCompoundMsg {
    public SyncMaculaS2CMsg(Macula macula) {
        super(wrapMacula(macula));
    }

    private static NbtCompound wrapMacula(Macula macula) {
        NbtCompound compound = new NbtCompound();
        macula.writeToNbt(compound);
        return compound;
    }

    public SyncMaculaS2CMsg(PacketByteBuf buf) {
        super(buf);
    }

    @Override
    public void apply(Supplier<NetworkManager.PacketContext> contextSupplier) {
        contextSupplier.get().queue(() -> MaculaClient.syncFromServer(compound));
    }
}
