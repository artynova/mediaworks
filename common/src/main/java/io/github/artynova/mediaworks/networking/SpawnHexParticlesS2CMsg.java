package io.github.artynova.mediaworks.networking;

import at.petrak.hexcasting.api.misc.FrozenColorizer;
import at.petrak.hexcasting.api.spell.ParticleSpray;
import dev.architectury.networking.NetworkManager;
import io.github.artynova.mediaworks.client.util.RenderUtils;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;

import java.util.function.Supplier;

// Rewritten in architectury for ease of tweaking
public class SpawnHexParticlesS2CMsg {
    private final ParticleSpray spray;
    private final FrozenColorizer colorizer;

    public SpawnHexParticlesS2CMsg(ParticleSpray spray, FrozenColorizer colorizer) {
        this.spray = spray;
        this.colorizer = colorizer;
    }

    public SpawnHexParticlesS2CMsg(PacketByteBuf buf) {
        this.spray = new ParticleSpray(new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble()), new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble()), buf.readDouble(), buf.readDouble(), buf.readInt());
        this.colorizer = FrozenColorizer.fromNBT(buf.readUnlimitedNbt());
    }

    public void encode(PacketByteBuf buf) {
        buf.writeDouble(this.spray.getPos().x);
        buf.writeDouble(this.spray.getPos().y);
        buf.writeDouble(this.spray.getPos().z);
        buf.writeDouble(this.spray.getVel().x);
        buf.writeDouble(this.spray.getVel().y);
        buf.writeDouble(this.spray.getVel().z);
        buf.writeDouble(this.spray.getFuzziness());
        buf.writeDouble(this.spray.getSpread());
        buf.writeInt(this.spray.getCount());
        buf.writeNbt(this.colorizer.serializeToNBT());
    }

    public void apply(Supplier<NetworkManager.PacketContext> contextSupplier) {
        contextSupplier.get().queue(() -> RenderUtils.renderHexParticleSpray(spray, colorizer));
    }
}
