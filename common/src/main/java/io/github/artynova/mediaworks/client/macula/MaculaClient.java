package io.github.artynova.mediaworks.client.macula;

import io.github.artynova.mediaworks.logic.macula.MaculaContent;
import io.github.artynova.mediaworks.logic.macula.MaculaSerializer;
import io.github.artynova.mediaworks.networking.MediaworksNetworking;
import io.github.artynova.mediaworks.networking.macula.SyncMaculaDimensionsC2SMsg;
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
    private static List<VisageRenderer.Prepared> preparedRenderers;

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
        maculaContent.sortByDepth();
        List<VisageRenderer.Prepared> newList = new ArrayList<>();
        maculaContent.forEach(entry -> newList.add(VisageRendererLoader.getRenderer(entry.getVisage()).prepare(entry)));
        preparedRenderers = newList;
    }

    public static void sendDimensions() {
        MediaworksNetworking.sendToServer(new SyncMaculaDimensionsC2SMsg(CLIENT.getWindow().getScaledWidth(), CLIENT.getWindow().getScaledHeight()));
    }

    /**
     * Sends initial macula dimensions.
     */
    public static void handleJoin(PlayerEntity player) {
        sendDimensions();
    }

    /**
     * Clears the rendering list.
     */
    public static void handleQuit(PlayerEntity player) {
        preparedRenderers = new ArrayList<>();
    }
}
