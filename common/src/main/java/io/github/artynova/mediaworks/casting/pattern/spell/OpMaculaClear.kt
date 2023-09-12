package io.github.artynova.mediaworks.casting.pattern.spell

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import io.github.artynova.mediaworks.MediaworksAbstractions
import io.github.artynova.mediaworks.logic.macula.MaculaServer

class OpMaculaClear : SpellAction {
    override val argc: Int = 0
    val cost: Int = MediaConstants.DUST_UNIT / 100

    override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>>? {
        return Triple(
            Spell(),
            cost,
            listOf(ParticleSpray.burst(ctx.caster.pos, 1.0))
        )
    }

    private class Spell : RenderedSpell {
        override fun cast(ctx: CastingContext) {
            MaculaServer.getMacula(ctx.caster).clear()
            MaculaServer.syncToClient(ctx.caster)
        }
    }
}