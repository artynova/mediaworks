package io.github.artynova.mediaworks.util;

import at.petrak.hexcasting.api.addldata.ADHexHolder;
import at.petrak.hexcasting.api.addldata.ADMediaHolder;
import at.petrak.hexcasting.api.block.circle.BlockEntityAbstractImpetus;
import at.petrak.hexcasting.api.misc.DiscoveryHandlers;
import at.petrak.hexcasting.api.player.Sentinel;
import at.petrak.hexcasting.api.spell.casting.CastingContext;
import at.petrak.hexcasting.api.spell.casting.CastingHarness;
import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.api.spell.iota.ListIota;
import at.petrak.hexcasting.api.utils.MediaHelper;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import io.github.artynova.mediaworks.api.logic.media.MediaDiscoveryHandler;
import io.github.artynova.mediaworks.api.logic.media.PackagedHexData;
import io.github.artynova.mediaworks.casting.ExtendedCastingContext;
import io.github.artynova.mediaworks.enchantment.LocaleMagnificationEnchantment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HexUtils {
    public static final double DEFAULT_AMBIT = 32.0;

    public static double getAmbitRadius(PlayerEntity player) {
        return DEFAULT_AMBIT + LocaleMagnificationEnchantment.getAmbitIncrease(player);
    }

    /**
     * Helper method to find whether a vector is or is not within caster's ambit
     * without needing to create a new casting context.
     */
    public static boolean isInAmbit(Vec3d vec, ServerPlayerEntity caster) {
        if (vec.squaredDistanceTo(caster.getEyePos()) <= MathHelper.square(getAmbitRadius(caster))) return true;
        Sentinel sentinel = IXplatAbstractions.INSTANCE.getSentinel(caster);
        return sentinel.hasSentinel() && sentinel.extendsRange() && caster.getWorld().getRegistryKey().equals(sentinel.dimension()) && vec.squaredDistanceTo(sentinel.position()) < 256.0;
    }

    public static List<Iota> decompose(ListIota listIota) {
        List<Iota> res = new ArrayList<>();
        for (Iota iota : listIota.getList()) res.add(iota);
        return res;
    }

    public static Vec3d smartRaycast(double ambit, @NotNull Vec3d origin, @NotNull Vec3d look) {
        return origin.add(look.normalize().multiply(ambit));
    }

    public static List<ADMediaHolder> collectMediaHolders(List<ItemStack> stacks) {
        return stacks.stream().filter(MediaHelper::isMediaItem).map(IXplatAbstractions.INSTANCE::findMediaHolder).filter(Objects::nonNull).sorted((holder1, holder2) -> MediaHelper.compareMediaItem(holder2, holder1)).toList();
    }

    public static int getAvailableContextMedia(CastingContext context) {
        // spell circle handling
        if (context.getSpellCircle() != null) {
            BlockEntity blockEntity = context.getWorld().getBlockEntity(context.getSpellCircle().getImpetusPos());
            if (!(blockEntity instanceof BlockEntityAbstractImpetus impetus)) return 0;
            return impetus.getMedia();
        }
        CastingHarness dummyHarness = new CastingHarness(context); // yes, it's not the original harness, but will do just fine for media discovery purposes
        List<ADMediaHolder> holders;
        // casting item handling
        if (context.getSource() == CastingContext.CastSource.PACKAGED_HEX)
            holders = collectPackagedFriendlyMediaHolders(dummyHarness);
        else holders = DiscoveryHandlers.collectMediaHolders(dummyHarness);
        return (int) holders.stream().collect(Collectors.summarizingInt(holder -> holder.withdrawMedia(-1, true))).getSum();
    }

    /**
     * Collects media holders specifically available to a {@link CastingContext.CastSource#PACKAGED_HEX} cast.
     * Use when it is known that the cast source is a casting item in the casting hand.
     */
    public static List<ADMediaHolder> collectPackagedFriendlyMediaHolders(CastingHarness harness) {
        List<ADMediaHolder> holders = new ArrayList<>();
        PackagedHexData data = MediaDiscoveryHandler.collectCustomPackagedHex(harness);
        if (data == null) {
            ItemStack castingStack = harness.getCtx().getCaster().getStackInHand(harness.getCtx().getCastingHand());
            ADHexHolder hexHolder = IXplatAbstractions.INSTANCE.findHexHolder(castingStack);
            // this shouldn't really happen, but just in case some addon messes with casting items
            if (hexHolder == null) return holders;
            ADMediaHolder mediaHolder = IXplatAbstractions.INSTANCE.findMediaHolder(castingStack);
            data = new PackagedHexData(hexHolder, mediaHolder);
        }

        if (data.hexHolder().canDrawMediaFromInventory()) holders = DiscoveryHandlers.collectMediaHolders(harness);
        if (data.mediaHolder() == null) return holders;
        // this is needed because in standard hexcasting, if the media holders discoverers discover something twice,
        // it can still only withdraw the media once,
        // but since this method is used for calculating potential media without withdrawing, it needs to be careful not to count something twice
        // the manual reference comparison tomfoolery specifically is needed because CardinalComponents considers all ItemComponent instances of the same class equal,
        // thus we cannot rely on the equals() method in ADMediaHolder, which is a cardinal component on fabric
        for (ADMediaHolder holder : holders) {
            if (holder == data.mediaHolder()) return holders;
        }
        holders.add(data.mediaHolder());
        return holders;
    }

    /**
     * Wraps the cast from {@link CastingContext} to {@link ExtendedCastingContext} because we know
     * the cast is always possible, even if the IDE doesn't.
     */
    public static ExtendedCastingContext extend(CastingContext castingContext) {
        return (ExtendedCastingContext) (Object) castingContext;
    }
}
