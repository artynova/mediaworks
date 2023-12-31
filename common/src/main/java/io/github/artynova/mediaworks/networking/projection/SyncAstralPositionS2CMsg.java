package io.github.artynova.mediaworks.networking.projection;

import dev.architectury.networking.NetworkManager;
import io.github.artynova.mediaworks.client.projection.AstralProjectionClient;
import io.github.artynova.mediaworks.logic.projection.AstralPosition;
import net.minecraft.network.PacketByteBuf;

import java.util.function.Supplier;

/**
 * If this message comes in while the client is not dissociated,
 * it is treated as a command to dissociate and supplies the initial camera location.
 */
public class SyncAstralPositionS2CMsg extends AstralPositionMsg {
    public SyncAstralPositionS2CMsg(AstralPosition data) {
        super(data);
    }

    public SyncAstralPositionS2CMsg(PacketByteBuf buf) {
        super(buf);
    }

    @Override
    public void apply(Supplier<NetworkManager.PacketContext> contextSupplier) {
        contextSupplier.get().queue(() -> AstralProjectionClient.syncFromServer(data));
    }
}
