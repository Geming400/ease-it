package fr.geming400.easeit.client.debug;

import fr.geming400.easeit.client.Utils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.AbstractTextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.CachedMapper;
import net.minecraft.util.Util;
import net.minecraft.util.math.ColorHelper;

import java.util.Objects;
import java.util.OptionalInt;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class AlphaMultilineTextWidget extends AbstractTextWidget {
    private OptionalInt maxWidth;
    private OptionalInt maxRows;
    private final CachedMapper<CacheKey, MultilineText> cacheKeyToText;
    private boolean centered;

    public AlphaMultilineTextWidget(Text message, TextRenderer textRenderer) {
        this(0, 0, message, textRenderer);
    }

    public AlphaMultilineTextWidget(int x, int y, Text message, TextRenderer textRenderer) {
        super(x, y, 0, 0, message, textRenderer);
        this.maxWidth = OptionalInt.empty();
        this.maxRows = OptionalInt.empty();
        this.centered = false;
        this.active = false;
        this.cacheKeyToText = Util.cachedMapper(cacheKey ->
                cacheKey.maxRows.isPresent()
                        ? MultilineText.create(textRenderer, cacheKey.maxWidth, cacheKey.maxRows.getAsInt(), cacheKey.message)
                        : MultilineText.create(textRenderer, cacheKey.message, cacheKey.maxWidth)
        );
    }

    public AlphaMultilineTextWidget setTextColor(int i) {
        super.setTextColor(i);
        return this;
    }

    public AlphaMultilineTextWidget setMaxWidth(int maxWidth) {
        this.maxWidth = OptionalInt.of(maxWidth);
        return this;
    }

    public AlphaMultilineTextWidget setMaxRows(int maxRows) {
        this.maxRows = OptionalInt.of(maxRows);
        return this;
    }

    public AlphaMultilineTextWidget noMaxRows() {
        this.maxRows = OptionalInt.empty();
        return this;
    }

    public AlphaMultilineTextWidget setCentered(boolean centered) {
        this.centered = centered;
        return this;
    }

    public int getWidth() {
        return this.cacheKeyToText.map(this.getCacheKey()).getMaxWidth();
    }

    public int getHeight() {
        int var10000 = this.cacheKeyToText.map(this.getCacheKey()).count();
        Objects.requireNonNull(this.getTextRenderer());

        return var10000 * 9;
    }

    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        Objects.requireNonNull(this.getTextRenderer());
        MultilineText multilineText = this.cacheKeyToText.map(this.getCacheKey());
        int x = this.getX();
        int y = this.getY();

        int lineHeight = 9;
        int textAlpha = ColorHelper.Argb.getAlpha(this.getTextColor());
        int color = Utils.withFixedAlpha((int) (textAlpha * this.alpha), this.getTextColor());

        if (this.centered) {
            multilineText.drawCenterWithShadow(context, x + this.getWidth() / 2, y, lineHeight, color);
        } else {
            multilineText.drawWithShadow(context, x, y, lineHeight, color);
        }

    }

    @Override
    public void setWidth(int width) {
        this.setMaxWidth(width);
    }

    @Override
    public void setHeight(int height) {
        this.setMaxRows(height / 9);
    }

    @Override
    public void setDimensions(int width, int height) {
        this.setWidth(width);
        this.setHeight(height);
    }

    public float getAlpha() {
        return this.alpha;
    }

    public int getIntAlpha() {
        return (int) (this.alpha * 255);
    }

    private CacheKey getCacheKey() {
        return new CacheKey(this.getMessage(), this.maxWidth.orElse(Integer.MAX_VALUE), this.maxRows);
    }

    @Environment(EnvType.CLIENT)
    record CacheKey(Text message, int maxWidth, OptionalInt maxRows) {}
}
