package io.github.artynova.mediaworks.client.projection.camera;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.Packet;

@Environment(EnvType.CLIENT)
public class FakeClientPlayNetworkHandler extends ClientPlayNetworkHandler {
    public FakeClientPlayNetworkHandler(MinecraftClient client) {
        super(client, null, new FakeClientConnection(NetworkSide.CLIENTBOUND), client.getSession().getProfile(), null);
    }

    @Override
    public void sendPacket(Packet<?> packet) {

    }
}
