package io.github.artynova.mediaworks.casting.pattern

import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.spell.mishaps.MishapNotEnoughArgs
import io.github.artynova.mediaworks.casting.iota.Vec2Iota
import net.minecraft.util.math.Vec2f

fun List<Iota>.getVec2(idx: Int, argc: Int = 0): Vec2f {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    if (x is Vec2Iota) {
        return x.vec2
    } else {
        throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "flatvector")
    }
}

fun Vec2f.asActionResult(): List<Iota> {
    return listOf(Vec2Iota(this))
}
