package io.github.artynova.mediaworks.casting.mishap

import at.petrak.hexcasting.api.misc.FrozenColorizer
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.Mishap
import io.github.artynova.mediaworks.logic.macula.MaculaServer
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.text.Text
import net.minecraft.util.DyeColor

/**
 * A mishap triggered when an action goes against Macula rules.
 * Error message translation is expected to be at "hexcasting.mishap.black_eye.REASON", where REASON
 * is the actual string value of the string passed to the constructor. The translation is also supplied with the name
 * of the processed pattern as the first argument.
 */
class MishapBlackEye(val reason: String) : Mishap() {
    val BLINDNESS_TICKS: Int = 200

    override fun accentColor(ctx: CastingContext, errorCtx: Context): FrozenColorizer = dyeColor(DyeColor.BLACK)

    override fun errorMessage(ctx: CastingContext, errorCtx: Context): Text = error("black_eye.$reason", actionName(errorCtx.action))

    override fun execute(ctx: CastingContext, errorCtx: Context, stack: MutableList<Iota>) {
        ctx.caster.addStatusEffect(StatusEffectInstance(StatusEffects.BLINDNESS, BLINDNESS_TICKS))
        MaculaServer.getMacula(ctx.caster).clear()
        MaculaServer.syncContentToClient(ctx.caster)
    }
}