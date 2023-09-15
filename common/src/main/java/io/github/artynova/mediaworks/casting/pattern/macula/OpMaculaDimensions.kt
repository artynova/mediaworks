package io.github.artynova.mediaworks.casting.pattern.macula

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.DoubleIota
import at.petrak.hexcasting.api.spell.iota.Iota
import io.github.artynova.mediaworks.logic.macula.Macula
import io.github.artynova.mediaworks.logic.macula.MaculaServer

class OpMaculaDimensions : ConstMediaAction {
    override val argc: Int = 0

    override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
        val macula: Macula = MaculaServer.getMacula(ctx.caster)
        return listOf(DoubleIota(macula.width.toDouble()), DoubleIota(macula.height.toDouble()))
    }
}