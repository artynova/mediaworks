package io.github.artynova.mediaworks.networking;

import dev.architectury.networking.NetworkManager;
import io.github.artynova.mediaworks.projection.AstralPosition;
import io.github.artynova.mediaworks.projection.AstralProjectionServer;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.function.Supplier;

public class AstralPositionSyncC2SMsg extends AstralPositionMsg {
    public AstralPositionSyncC2SMsg(AstralPosition data) {
        super(data);
    }

    public AstralPositionSyncC2SMsg(PacketByteBuf buf) {
        super(buf);
    }

    @Override
    public void apply(Supplier<NetworkManager.PacketContext> contextSupplier) {
        contextSupplier.get().queue(() -> AstralProjectionServer.syncFromClient((ServerPlayerEntity) contextSupplier.get().getPlayer(), data));
    }
}
