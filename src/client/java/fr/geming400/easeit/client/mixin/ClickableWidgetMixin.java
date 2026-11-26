package fr.geming400.easeit.client.mixin;

import fr.geming400.easeit.client.animation.Animatable;
import fr.geming400.easeit.client.animation.WidgetAnimation;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.Widget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This mixin is used for the interface injection of {@link Animatable}
 */
@Mixin(ClickableWidget.class)
abstract class ClickableWidgetMixin implements Widget, Animatable<WidgetAnimation<?>> {
    @Unique
    private final List<WidgetAnimation<?>> playingAnimations = new CopyOnWriteArrayList<>();

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ClickableWidget;renderWidget(Lnet/minecraft/client/gui/DrawContext;IIF)V"), method = "render")
    private void render(DrawContext context, int mouseX, int mouseY, float deltaTicks, CallbackInfo ci) {
        this.easeit$tickWidgetAnimations();
    }

    @Override
    public List<WidgetAnimation<?>> easeit$getAnimations() {
        return this.playingAnimations;
    }
}
