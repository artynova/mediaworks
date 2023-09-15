package io.github.artynova.mediaworks.logic.macula;

import at.petrak.hexcasting.api.spell.iota.Iota;
import io.github.artynova.mediaworks.interop.moreiotas.MoreIotasInterop;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TextVisage extends Visage {
    public static final int UNLIMITED_SIZE = -1;
    public static final String TEXT_TAG = "text";
    public static final String WIDTH_TAG = "width";
    public static final String HEIGHT_TAG = "height";
    public static VisageType<TextVisage> TYPE = new VisageType<>() {
        @Override
        public TextVisage deserializeData(NbtCompound compound) {
            Text text = Text.Serializer.fromJson(compound.getString(TEXT_TAG));
            int width = compound.getInt(WIDTH_TAG);
            int height = compound.getInt(HEIGHT_TAG);
            if (width == -1 || height == -1) return new TextVisage(text);
            return new TextVisage(text, width, height);
        }

        @Override
        public @NotNull NbtCompound serializeData(TextVisage visage) {
            NbtCompound compound = new NbtCompound();
            compound.put(TEXT_TAG, NbtString.of(Text.Serializer.toJson(visage.getText())));
            compound.put(WIDTH_TAG, NbtInt.of(visage.getWidth()));
            compound.put(HEIGHT_TAG, NbtInt.of(visage.getHeight()));
            return compound;
        }
    };

    private final Text text;
    private final int width;
    private final int height;

    /**
     * Constructs a bounded text visage.
     * For an unbounded instance, use {@link #TextVisage(Text)}
     */
    public TextVisage(Text text, int width, int height) {
        super(TYPE);
        this.text = text;
        if (width < 0) {
            throw new IllegalArgumentException("Illegal TextVisage width: " + width);
        }
        this.width = width;
        if (height < 0) {
            throw new IllegalArgumentException("Illegal TextVisage height: " + height);
        }
        this.height = height;
    }

    public TextVisage(Text text) {
        super(TYPE);
        this.text = text;
        this.width = UNLIMITED_SIZE;
        this.height = UNLIMITED_SIZE;
    }

    public static Text captureText(Iota iota) {
        if (MoreIotasInterop.isPresent() && MoreIotasInterop.isStringIota(iota)) {
            return MoreIotasInterop.captureStringIota(iota);
        }
        return iota.display();
    }

    public Text getText() {
        return text;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public MutableText displayOnStack() {
        if (getWidth() == -1) return Text.translatable("mediaworks.visage.text.unbounded");
        else return Text.translatable("mediaworks.visage.text.bounded", getWidth(), getHeight());
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        TextVisage visage = (TextVisage) other;
        return getWidth() == visage.getWidth() && getHeight() == visage.getHeight() && Objects.equals(getText(), visage.getText());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getText(), getWidth(), getHeight());
    }
}
