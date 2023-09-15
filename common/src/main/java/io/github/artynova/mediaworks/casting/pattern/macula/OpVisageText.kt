package io.github.artynova.mediaworks.casting.pattern.macula

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getPositiveDouble
import at.petrak.hexcasting.api.spell.iota.Iota
import io.github.artynova.mediaworks.casting.pattern.asActionResult
import io.github.artynova.mediaworks.logic.macula.TextVisage
import net.minecraft.text.Text
import kotlin.math.roundToInt

class OpVisageText(val bounded: Boolean) : ConstMediaAction {
    override val argc: Int = if (bounded) 3 else 1

    override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
        val width: Int = if (bounded) args.getPositiveDouble(0, argc).roundToInt() else TextVisage.UNLIMITED_SIZE
        val height: Int = if (bounded) args.getPositiveDouble(1, argc).roundToInt() else TextVisage.UNLIMITED_SIZE

        val text: Text = TextVisage.captureText(args.get(argc - 1))
        val visage = if (bounded) TextVisage(text, width, height) else TextVisage(text)

        return visage.asActionResult()
    }
}