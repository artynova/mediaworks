package io.github.artynova.mediaworks.casting.pattern.vec2

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getDouble
import at.petrak.hexcasting.api.spell.iota.Iota
import io.github.artynova.mediaworks.casting.pattern.asActionResult
import net.minecraft.util.math.Vec2f

class OpConstructVec2 : ConstMediaAction {
    override val argc: Int = 2

    override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
        return Vec2f(args.getDouble(0, argc).toFloat(), args.getDouble(1, argc).toFloat()).asActionResult()
    }
}