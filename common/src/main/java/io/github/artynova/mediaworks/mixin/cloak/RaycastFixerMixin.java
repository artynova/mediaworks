package io.github.artynova.mediaworks.mixin.cloak;

import at.petrak.hexcasting.api.spell.Action;
import at.petrak.hexcasting.api.spell.casting.CastingContext;
import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.common.casting.operators.OpBlockAxisRaycast;
import at.petrak.hexcasting.common.casting.operators.OpBlockRaycast;
import at.petrak.hexcasting.common.casting.operators.OpEntityRaycast;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.artynova.mediaworks.util.HexHelpers;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin({OpBlockAxisRaycast.class, OpBlockRaycast.class, OpEntityRaycast.class})
public class RaycastFixerMixin {
    @Unique
    private ServerPlayerEntity mediaworks$caster;

    @Inject(method = "execute", at = @At("HEAD"), remap = false)
    private void captureCaster(List<? extends Iota> args, CastingContext ctx, CallbackInfoReturnable<List<Iota>> cir) {
        this.mediaworks$caster = ctx.getCaster();
    }

    @WrapOperation(method = "execute", at = @At(value = "INVOKE", target = "Lat/petrak/hexcasting/api/spell/Action$Companion;raycastEnd(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;"))
    private Vec3d replaceRaycast(Action.Companion instance, Vec3d origin, Vec3d look, Operation<Vec3d> original) {
        double ambit = HexHelpers.getAmbitRadius(mediaworks$caster);
        // as a bare minimum compatibility in case some other addon also hacks into the ambit,
        // use default method when our functionality cannot detect ambit extensions
        if (ambit == HexHelpers.DEFAULT_AMBIT) return original.call(instance, origin, look);
        return HexHelpers.smartRaycast(ambit, origin, look);
    }
}
