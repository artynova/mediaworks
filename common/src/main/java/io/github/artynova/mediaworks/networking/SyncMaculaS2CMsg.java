package io.github.artynova.mediaworks.networking;

import dev.architectury.networking.NetworkManager;
import io.github.artynova.mediaworks.client.macula.MaculaClient;
import io.github.artynova.mediaworks.macula.Macula;
import io.github.artynova.mediaworks.macula.MaculaSerializer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class SyncMaculaS2CMsg extends NbtCompoundMsg {
    public SyncMaculaS2CMsg(NbtList maculaList) {
        super(wrapMacula(maculaList));
    }

    public SyncMaculaS2CMsg(Macula macula, World world) {
        super(wrapMacula(macula, world));
    }

    private static NbtCompound wrapMacula(Macula macula, World world) {
        NbtCompound compound = new NbtCompound();
        MaculaSerializer.putMacula(compound, macula, world);
        return compound;
    }

    private static NbtCompound wrapMacula(NbtList maculaList) {
        NbtCompound compound = new NbtCompound();
        compound.put(MaculaSerializer.MACULA_TAG, maculaList);
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
