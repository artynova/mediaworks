package io.github.artynova.mediaworks.client.macula;

import io.github.artynova.mediaworks.logic.macula.MaculaContent;
import io.github.artynova.mediaworks.logic.macula.MaculaSerializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class MaculaClient {
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static List<VisageRenderer.Prepared<?>> preparedRenderers;

    public static void render(MatrixStack matrixStack) {
        assert CLIENT.world != null;
        preparedRenderers.forEach(renderer -> renderer.render(matrixStack));
        preparedRenderers.removeIf(VisageRenderer.Prepared::doneDisplaying);
    }

    public static void syncFromServer(NbtCompound maculaCompound) {
        assert CLIENT.world != null;
        MaculaContent maculaContent = MaculaSerializer.getContent(maculaCompound, CLIENT.world);
        setVisages(maculaContent);
    }

    /**
     * Converts the Macula content into an efficient rendering-ready list and stores the list.
     */
    public static void setVisages(MaculaContent maculaContent) {
        List<VisageRenderer.Prepared<?>> newList = new ArrayList<>();
        maculaContent.forEach(visage -> newList.add(VisageRendererLoader.getRenderer(visage).prepare(visage)));
        preparedRenderers = newList;
    }

    /**
     * Clears the rendering list.
     */
    public static void handleQuit(PlayerEntity player) {
        preparedRenderers = new ArrayList<>();
    }
}
