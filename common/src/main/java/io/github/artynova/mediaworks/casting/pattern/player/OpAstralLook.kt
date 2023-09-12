package io.github.artynova.mediaworks.casting.pattern.player

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import io.github.artynova.mediaworks.logic.projection.AstralPosition
import io.github.artynova.mediaworks.logic.projection.AstralProjectionServer
import io.github.artynova.mediaworks.util.MathHelpers

class OpAstralLook : ConstMediaAction {
    override val argc: Int = 0
    override val isGreat: Boolean = true

    override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
        val position: AstralPosition? = AstralProjectionServer.getProjection(ctx.caster).position
        if (position == null) return listOf(NullIota())
        return MathHelpers.getRotationVector(position.pitch, position.yaw).asActionResult
    }
}