package io.github.artynova.mediaworks.forge.capabilities;

import at.petrak.hexcasting.api.addldata.ADMediaHolder;
import at.petrak.hexcasting.forge.cap.ForgeCapabilityHandler;
import at.petrak.hexcasting.forge.cap.HexCapabilities;
import io.github.artynova.mediaworks.logic.PersistentDataContainer;
import io.github.artynova.mediaworks.logic.PersistentDataWrapper;
import io.github.artynova.mediaworks.logic.macula.MaculaHolder;
import io.github.artynova.mediaworks.logic.projection.AstralProjectionHolder;
import io.github.artynova.mediaworks.misc.ShulkerBoxMediaHolder;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
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

// "simple provider" code is based on https://github.com/gamma-delta/HexMod/blob/c8510ed83d50ac7e05d91ba3f1924e21ec10d837/Forge/src/main/java/at/petrak/hexcasting/forge/cap/ForgeCapabilityHandler.java
public class MediaworksCapabilities {

    public static final Capability<AstralProjectionHolder> PROJECTION_HOLDER_CAP = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Identifier PROJECTION_HOLDER_CAP_ID = id("projection_holder");
    public static final Capability<MaculaHolder> MACULA_HOLDER_CAP = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Identifier MACULA_HOLDER_CAP_ID = id("macula_holder");
    public static final Capability<ADMediaHolder> MEDIA_HOLDER_CAP = HexCapabilities.MEDIA;
    public static final Identifier MEDIA_HOLDER_CAP_ID = ForgeCapabilityHandler.HEX_HOLDER_CAP;

    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(AstralProjectionHolder.class);
        event.register(MaculaHolder.class);
    }

    public static void attachEntityCaps(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();

        if (entity instanceof ServerPlayerEntity player) {
            event.addCapability(PROJECTION_HOLDER_CAP_ID, provideSerializable(player, PROJECTION_HOLDER_CAP, () -> new ProjectionHolderCap(player)));
            event.addCapability(MACULA_HOLDER_CAP_ID, provideSerializable(player, MACULA_HOLDER_CAP, () -> new MaculaHolderCap(player)));
        }
    }

    public static void attachItemCaps(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();
        if (ShulkerBoxMediaHolder.isShulkerBox(stack.getItem())) {
            event.addCapability(MEDIA_HOLDER_CAP_ID, provideSimple(stack, MEDIA_HOLDER_CAP, () -> new ShulkerBoxMediaHolder(stack)));
        }
    }

    private static <T extends PersistentDataContainer, W extends PersistentDataWrapper<T>> PersistentProvider<T, W> provideSerializable(ServerPlayerEntity player, Capability<W> capability, NonNullSupplier<W> supplier) {
        return provideSerializable(() -> false, capability, supplier);
    }

    private static <T extends PersistentDataContainer, W extends PersistentDataWrapper<T>> PersistentProvider<T, W> provideSerializable(BooleanSupplier invalidated, Capability<W> capability, NonNullSupplier<W> supplier) {
        return new PersistentProvider<>(invalidated, capability, LazyOptional.of(supplier));
    }

    private static <T> SimpleProvider<T> provideSimple(ItemStack stack, Capability<T> capability,
                                                       NonNullSupplier<T> supplier) {
        return provideSimple(stack::isEmpty, capability, supplier);
    }

    private static <T> SimpleProvider<T> provideSimple(BooleanSupplier invalidated, Capability<T> capability,
                                                       NonNullSupplier<T> supplier) {
        return new SimpleProvider<>(invalidated, capability, LazyOptional.of(supplier));
    }

    private record SimpleProvider<CAP>(BooleanSupplier invalidated,
                                       Capability<CAP> capability,
                                       LazyOptional<CAP> instance) implements ICapabilityProvider {

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            if (invalidated.getAsBoolean()) {
                return LazyOptional.empty();
            }

            return cap == capability ? instance.cast() : LazyOptional.empty();
        }
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
