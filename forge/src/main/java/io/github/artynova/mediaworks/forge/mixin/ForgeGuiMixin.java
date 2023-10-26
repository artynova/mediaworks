package io.github.artynova.mediaworks.forge.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.artynova.mediaworks.client.projection.AstralProjectionClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ForgeGui.class)
public class ForgeGuiMixin {
    @WrapOperation(method = "renderHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getCameraEntity()Lnet/minecraft/entity/Entity;"))
    private Entity injectPlayerInfo(MinecraftClient client, Operation<Entity> operation) {
        if (AstralProjectionClient.isDissociated()) return client.player;
        return operation.call(client);
    }
}
