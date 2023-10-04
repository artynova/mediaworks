package io.github.artynova.mediaworks.casting.pattern.spell.macula

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getPositiveDouble
import at.petrak.hexcasting.api.spell.getVec3
import at.petrak.hexcasting.api.spell.iota.DoubleIota
import at.petrak.hexcasting.api.spell.iota.Iota
import io.github.artynova.mediaworks.api.casting.OverloadedSpellAction
import io.github.artynova.mediaworks.api.logic.macula.Visage
import io.github.artynova.mediaworks.casting.mishap.MishapBlackEye
import io.github.artynova.mediaworks.casting.pattern.getVisage
import io.github.artynova.mediaworks.logic.macula.Macula
import io.github.artynova.mediaworks.logic.macula.MaculaServer
import io.github.artynova.mediaworks.logic.macula.VisageEntry
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i
import kotlin.math.roundToInt

class OpMaculaAdd : OverloadedSpellAction {
    override fun argc(allArgs: List<Iota>): Int {
        if (allArgs.size <= 2) return 2
        return if (allArgs.get(allArgs.size - 1) is DoubleIota) 3 else 2
    }

    val cost: Int = MediaConstants.DUST_UNIT / 100

    override fun execute(
        args: List<Iota>, argc: Int, ctx: CastingContext
    ): Triple<RenderedSpell, Int, List<ParticleSpray>> {
        val macula: Macula = MaculaServer.getMacula(ctx.caster)
        if (macula.checkFullness()) throw MishapBlackEye("visage_cap")

        val originDouble: Vec3d = args.getVec3(0, argc)
        val origin: Vec3i = Vec3i(originDouble.x.roundToInt(), originDouble.y.roundToInt(), originDouble.z.roundToInt())
        val visage: Visage = args.getVisage(1, argc)

        val ticks: Double = if (argc == 2) -1.0 else args.getPositiveDouble(2, argc) * 20
        if (ticks > Macula.MAX_FLEETING_VISAGE_TICKS) throw MishapBlackEye("duration_cap")
        val endTime: Long = if (ticks == -1.0) -1 else ctx.caster.world.time + ticks.toLong()

        return Triple(
            Spell(macula, VisageEntry(visage, origin, endTime)), cost, listOf(ParticleSpray.burst(ctx.caster.pos, 1.0))
        )
    }

    private data class Spell(val macula: Macula, val entry: VisageEntry) : RenderedSpell {
        override fun cast(ctx: CastingContext) {
            MaculaServer.getMacula(ctx.caster).add(entry)
            MaculaServer.syncContentToClient(ctx.caster)
        }
    }
}