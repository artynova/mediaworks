package io.github.artynova.mediaworks.networking.macula;

import dev.architectury.networking.NetworkManager;
import io.github.artynova.mediaworks.logic.macula.MaculaServer;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.function.Supplier;

public class SyncMaculaDimensionsC2SMsg {
    private final int width;
    private final int height;

    public SyncMaculaDimensionsC2SMsg(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public SyncMaculaDimensionsC2SMsg(PacketByteBuf buf) {
        this(buf.readInt(), buf.readInt());
    }

    public void encode(PacketByteBuf buf) {
        buf.writeInt(width);
        buf.writeInt(height);
    }

    public void apply(Supplier<NetworkManager.PacketContext> contextSupplier) {
        contextSupplier.get().queue(() -> MaculaServer.syncDimensionsFromClient((ServerPlayerEntity) contextSupplier.get().getPlayer(), width, height));
    }
}
