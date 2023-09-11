package io.github.artynova.mediaworks.networking;

import dev.architectury.networking.NetworkManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

import java.util.function.Supplier;

/**
 * Message that carries an NbtCompound.
 * For cases when serialization / deserialization of the nbt needs extra data and is thus deferred.
 */
public abstract class NbtCompoundMsg {
    protected final NbtCompound compound;

    public NbtCompoundMsg(NbtCompound compound) {
        this.compound = compound;
    }

    public NbtCompoundMsg(PacketByteBuf buf) {
        this(buf.readUnlimitedNbt());
    }

    public void encode(PacketByteBuf buf) {
        buf.writeNbt(compound);
    }

    public abstract void apply(Supplier<NetworkManager.PacketContext> contextSupplier);
}
