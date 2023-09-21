package io.github.artynova.mediaworks.casting.pattern.misc

import at.petrak.hexcasting.api.addldata.ADIotaHolder
import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import at.petrak.hexcasting.xplat.IXplatAbstractions
import io.github.artynova.mediaworks.item.MediaworksItems
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ItemStack

class OpCloakRead : ConstMediaAction {
    override val argc: Int = 0
    override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
        val stack: ItemStack = ctx.caster.getEquippedStack(EquipmentSlot.HEAD)
        if (!stack.isOf(MediaworksItems.MAGIC_CLOAK.get())) return listOf(NullIota())
        val holder: ADIotaHolder = IXplatAbstractions.INSTANCE.findDataHolder(stack)!!
        return listOf(holder.readIota(ctx.caster.getWorld()) ?: NullIota())
    }
}