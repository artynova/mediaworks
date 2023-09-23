package io.github.artynova.mediaworks.mixin.cloak;


import io.github.artynova.mediaworks.client.armor.ArmorLayersCulled;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BufferBuilderStorage.class)
public class BufferBuilderStorageMixin {
    @Inject(method = "assignBufferBuilder", at = @At("TAIL"))
    private static void assignCulledGlints(Object2ObjectLinkedOpenHashMap<RenderLayer, BufferBuilder> builderStorage, RenderLayer layer, CallbackInfo ci) {
        if (layer == RenderLayer.getArmorGlint()) {
            builderStorage.put(ArmorLayersCulled.getArmorGlintCull(), new BufferBuilder(ArmorLayersCulled.getArmorGlintCull().getExpectedBufferSize()));
        } else if (layer == RenderLayer.getArmorEntityGlint()) {
            builderStorage.put(ArmorLayersCulled.getArmorEntityGlintCull(), new BufferBuilder(ArmorLayersCulled.getArmorEntityGlintCull().getExpectedBufferSize()));
        }
    }
}
