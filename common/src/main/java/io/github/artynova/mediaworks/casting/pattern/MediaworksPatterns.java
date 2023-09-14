package io.github.artynova.mediaworks.casting.pattern;

import at.petrak.hexcasting.api.PatternRegistry;
import at.petrak.hexcasting.api.spell.Action;
import at.petrak.hexcasting.api.spell.math.HexDir;
import at.petrak.hexcasting.api.spell.math.HexPattern;
import io.github.artynova.mediaworks.casting.pattern.astral.OpAstralLook;
import io.github.artynova.mediaworks.casting.pattern.astral.OpAstralPos;
import io.github.artynova.mediaworks.casting.pattern.macula.OpMaculaDimensions;
import io.github.artynova.mediaworks.casting.pattern.spell.macula.OpMaculaAdd;
import io.github.artynova.mediaworks.casting.pattern.spell.macula.OpMaculaClear;
import io.github.artynova.mediaworks.casting.pattern.spell.great.OpAstralProjection;
import io.github.artynova.mediaworks.casting.pattern.vec2.OpConstructVec2;
import io.github.artynova.mediaworks.casting.pattern.vec2.OpDeconstructVec2;
import kotlin.Triple;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

import static io.github.artynova.mediaworks.Mediaworks.id;

public class MediaworksPatterns {
    public static List<Triple<HexPattern, Identifier, Action>> PATTERNS = new ArrayList<>();
    public static List<Triple<HexPattern, Identifier, Action>> PER_WORLD_PATTERNS = new ArrayList<>();
    public static HexPattern ASTRAL_PROJECTION = registerPerWorld(HexPattern.fromAngles("qdadwewewdadeadwddaaedqdeddew", HexDir.NORTH_WEST), "astral_projection", new OpAstralProjection());
    public static HexPattern ASTRAL_POS = register(HexPattern.fromAngles("qaqqqqaq", HexDir.NORTH_EAST), "astral_pos", new OpAstralPos());
    public static HexPattern ASTRAL_LOOK = register(HexPattern.fromAngles("waawaq", HexDir.NORTH_EAST), "astral_look", new OpAstralLook());
    public static HexPattern CONSTRUCT_VEC2 = register(HexPattern.fromAngles("ddeedd", HexDir.NORTH_WEST), "construct_vec2", new OpConstructVec2());
    public static HexPattern DECONSTRUCT_VEC2 = register(HexPattern.fromAngles("aaqqaa", HexDir.NORTH_EAST), "deconstruct_vec2", new OpDeconstructVec2());
    public static HexPattern MACULA_ADD_UNBOUNDED = register(HexPattern.fromAngles("aaqdwdwd", HexDir.NORTH_EAST), "macula_add_unbounded", new OpMaculaAdd(false));
    public static HexPattern MACULA_ADD_BOUNDED = register(HexPattern.fromAngles("aaqdwdwde", HexDir.NORTH_EAST), "macula_add_bounded", new OpMaculaAdd(true));
    public static HexPattern MACULA_CLEAR = register(HexPattern.fromAngles("awawa", HexDir.WEST), "macula_clear", new OpMaculaClear());
    public static HexPattern MACULA_DIMENSIONS = register(HexPattern.fromAngles("aawawaa", HexDir.NORTH_EAST), "macula_dimensions", new OpMaculaDimensions());

    public static void init() {

        try {
            for (Triple<HexPattern, Identifier, Action> patternTriple : PATTERNS) {
                PatternRegistry.mapPattern(patternTriple.getFirst(), patternTriple.getSecond(), patternTriple.getThird());
            }
            for (Triple<HexPattern, Identifier, Action> patternTriple : PER_WORLD_PATTERNS) {
                PatternRegistry.mapPattern(patternTriple.getFirst(), patternTriple.getSecond(), patternTriple.getThird(), true);
            }
        } catch (PatternRegistry.RegisterPatternException e) {
            e.printStackTrace();
        }
    }

    private static HexPattern register(HexPattern pattern, String name, Action action) {
        Triple<HexPattern, Identifier, Action> triple = new Triple<>(pattern, id(name), action);
        PATTERNS.add(triple);
        return pattern;
    }

    private static HexPattern registerPerWorld(HexPattern pattern, String name, Action action) {
        Triple<HexPattern, Identifier, Action> triple = new Triple<>(pattern, id(name), action);
        PER_WORLD_PATTERNS.add(triple);
        return pattern;
    }
}
