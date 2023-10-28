package io.github.artynova.mediaworks.util;

import at.petrak.hexcasting.api.addldata.ADHexHolder;
import at.petrak.hexcasting.api.addldata.ADMediaHolder;
import at.petrak.hexcasting.api.block.circle.BlockEntityAbstractImpetus;
import at.petrak.hexcasting.api.misc.DiscoveryHandlers;
import at.petrak.hexcasting.api.spell.casting.CastingContext;
import at.petrak.hexcasting.api.spell.casting.CastingHarness;
import at.petrak.hexcasting.api.utils.MediaHelper;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import io.github.artynova.mediaworks.api.logic.media.MediaDiscoveryHandler;
import io.github.artynova.mediaworks.api.logic.media.PackagedHexData;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MediaUtils {
    public static List<ADMediaHolder> collectMediaHolders(List<ItemStack> stacks) {
        return stacks.stream().filter(MediaHelper::isMediaItem).map(IXplatAbstractions.INSTANCE::findMediaHolder).filter(Objects::nonNull).sorted((holder1, holder2) -> MediaHelper.compareMediaItem(holder2, holder1)).toList();
    }

    public static int getTotalMedia(List<ADMediaHolder> holders) {
        return (int) holders.stream().collect(Collectors.summarizingInt(holder -> holder.withdrawMedia(-1, true))).getSum();
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
        return getTotalMedia(holders);
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

    public static int getEntityMedia(Entity entity) {
        if (entity instanceof ItemEntity itemEntity) return getItemStackMedia(itemEntity.getStack());
        if (entity instanceof ServerPlayerEntity player) return getPlayerMedia(player);
        return 0;
    }

    /**
     * If the item stack is a media holder, this method works regardless of whether
     * the stack can provide media for a cast or not.
     * So, for example, this would get the media amount of a {@link at.petrak.hexcasting.common.items.magic.ItemCypher Cypher}
     * the same as it would for an amethyst shard stack.
     *
     * @param stack item stack.
     * @return amount of media.
     */
    public static int getItemStackMedia(ItemStack stack) {
        ADMediaHolder holder = IXplatAbstractions.INSTANCE.findMediaHolder(stack);
        if (holder == null) return 0;
        return holder.withdrawMedia(-1, true);
    }

    public static int getPlayerMedia(ServerPlayerEntity player) {
        CastingHarness harness = IXplatAbstractions.INSTANCE.getHarness(player, Hand.MAIN_HAND);
        List<ADMediaHolder> holders = DiscoveryHandlers.collectMediaHolders(harness);
        return getTotalMedia(holders);
    }

    public static int getPosMedia(Vec3d vector, ServerWorld world) {
        BlockPos pos = new BlockPos(vector);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof BlockEntityAbstractImpetus impetus) return impetus.getMedia();
        if (!(blockEntity instanceof Inventory inventory)) return 0;
        List<ItemStack> stacks = new ArrayList<>(inventory.size());
        for (int i = 0; i < inventory.size(); i++) stacks.add(inventory.getStack(i));
        return getTotalMedia(collectMediaHolders(stacks));
    }
}
