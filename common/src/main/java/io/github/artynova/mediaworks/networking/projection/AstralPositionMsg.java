package io.github.artynova.mediaworks.networking.projection;

import dev.architectury.networking.NetworkManager;
import io.github.artynova.mediaworks.projection.AstralPosition;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;

import java.util.function.Supplier;

public abstract class AstralPositionMsg {
    protected final AstralPosition data;

    public AstralPositionMsg(AstralPosition data) {
        this.data = data;
    }

    public AstralPositionMsg(PacketByteBuf buf) {
        this(new AstralPosition(new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble()), buf.readFloat(), buf.readFloat()));
    }

    public void encode(PacketByteBuf buf) {
        buf.writeDouble(data.coordinates().getX());
        buf.writeDouble(data.coordinates().getY());
        buf.writeDouble(data.coordinates().getZ());
        buf.writeFloat(data.yaw());
        buf.writeFloat(data.pitch());
    }

    public abstract void apply(Supplier<NetworkManager.PacketContext> contextSupplier);
}
