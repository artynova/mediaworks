package io.github.artynova.mediaworks.mixin.projection;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.artynova.mediaworks.client.projection.AstralProjectionClient;
import io.github.artynova.mediaworks.client.projection.camera.AstralCameraEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {
    @Shadow
    @Final
    protected MinecraftClient client;

    @ModifyExpressionValue(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;isFlyingLocked()Z"))
    private boolean lockFlyingForCamera(boolean previous) {
        if ((Object) this instanceof AstralCameraEntity) return true;
        return previous;
    }

    @ModifyExpressionValue(method = "sendMovementPackets", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isCamera()Z"))
    private boolean fakeMovementPackets(boolean previous) {
        if (AstralProjectionClient.isDissociated()) return true;
        return previous;
    }

}
