package io.github.artynova.mediaworks.casting.pattern.spell.macula

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getPositiveDouble
import at.petrak.hexcasting.api.spell.iota.DoubleIota
import at.petrak.hexcasting.api.spell.iota.Iota
import io.github.artynova.mediaworks.casting.mishap.MishapBlackEye
import io.github.artynova.mediaworks.casting.pattern.OverloadedSpellAction
import io.github.artynova.mediaworks.casting.pattern.getVec2
import io.github.artynova.mediaworks.logic.macula.Macula
import io.github.artynova.mediaworks.logic.macula.MaculaServer
import io.github.artynova.mediaworks.logic.macula.TextVisage
import net.minecraft.text.Text
import net.minecraft.util.math.Vec2f

class OpMaculaAdd(val bounded: Boolean) : OverloadedSpellAction {
    val minArgc = if (bounded) 3 else 2

    override fun argc(allArgs: List<Iota>): Int {
        if (allArgs.size <= minArgc) return minArgc
        return if (allArgs.get(minArgc) is DoubleIota) minArgc + 1 else minArgc
    }

    val cost: Int = MediaConstants.DUST_UNIT / 100

    override fun execute(
        args: List<Iota>,
        argc: Int,
        ctx: CastingContext
    ): Triple<RenderedSpell, Int, List<ParticleSpray>> {
        if (MaculaServer.getMacula(ctx.caster).isFull) throw MishapBlackEye(MishapBlackEye.Reason.EXCEED_VISAGE_CAP)
        val origin: Vec2f = args.getVec2(0, argc)
        val dimensions: Vec2f = if (bounded) args.getVec2(1, argc) else TextVisage.UNBOUNDED_DIMENSIONS
        if (bounded && (dimensions.x < 0 || dimensions.y < 0)) throw MishapBlackEye(MishapBlackEye.Reason.BAD_DIMENSIONS)

        val text: Text = TextVisage.captureText(args.get(minArgc - 1))


        val ticks: Double = if (argc == minArgc) -1.0 else args.getPositiveDouble(minArgc, argc) * 20
        if (ticks > Macula.MAX_FLEETING_VISAGE_TICKS) throw MishapBlackEye(MishapBlackEye.Reason.EXCEED_DURATION_CAP)

        val endTime: Long = if (ticks == -1.0) -1 else ctx.caster.world.time + ticks.toLong()

        return Triple(
            Spell(text, origin, dimensions, endTime),
            cost,
            listOf(ParticleSpray.burst(ctx.caster.pos, 1.0))
        )
    }

    private data class Spell(val text: Text, val origin: Vec2f, val dimensions: Vec2f, val endTime: Long) :
        RenderedSpell {
        override fun cast(ctx: CastingContext) {
            val visage: TextVisage = TextVisage(text, origin, dimensions)
            visage.endTime = endTime
            MaculaServer.getMacula(ctx.caster).add(visage)
            MaculaServer.syncContentToClient(ctx.caster)
        }
    }
}