package io.github.artynova.mediaworks.mixin.cloak;

import io.github.artynova.mediaworks.enchantment.MediaShieldEnchantment;
import io.github.artynova.mediaworks.enchantment.ReciprocationEnchantment;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Unique
    private DamageSource mediaworks$lastSource;

    @Inject(method = "applyDamage", at = @At("HEAD"))
    private void captureDamageSource(DamageSource source, float amount, CallbackInfo ci) {
        this.mediaworks$lastSource = source;
    }

    @ModifyVariable(method = "applyDamage", at = @At("HEAD"), index = 2, argsOnly = true)
    private float modifyIncomingDamage(float amount) {
        if (isInvulnerableTo(mediaworks$lastSource)) return amount;
        if (mediaworks$lastSource.isUnblockable()) return amount;
        return MediaShieldEnchantment.processIncomingDamage((PlayerEntity) (Object) this, amount);
    }

    @Inject(method = "damage", at = @At("RETURN"))
    private void captureReturn(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ()) ReciprocationEnchantment.processPlayerHurt((PlayerEntity) (Object) this);
    }

    @Shadow
    public abstract boolean isInvulnerableTo(DamageSource damageSource);
}
