package io.github.artynova.mediaworks.casting.pattern

import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.spell.mishaps.MishapNotEnoughArgs
import io.github.artynova.mediaworks.casting.iota.VisageIota
import io.github.artynova.mediaworks.logic.macula.Visage

fun List<Iota>.getVisage(idx: Int, argc: Int = 0): Visage {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    if (x is VisageIota) {
        return x.visage
    } else {
        throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "visage")
    }
}

fun Visage.asActionResult(): List<Iota> {
    return listOf(VisageIota(this))
}
