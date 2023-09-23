package io.github.artynova.mediaworks.mixin.cloak;

import at.petrak.hexcasting.api.spell.casting.CastingContext;
import at.petrak.hexcasting.api.spell.casting.CastingHarness;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.artynova.mediaworks.util.HexUtils;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CastingHarness.class)
public abstract class CastingHarnessMixin {
    @ModifyExpressionValue(method = "withdrawMedia(IZ)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;getStackInHand(Lnet/minecraft/util/Hand;)Lnet/minecraft/item/ItemStack;"))
    private ItemStack injectCustomStack(ItemStack stack) {
        ItemStack forced = HexUtils.extend(getCtx()).mediaworks$getForcedCastingStack();
        return forced == null ? stack : forced;
    }

    @Shadow
    public abstract CastingContext getCtx();
}
