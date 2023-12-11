package io.github.artynova.mediaworks.mixin.cloak;

import at.petrak.hexcasting.api.spell.casting.CastingContext;
import at.petrak.hexcasting.api.spell.casting.CastingHarness;
import at.petrak.hexcasting.api.spell.mishaps.Mishap;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.artynova.mediaworks.casting.DamageSourceReciprocationOvercast;
import io.github.artynova.mediaworks.casting.ExtendedCastingContext;
import io.github.artynova.mediaworks.item.MediaworksItems;
import io.github.artynova.mediaworks.util.HexUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
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

    @WrapOperation(method = "withdrawMedia(IZ)I", at = @At(value = "INVOKE", target = "Lat/petrak/hexcasting/api/spell/mishaps/Mishap$Companion;trulyHurt(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/damage/DamageSource;F)V"))
    private void injectReciprocationOvercast(Mishap.Companion companion, LivingEntity entity, DamageSource source, float damage, Operation<Void> original) {
        ExtendedCastingContext extended = HexUtils.extend(getCtx());
        ItemStack maybeForcedStack = extended.mediaworks$getForcedCastingStack();

        if (maybeForcedStack == null || !maybeForcedStack.isOf(MediaworksItems.MAGIC_CLOAK.get()))
            original.call(companion, entity, source, damage);

        original.call(companion, entity, new DamageSourceReciprocationOvercast(extended.mediaworks$getReciprocationReps() + 1), damage);
    }

    @Shadow
    public abstract CastingContext getCtx();
}
