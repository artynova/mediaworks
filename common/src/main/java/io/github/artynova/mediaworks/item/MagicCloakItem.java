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
import io.github.artynova.mediaworks.logic.media.MediaDiscoveryHandler;
import io.github.artynova.mediaworks.logic.media.PackagedHexData;
import io.github.artynova.mediaworks.util.HexHelpers;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MagicCloakItem extends ArmorItem implements IotaHolderItem, HexHolderItem {
    public static final ArmorMaterial MATERIAL = new ArmorMaterial() {
        @Override
        public int getDurability(EquipmentSlot slot) {
            return 432; // like elytra
        }

        @Override
        public int getProtectionAmount(EquipmentSlot slot) {
            return 0; // "not armor"
        }

        @Override
        public int getEnchantability() {
            return 25; // like gold
        }

        @Override
        public SoundEvent getEquipSound() {
            return SoundEvents.ITEM_ARMOR_EQUIP_LEATHER;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return null;
        }

        @Override
        public String getName() {
            return "magic_cloak";
        }

        @Override
        public float getToughness() {
            return 0; // "not armor"
        }

        @Override
        public float getKnockbackResistance() {
            return 0; // "not armor"
        }
    };
    public static final String IOTA_TAG = "mediaworks:iota";
    public static final Settings SETTINGS = new Item.Settings().group(IXplatAbstractions.INSTANCE.getTab()).rarity(Rarity.UNCOMMON);

    static {
        MediaDiscoveryHandler.addCustomPackagedHexDiscoverer(harness -> {
            ItemStack maybeCloak = HexHelpers.extend(harness.getCtx()).mediaworks$getForcedCastingStack();
            if (maybeCloak == null) return null;
            if (!maybeCloak.isOf(MediaworksItems.MAGIC_CLOAK.get())) return null;
            // we only "discover" the cloak in contexts where it has been explicitly forced, so essentially in our own casts
            ADHexHolder hexHolder = IXplatAbstractions.INSTANCE.findHexHolder(maybeCloak);
            assert hexHolder != null;
            ADMediaHolder mediaHolder = IXplatAbstractions.INSTANCE.findMediaHolder(maybeCloak);
            return new PackagedHexData(hexHolder, mediaHolder);
        });
    }

    public MagicCloakItem() {
        super(MATERIAL, EquipmentSlot.HEAD, SETTINGS);
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return false;
    }

    @Override
    public @Nullable NbtCompound readIotaTag(ItemStack stack) {
        return stack.getSubNbt(IOTA_TAG);
    }

    @Override
    public boolean canWrite(ItemStack stack, @Nullable Iota iota) {
        return true;
    }

    @Override
    public void writeDatum(ItemStack stack, @Nullable Iota iota) {
        if (iota == null) {
            stack.removeSubNbt(IOTA_TAG);
        } else {
            stack.setSubNbt(IOTA_TAG, HexIotaTypes.serialize(iota));
        }
    }

    @Override
    public @Nullable Iota emptyIota(ItemStack stack) {
        return new NullIota();
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        IotaHolderItem.appendHoverText(this, stack, tooltip, context);
    }

    @Override
    public boolean canDrawMediaFromInventory(ItemStack stack) {
        return true;
    }

    @Override
    public boolean hasHex(ItemStack stack) {
        return stack.getSubNbt(IOTA_TAG) != null;
    }

    @Override
    public @Nullable List<Iota> getHex(ItemStack stack, ServerWorld world) {
        Iota iota = readIota(stack, world);
        if (iota == null) return null;
        if (iota instanceof ListIota listIota) return HexHelpers.decompose(listIota);
        return List.of(iota);
    }

    @Override
    public void writeHex(ItemStack stack, List<Iota> program, int media) {
        writeDatum(stack, new ListIota(program));
    }

    @Override
    public void clearHex(ItemStack stack) {
        writeDatum(stack, null);
    }

    @Override
    public int getMedia(ItemStack stack) {
        return 0;
    }

    @Override
    public int getMaxMedia(ItemStack stack) {
        return 0;
    }

    @Override
    public void setMedia(ItemStack stack, int media) {
        // non-op
    }

    @Override
    public boolean canProvideMedia(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canRecharge(ItemStack stack) {
        return false;
    }
}
