package io.github.artynova.mediaworks.networking.projection;

import dev.architectury.networking.NetworkManager;
import io.github.artynova.mediaworks.client.projection.AstralProjectionClient;
import net.minecraft.network.PacketByteBuf;

import java.util.function.Supplier;

// Proper server-authorized confirmation to remove the fake camera
public class EndProjectionS2CMsg {
    public EndProjectionS2CMsg() {
    }

    public EndProjectionS2CMsg(PacketByteBuf buf) {
    }

    public void encode(PacketByteBuf buf) {

    }

    public void apply(Supplier<NetworkManager.PacketContext> contextSupplier) {
        contextSupplier.get().queue(AstralProjectionClient::endProjection);
    }
}
