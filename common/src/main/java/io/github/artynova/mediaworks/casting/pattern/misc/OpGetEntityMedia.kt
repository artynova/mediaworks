package io.github.artynova.mediaworks.casting.pattern.misc

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getEntity
import at.petrak.hexcasting.api.spell.iota.Iota
import io.github.artynova.mediaworks.util.MediaUtils

class OpGetEntityMedia : ConstMediaAction {
    override val argc: Int = 1

    override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
        val entity = args.getEntity(0, argc)
        return (MediaUtils.getEntityMedia(entity).toDouble() / MediaConstants.DUST_UNIT).asActionResult
    }
}