package io.github.artynova.mediaworks.mixin.projection;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.artynova.mediaworks.client.projection.camera.AstralCameraEntity;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class EntityMixin {
    @ModifyExpressionValue(method = "pushAwayFrom", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;noClip:Z"))
    private boolean noClipCamera(boolean previous, Entity other) {
        if (other instanceof AstralCameraEntity || (Object) this instanceof AstralCameraEntity) return true;
        return previous;
    }
}
