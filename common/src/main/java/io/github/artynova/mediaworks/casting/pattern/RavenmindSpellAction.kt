package io.github.artynova.mediaworks.casting.pattern

import at.petrak.hexcasting.api.spell.Action
import at.petrak.hexcasting.api.spell.OperationResult
import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.casting.eval.SpellContinuation
import at.petrak.hexcasting.api.spell.casting.sideeffects.OperatorSideEffect
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapNotEnoughArgs

interface RavenmindSpellAction : Action {
    val argc: Int
    fun hasCastingSound(ctx: CastingContext): Boolean = true

    fun awardsCastingStat(ctx: CastingContext): Boolean = true

    fun execute(
        args: List<Iota>,
        ctx: CastingContext,
        ravenmind: Iota?
    ): Triple<RenderedSpell, Int, List<ParticleSpray>>?

    // this is basically SpellAction code
    // changes are minimal, just needed to access the cast's ravenmind
    override fun operate(
        continuation: SpellContinuation,
        stack: MutableList<Iota>,
        ravenmind: Iota?,
        ctx: CastingContext
    ): OperationResult {
        val argc = this.argc
        if (argc > stack.size)
            throw MishapNotEnoughArgs(argc, stack.size)
        val args = stack.takeLast(argc)
        for (_i in 0 until argc) stack.removeLast()
        val executeResult =
            this.execute(args, ctx, ravenmind) ?: return OperationResult(continuation, stack, ravenmind, listOf())
        val (spell, media, particles) = executeResult

        val sideEffects = mutableListOf<OperatorSideEffect>()

        if (media > 0)
            sideEffects.add(OperatorSideEffect.ConsumeMedia(media))

        // Don't have an effect if the caster isn't enlightened, even if processing other side effects
        if (!isGreat || ctx.isCasterEnlightened)
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
}