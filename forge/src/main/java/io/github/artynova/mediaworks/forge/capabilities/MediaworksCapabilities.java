package io.github.artynova.mediaworks.forge.capabilities;

import io.github.artynova.mediaworks.logic.PersistentDataContainer;
import io.github.artynova.mediaworks.logic.PersistentDataWrapper;
import io.github.artynova.mediaworks.logic.macula.MaculaHolder;
import io.github.artynova.mediaworks.logic.projection.AstralProjectionHolder;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BooleanSupplier;

import static io.github.artynova.mediaworks.Mediaworks.id;

public class MediaworksCapabilities {

    public static final Capability<AstralProjectionHolder> PROJECTION_HOLDER_CAP = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Identifier PROJECTION_HOLDER_CAP_ID = id("projection_holder");
    public static final Capability<MaculaHolder> MACULA_HOLDER_CAP = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Identifier MACULA_HOLDER_CAP_ID = id("macula_holder");

    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(AstralProjectionHolder.class);
        event.register(MaculaHolder.class);
    }

    public static void attachEntityCaps(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();

        if (entity instanceof ServerPlayerEntity player) {
            event.addCapability(PROJECTION_HOLDER_CAP_ID, providePersistent(player, PROJECTION_HOLDER_CAP, () -> new ProjectionHolderCap(player)));
            event.addCapability(MACULA_HOLDER_CAP_ID, providePersistent(player, MACULA_HOLDER_CAP, () -> new MaculaHolderCap(player)));
        }
    }

    private static <T extends PersistentDataContainer, W extends PersistentDataWrapper<T>> PersistentProvider<T, W> providePersistent(ServerPlayerEntity player, Capability<W> capability, NonNullSupplier<W> supplier) {
        return new PersistentProvider<>(() -> false, capability, LazyOptional.of(supplier));
    }

    private static <T extends PersistentDataContainer, W extends PersistentDataWrapper<T>> PersistentProvider<T, W> providePersistent(BooleanSupplier invalidated, Capability<W> capability, NonNullSupplier<W> supplier) {
        return new PersistentProvider<>(invalidated, capability, LazyOptional.of(supplier));
    }

    private record PersistentProvider<T extends PersistentDataContainer, W extends PersistentDataWrapper<T>>(
            BooleanSupplier invalidated, Capability<W> capability,
            LazyOptional<W> instance) implements ICapabilitySerializable<NbtCompound> {

        @NotNull
        @Override
        public <V> LazyOptional<V> getCapability(@NotNull Capability<V> cap, @Nullable Direction side) {
            if (invalidated.getAsBoolean()) return LazyOptional.empty();
            return cap == capability ? instance.cast() : LazyOptional.empty();
        }

        @Override
        public NbtCompound serializeNBT() {
            NbtCompound compound = new NbtCompound();
            if (invalidated.getAsBoolean()) return compound;
            getWrapper().unwrap().writeToNbt(compound);
            return compound;
        }

        @Override
        public void deserializeNBT(NbtCompound compound) {
            if (invalidated.getAsBoolean()) return;
            T base = getWrapper().unwrap();
            base.readFromNbt(compound);
        }

        // use only when guaranteed to be valid, do validation checks elsewhere
        private @NotNull W getWrapper() {
            assert !invalidated.getAsBoolean();
            return instance.orElseThrow(() -> new RuntimeException("Does not actually happen")); // the provider is based on a non-null supplier
        }
    }
}
