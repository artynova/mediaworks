package io.github.artynova.mediaworks.item;

import at.petrak.hexcasting.api.addldata.ADHexHolder;
import at.petrak.hexcasting.api.addldata.ADMediaHolder;
import at.petrak.hexcasting.api.item.HexHolderItem;
import at.petrak.hexcasting.api.item.IotaHolderItem;
import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.api.spell.iota.ListIota;
import at.petrak.hexcasting.api.spell.iota.NullIota;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import io.github.artynova.mediaworks.api.logic.media.MediaDiscoveryHandler;
import io.github.artynova.mediaworks.api.logic.media.PackagedHexData;
import io.github.artynova.mediaworks.util.HexUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;

import java.util.List;

/**
 * Just an interface masquerading as a class, nothing weird to see here.
 * This is an interface because geckolib requires extending a dedicated GeoArmorItem class on forge and not on fabric,
 * so the actual impl class has to be completely platform-specific.
 */
public interface MagicCloakItem extends DyeableItem, IAnimatable, IotaHolderItem, HexHolderItem {
    ArmorMaterial MATERIAL = new MagicCloakMaterial();
    String IOTA_TAG = "mediaworks:iota";
    Item.Settings SETTINGS = new Item.Settings().group(IXplatAbstractions.INSTANCE.getTab()).rarity(Rarity.UNCOMMON);
    int DEFAULT_COLOR = 0x5B4533;

    static void initPackagedHexDiscovery() {
        MediaDiscoveryHandler.addCustomPackagedHexDiscoverer(harness -> {
            ItemStack maybeCloak = HexUtils.extend(harness.getCtx()).mediaworks$getForcedCastingStack();
            if (maybeCloak == null) return null;
            if (!maybeCloak.isOf(MediaworksItems.MAGIC_CLOAK.get())) return null;
            // we only "discover" the cloak in contexts where it has been explicitly forced, so essentially in our own casts
            ADHexHolder hexHolder = IXplatAbstractions.INSTANCE.findHexHolder(maybeCloak);
            assert hexHolder != null;
            ADMediaHolder mediaHolder = IXplatAbstractions.INSTANCE.findMediaHolder(maybeCloak);
            return new PackagedHexData(hexHolder, mediaHolder);
        });
    }

    default boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return false;
    }

    default void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        IotaHolderItem.appendHoverText(this, stack, tooltip, context);
    }

    @Override
    default void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 20, event -> PlayState.STOP));
    }

    @Override
    default @Nullable NbtCompound readIotaTag(ItemStack stack) {
        return stack.getSubNbt(IOTA_TAG);
    }

    @Override
    default boolean canWrite(ItemStack stack, @Nullable Iota iota) {
        return true;
    }

    @Override
    default void writeDatum(ItemStack stack, @Nullable Iota iota) {
        if (iota == null) {
            stack.removeSubNbt(IOTA_TAG);
        } else {
            stack.setSubNbt(IOTA_TAG, HexIotaTypes.serialize(iota));
        }
    }

    @Override
    default @Nullable Iota emptyIota(ItemStack stack) {
        return new NullIota();
    }

    @Override
    default boolean canDrawMediaFromInventory(ItemStack stack) {
        return true;
    }

    @Override
    default boolean hasHex(ItemStack stack) {
        return stack.getSubNbt(IOTA_TAG) != null;
    }

    @Override
    default @Nullable List<Iota> getHex(ItemStack stack, ServerWorld world) {
        Iota iota = readIota(stack, world);
        if (iota == null) return null;
        if (iota instanceof ListIota listIota) return HexUtils.decompose(listIota);
        return List.of(iota);
    }

    @Override
    default void writeHex(ItemStack stack, List<Iota> program, int media) {
        writeDatum(stack, new ListIota(program));
    }

    @Override
    default void clearHex(ItemStack stack) {
        writeDatum(stack, null);
    }

    @Override
    default int getMedia(ItemStack stack) {
        return 0;
    }

    @Override
    default int getMaxMedia(ItemStack stack) {
        return 0;
    }

    @Override
    default void setMedia(ItemStack stack, int media) {
        // non-op
    }

    @Override
    default boolean canProvideMedia(ItemStack stack) {
        return false;
    }

    @Override
    default boolean canRecharge(ItemStack stack) {
        return false;
    }

    @Override
    default int getColor(ItemStack stack) {
        NbtCompound nbtCompound = stack.getSubNbt(DISPLAY_KEY);
        if (nbtCompound != null && nbtCompound.contains(COLOR_KEY, NbtElement.NUMBER_TYPE)) {
            return nbtCompound.getInt(COLOR_KEY);
        }
        return DEFAULT_COLOR;
    }
}
