package io.github.artynova.mediaworks.fabric.mixin;

import net.minecraft.item.ArmorItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

@Mixin(GeoArmorRenderer.class)
public interface GeoArmorRendererAccessor<T extends ArmorItem & IAnimatable> {
    @Invoker
    void callFitToBiped();
}
