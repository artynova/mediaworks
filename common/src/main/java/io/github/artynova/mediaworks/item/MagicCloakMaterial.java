package io.github.artynova.mediaworks.item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class MagicCloakMaterial implements ArmorMaterial {
    @Override
    public int getDurability(EquipmentSlot slot) {
        return 432; // like elytra
    }

    @Override
    public int getProtectionAmount(EquipmentSlot slot) {
        return 0; // "not armor"
    }

    @Override
    public int getEnchantability() {
        return 25; // like gold
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ITEM_ARMOR_EQUIP_LEATHER;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.EMPTY;
    }

    @Override
    public String getName() {
        return "magic_cloak";
    }

    @Override
    public float getToughness() {
        return 0; // "not armor"
    }

    @Override
    public float getKnockbackResistance() {
        return 0; // "not armor"
    }
}
