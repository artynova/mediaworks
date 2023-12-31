package io.github.artynova.mediaworks.casting.pattern.misc

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import io.github.artynova.mediaworks.util.MediaUtils

class OpGetMedia : ConstMediaAction {
    override val argc: Int = 0
    override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
        return (MediaUtils.getAvailableContextMedia(ctx).toDouble() / MediaConstants.DUST_UNIT).asActionResult
    }
}