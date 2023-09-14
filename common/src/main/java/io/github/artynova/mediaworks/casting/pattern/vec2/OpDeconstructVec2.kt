package io.github.artynova.mediaworks.casting.pattern.vec2

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.DoubleIota
import at.petrak.hexcasting.api.spell.iota.Iota
import io.github.artynova.mediaworks.casting.pattern.getVec2
import net.minecraft.util.math.Vec2f

class OpDeconstructVec2 : ConstMediaAction {
    override val argc: Int = 1

    override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
        val vec: Vec2f = args.getVec2(0, argc)
        return listOf(DoubleIota(vec.x.toDouble()), DoubleIota(vec.y.toDouble()))
    }
}