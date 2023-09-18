package io.github.artynova.mediaworks.effect;

import io.github.artynova.mediaworks.logic.projection.AstralProjectionServer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.network.ServerPlayerEntity;

public class AstralProjectionEffect extends StatusEffect {
    public static final int COLOR = 0xFF_65518A;

    public AstralProjectionEffect() {
        super(StatusEffectCategory.BENEFICIAL, COLOR);
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);
        if (entity instanceof ServerPlayerEntity serverPlayer) {
            AstralProjectionServer.startProjection(serverPlayer);
        }
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);
        if (entity instanceof ServerPlayerEntity serverPlayer) {
            AstralProjectionServer.endProjection(serverPlayer);
        }
    }
}
