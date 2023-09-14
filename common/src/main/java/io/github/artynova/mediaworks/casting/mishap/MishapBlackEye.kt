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

class MishapBlackEye(val reason: Reason) : Mishap() {
    val BLINDNESS_TICKS: Int = 200

    enum class Reason(val lang: String) {
        BAD_DIMENSIONS("dimensions"),
        EXCEED_VISAGE_CAP("visage_cap"),
        EXCEED_DURATION_CAP("duration_cap");
    }

    override fun accentColor(ctx: CastingContext, errorCtx: Context): FrozenColorizer = dyeColor(DyeColor.BLACK)

    override fun errorMessage(ctx: CastingContext, errorCtx: Context): Text = error("black_eye", Text.translatable(reason.lang))

    override fun execute(ctx: CastingContext, errorCtx: Context, stack: MutableList<Iota>) {
        System.out.println(reason.lang)
        ctx.caster.addStatusEffect(StatusEffectInstance(StatusEffects.BLINDNESS, BLINDNESS_TICKS))
        MaculaServer.getMacula(ctx.caster).clear()
        MaculaServer.syncContentToClient(ctx.caster)
    }
}