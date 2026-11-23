package fr.geming400.easeit.client.mixin;

import fr.geming400.easeit.client.animation.Animatable;
import fr.geming400.easeit.client.animation.Animation;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * This mixin is used for the interface injection of {@link Animatable}
 */
@Mixin(Screen.class)
abstract class ScreenMixin implements Drawable, Animatable {
    @Unique
    private final Deque<Animation<?>> animationsQueue = new ArrayDeque<>();

    @Unique
    private Animation<?> currentAnimation = null;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Drawable;render(Lnet/minecraft/client/gui/DrawContext;IIF)V"), method = "render")
    private void render(DrawContext context, int mouseX, int mouseY, float deltaTicks, CallbackInfo ci) {
        this.easeit$tickWidgetAnimations();
    }

    @Override
    public void easeit$setCurrentAnimation(Animation<?> currentAnimation) {
        this.currentAnimation = currentAnimation;
    }

    @Override
    @Nullable
    public Animation<?> easeit$getCurrentAnimation() {
        return this.currentAnimation;
    }

    @Override
    public Deque<Animation<?>> easeit$getAnimationsQueue() {
        return this.animationsQueue;
    }
}
