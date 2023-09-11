package io.github.artynova.mediaworks.networking.projection;

import dev.architectury.networking.NetworkManager;
import io.github.artynova.mediaworks.logic.projection.AstralProjectionServer;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.function.Supplier;

/**
 * Sent when the client right-clicks while in projection,
 * requesting to evaluate the captured ravenmind iota.
 * The iota itself is only stored on the server.
 */
public class CastAstralIotaC2SMsg {
    public CastAstralIotaC2SMsg() {
    }

    public CastAstralIotaC2SMsg(PacketByteBuf buf) {
    }

    public void encode(PacketByteBuf buf) {

    }

    public void apply(Supplier<NetworkManager.PacketContext> contextSupplier) {
        contextSupplier.get().queue(() -> AstralProjectionServer.evaluateIota((ServerPlayerEntity) contextSupplier.get().getPlayer()));
    }
}
