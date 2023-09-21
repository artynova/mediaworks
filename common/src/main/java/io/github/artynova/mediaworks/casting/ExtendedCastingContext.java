package io.github.artynova.mediaworks.casting;

import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface ExtendedCastingContext {
    @Nullable ItemStack mediaworks$getForcedCastingStack();

    void mediaworks$setForcedCastingStack(@Nullable ItemStack itemStack);
}
