package io.github.artynova.mediaworks.client.macula;

import io.github.artynova.mediaworks.macula.Macula;
import io.github.artynova.mediaworks.macula.MaculaSerializer;
import io.github.artynova.mediaworks.macula.Visage;
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
        System.out.println("try render");
        assert CLIENT.world != null;
        preparedRenderers.forEach(renderer -> renderer.render(matrixStack));
        preparedRenderers.removeIf(VisageRenderer.Prepared::doneDisplaying);
    }

    public static void syncFromServer(NbtCompound maculaCompound) {
        assert CLIENT.world != null;
        System.out.println("compound:");
        System.out.println(maculaCompound);
        Macula macula = MaculaSerializer.getMacula(maculaCompound, CLIENT.world);
        System.out.println("decoded macula");
        System.out.println(macula);
        setMacula(macula);
    }

    /**
     * Converts the Macula into an efficient rendering-ready list.
     */
    public static void setMacula(Macula macula) {
        List<VisageRenderer.Prepared<?>> newList = new ArrayList<>();
        macula.forEach(visage -> newList.add(VisageRendererLoader.getRenderer(visage).prepare(visage)));
        preparedRenderers = newList;
    }

    public static void handleQuit(PlayerEntity player) {
        preparedRenderers = new ArrayList<>();
    }
}
