package io.github.artynova.mediaworks.networking.projection;

import dev.architectury.networking.NetworkManager;
import io.github.artynova.mediaworks.projection.AstralProjectionServer;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.function.Supplier;

// Request for early state termination, sent after pressing the hotkey.
public class EndProjectionC2SMsg {
    public EndProjectionC2SMsg() {
    }

    public EndProjectionC2SMsg(PacketByteBuf buf) {
    }

    public void encode(PacketByteBuf buf) {

    }

    public void apply(Supplier<NetworkManager.PacketContext> contextSupplier) {
        contextSupplier.get().queue(() -> AstralProjectionServer.endProjectionEarly((ServerPlayerEntity) contextSupplier.get().getPlayer()));
    }
}
