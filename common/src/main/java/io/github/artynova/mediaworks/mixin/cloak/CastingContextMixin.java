package io.github.artynova.mediaworks.mixin.cloak;

import at.petrak.hexcasting.api.spell.casting.CastingContext;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.artynova.mediaworks.casting.ExtendedCastingContext;
import io.github.artynova.mediaworks.util.HexUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = CastingContext.class, priority = 10000)
public abstract class CastingContextMixin implements ExtendedCastingContext {
    @Unique
    private @Nullable ItemStack mediaworks$forcedCastingStack;

    @ModifyExpressionValue(method = "isVecInRange", at = @At(value = "CONSTANT", args = "doubleValue=1024.0"))
    private double extendAmbit(double original) {
        return MathHelper.square(HexUtils.getAmbitRadius(getCaster()));
    }

    @Unique
    @Override
    public @Nullable ItemStack mediaworks$getForcedCastingStack() {
        return mediaworks$forcedCastingStack;
    }

    @Unique
    @Override
    public void mediaworks$setForcedCastingStack(@Nullable ItemStack itemStack) {
        this.mediaworks$forcedCastingStack = itemStack;
    }

    @Shadow
    public abstract ServerPlayerEntity getCaster();
}
