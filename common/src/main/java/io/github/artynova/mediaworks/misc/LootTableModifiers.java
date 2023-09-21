package io.github.artynova.mediaworks.misc;

import dev.architectury.event.events.common.LootEvent;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.util.Identifier;

import static io.github.artynova.mediaworks.Mediaworks.id;

public class LootTableModifiers {
    public static final Identifier ANCIENT_CITY_ADDITIONS = id("chests/ancient_city_additions");
    public static final int ANCIENT_CITY_ADDITIONS_WEIGHT = 14;

    public static void injectCloakLoot(LootManager manager, Identifier id, LootEvent.LootTableModificationContext context, boolean builtin) {
        if (!builtin) return;
        if (!LootTables.ANCIENT_CITY_CHEST.equals(id)) return;
        LootPoolEntry.Builder<?> entryBuilder = LootTableEntry.builder(ANCIENT_CITY_ADDITIONS).weight(ANCIENT_CITY_ADDITIONS_WEIGHT);
        context.addPool(LootPool.builder().with(entryBuilder));
    }
}
