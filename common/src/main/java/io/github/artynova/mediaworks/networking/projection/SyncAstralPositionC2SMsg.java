package io.github.artynova.mediaworks.networking.projection;

import dev.architectury.networking.NetworkManager;
import io.github.artynova.mediaworks.logic.projection.AstralPosition;
import io.github.artynova.mediaworks.logic.projection.AstralProjectionServer;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.function.Supplier;

public class SyncAstralPositionC2SMsg extends AstralPositionMsg {
    public SyncAstralPositionC2SMsg(AstralPosition data) {
        super(data);
    }

    public SyncAstralPositionC2SMsg(PacketByteBuf buf) {
        super(buf);
    }

    @Override
    public void apply(Supplier<NetworkManager.PacketContext> contextSupplier) {
        contextSupplier.get().queue(() -> AstralProjectionServer.syncFromClient((ServerPlayerEntity) contextSupplier.get().getPlayer(), data));
    }
}
