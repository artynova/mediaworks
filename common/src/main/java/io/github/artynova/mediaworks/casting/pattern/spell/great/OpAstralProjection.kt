package io.github.artynova.mediaworks.casting.pattern.spell.great

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.casting.eval.SpellContinuation
import at.petrak.hexcasting.api.spell.casting.sideeffects.OperatorSideEffect
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapImmuneEntity
import at.petrak.hexcasting.api.spell.mishaps.MishapNotEnoughArgs
import io.github.artynova.mediaworks.casting.pattern.RavenmindSpellAction
import io.github.artynova.mediaworks.effect.MediaworksEffects
import io.github.artynova.mediaworks.logic.projection.AstralProjectionServer
import net.minecraft.entity.effect.StatusEffectInstance

class OpAstralProjection : RavenmindSpellAction {
    override val isGreat: Boolean = true
    override val argc: Int = 1
    val costPerSecond: Int = MediaConstants.DUST_UNIT * 2

    override fun operate(
        continuation: SpellContinuation,
        stack: MutableList<Iota>,
        ravenmind: Iota?,
        ctx: CastingContext
    ): OperationResult {
        if (this.argc > stack.size)
            throw MishapNotEnoughArgs(this.argc, stack.size)
        val args = stack.takeLast(this.argc)
        for (_i in 0 until this.argc) stack.removeLast()
        val executeResult = this.execute(args, ctx, ravenmind)
        val (spell, media, particles) = executeResult

        val sideEffects = mutableListOf<OperatorSideEffect>()

        sideEffects.add(OperatorSideEffect.ConsumeMedia(media))

        if (ctx.isCasterEnlightened)
            sideEffects.add(
                OperatorSideEffect.AttemptSpell(
                    spell,
                    this.hasCastingSound(ctx),
                    this.awardsCastingStat(ctx)
                )
            )

        for (spray in particles)
            sideEffects.add(OperatorSideEffect.Particles(spray))

        return OperationResult(continuation, stack, ravenmind, sideEffects)
    }

    override fun execute(
        args: List<Iota>,
        ctx: CastingContext,
        ravenmind: Iota?
    ): Triple<RenderedSpell, Int, List<ParticleSpray>> {
        val duration: Double = args.getPositiveDouble(0, argc)

        if (ctx.caster.hasStatusEffect(MediaworksEffects.ASTRAL_PROJECTION.get())) {
            throw MishapImmuneEntity(ctx.caster)
        }

        return Triple(
            Spell(duration, ravenmind),
            (duration * costPerSecond).toInt(),
            listOf(ParticleSpray.burst(ctx.caster.pos, 1.0))
        )
    }

    private data class Spell(val duration: Double, val ravenmind: Iota?) : RenderedSpell {
        override fun cast(ctx: CastingContext) {
            ctx.caster.addStatusEffect(
                StatusEffectInstance(
                    MediaworksEffects.ASTRAL_PROJECTION.get(),
                    (duration * 20).toInt(),
                    0,
                    false,
                    false
                )
            )

            AstralProjectionServer.getProjection(ctx.caster).iota = ravenmind
        }
    }
}