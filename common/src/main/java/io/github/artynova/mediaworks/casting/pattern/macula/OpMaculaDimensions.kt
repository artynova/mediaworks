package io.github.artynova.mediaworks.casting.pattern.macula

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import io.github.artynova.mediaworks.casting.pattern.asActionResult
import io.github.artynova.mediaworks.logic.macula.Macula
import io.github.artynova.mediaworks.logic.macula.MaculaServer
import net.minecraft.util.math.Vec2f

class OpMaculaDimensions : ConstMediaAction {
    override val argc: Int = 0

    override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
        val macula: Macula = MaculaServer.getMacula(ctx.caster)
        return Vec2f(macula.width.toFloat(), macula.height.toFloat()).asActionResult()
    }
}