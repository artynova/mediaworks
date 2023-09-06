package io.github.artynova.mediaworks.mixin.projection;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.artynova.mediaworks.client.projection.AstralProjectionClient;
import io.github.artynova.mediaworks.client.render.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Unique
    private final WorldRenderContext context = new WorldRenderContext();
    @Final
    @Shadow
    private BufferBuilderStorage bufferBuilders;

    // This is a simplified recreation of Fabric's "after entities" render event
    @Inject(method = "render", at = @At("HEAD"))
    private void beforeRender(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci) {
        context.setValues((WorldRenderer) (Object) this, matrices, tickDelta, camera, bufferBuilders.getEntityVertexConsumers());
    }

    @Inject(method = "render", at = @At(value = "CONSTANT", args = "stringValue=blockentities", ordinal = 0))
    private void afterEntities(CallbackInfo ci) {
        if (AstralProjectionClient.isDissociated()) AstralProjectionClient.renderAstralBody(context);
    }

    @ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;getFocusedEntity()Lnet/minecraft/entity/Entity;", ordinal = 3))
    private Entity makeBodyRender(Entity previous) {
        if (AstralProjectionClient.isDissociated()) return MinecraftClient.getInstance().player;
        return previous;
    }

    @ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isSpectator()Z"))
    private boolean disableCulling(boolean previous) {
        if (AstralProjectionClient.isDissociated()) return true;
        return previous;
    }

    @ModifyExpressionValue(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;method_43788(Lnet/minecraft/client/render/Camera;)Z"))
    private boolean disableSky(boolean previous) {
        if (AstralProjectionClient.isDissociated()) return true;
        return previous;
    }
}
