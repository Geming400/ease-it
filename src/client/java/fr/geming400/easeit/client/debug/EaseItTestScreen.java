package fr.geming400.easeit.client.debug;

import fr.geming400.easeit.client.WaiterEaseItPleaseClient;
import fr.geming400.easeit.client.animation.Action;
import fr.geming400.easeit.client.animation.WidgetAnimation;
import fr.geming400.easeit.client.animation.easing.Easing;
import fr.geming400.easeit.client.animation.easing.EasingType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.util.Window;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;
import org.joml.Vector2ic;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class EaseItTestScreen extends Screen {
    public static final Random RANDOM = Random.create();

    private static final int BUTTON_WIDTH = 60;
    private static final int BUTTON_HEIGHT = 15;

    private static final int END_BORDER_COLOR = 0xCCFF0000;
    private static final int START_BORDER_COLOR = 0xCC00FF00;
    private static final int PREVIEW_LINES_COLOR = 0x50FFFFFF;

    private static final String END_BORDER_NAME = "End";
    private static final String START_BORDER_NAME = "Start";

    public static final List<TestType<?>> TESTS = new ArrayList<>();

    static {
        registerTest("Move Test", screen -> {
            AlphaMultilineTextWidget testWidget = screen.testWidget;

            Window window = MinecraftClient.getInstance().getWindow();

            Vector2i endPos = new Vector2i(RANDOM.nextBetween(0, window.getScaledWidth() - testWidget.getWidth()), RANDOM.nextBetween(0, window.getScaledHeight() - testWidget.getHeight()));
            screen.startBorder = Border.fromWidget(testWidget, START_BORDER_COLOR, START_BORDER_NAME);
            screen.endBorder = new Border(endPos.x, endPos.y, testWidget.getWidth(), testWidget.getHeight(), END_BORDER_COLOR, END_BORDER_NAME);

            return testWidget.easeit$addAnimation(WidgetAnimation.ofInt(
                    testWidget,
                    Easing.EXPONENTIAL, EasingType.OUT,
                    20 * 5,
                    Action.createMove(endPos)
            ));
        });

        registerTest("Relative Move Test", screen -> {
            AlphaMultilineTextWidget testWidget = screen.testWidget;

            Vector2i relativeEndPos = Util.getRandom(
                    List.of(
                            new Vector2i(0, testWidget.getHeight()), new Vector2i(0, -testWidget.getHeight()),
                            new Vector2i(testWidget.getWidth(), 0), new Vector2i(-testWidget.getWidth(), 0)
                    ),
                    RANDOM
            );

            Vector2i absoluteEndPos = new Vector2i(testWidget.getX(), testWidget.getY()).add(relativeEndPos);
            screen.startBorder = Border.fromWidget(testWidget, START_BORDER_COLOR, START_BORDER_NAME);
            screen.endBorder = new Border(absoluteEndPos.x, absoluteEndPos.y, testWidget.getWidth(), testWidget.getHeight(), END_BORDER_COLOR, END_BORDER_NAME);

            return testWidget.easeit$addAnimation(WidgetAnimation.ofInt(
                    testWidget,
                    Easing.EXPONENTIAL, EasingType.OUT,
                    20 * 5,
                    Action.createRelativeMove(relativeEndPos)
            ));
        });

        registerTest("Alpha Test", screen -> {
            AlphaMultilineTextWidget testWidget = screen.testWidget;

            float endAlpha = RANDOM.nextFloat();
//            screen.startBorder = Border.fromWidget(testWidget, ColorHelper.Argb.withAlpha(testWidget.getIntAlpha(), START_BORDER_COLOR), START_BORDER_NAME);
            screen.startBorder = null;
            screen.endBorder = Border.fromWidget(testWidget, ColorHelper.Argb.withAlpha((int) (endAlpha * 255), END_BORDER_COLOR), END_BORDER_NAME);
            screen.showWidgetBorder = false;

            return testWidget.easeit$addAnimation(WidgetAnimation.ofFloat(
                    testWidget,
                    Easing.EXPONENTIAL, EasingType.OUT,
                    20 * 5,
                    Action.createAlpha(endAlpha)
            ));
        });

        registerTest("Scale Test", screen -> {
            AlphaMultilineTextWidget testWidget = screen.testWidget;

            Window window = MinecraftClient.getInstance().getWindow();

            Vector2i endScale = new Vector2i(RANDOM.nextBetween(0, window.getScaledWidth() - testWidget.getWidth()), RANDOM.nextBetween(0, window.getScaledHeight() - testWidget.getHeight()));
            screen.startBorder = Border.fromWidget(testWidget, START_BORDER_COLOR, START_BORDER_NAME);
            screen.endBorder = new Border(testWidget.getX(), testWidget.getY(), endScale.x, endScale.y, END_BORDER_COLOR, END_BORDER_NAME);

            return testWidget.easeit$addAnimation(WidgetAnimation.ofInt(
                    testWidget,
                    Easing.EXPONENTIAL, EasingType.OUT,
                    20 * 5,
                    Action.createResize(endScale.x, endScale.y)
            ));
        });
    }

    /// The main widget that is modified for tests
    public AlphaMultilineTextWidget testWidget;
    /// The <b>green</b> "start" border
    public Border startBorder = null;
    /// The <b>red</b> "end" border
    public Border endBorder = null;
    /// If the transparent border around the {@link #testWidget} should get shown
    public boolean showWidgetBorder = true;

    public EaseItTestScreen() {
        super(Text.literal("Ease It Debug Screen"));
    }

    public void resetBorders() {
        this.startBorder = null;
        this.endBorder = null;
        this.showWidgetBorder = true;
    }

    public void resetStates() {
        this.resetBorders();

        Window window = MinecraftClient.getInstance().getWindow();
        this.testWidget.visible = true;
        this.testWidget.setAlpha(1);
        this.testWidget.setTextColor(0xFFFFFFFF);
        this.testWidget.setCentered(true);
        this.testWidget.setMaxWidth(115);
        this.testWidget.noMaxRows();

        this.testWidget.setPosition(window.getScaledWidth() / 2 - this.testWidget.getWidth() / 2, window.getScaledHeight() / 2 - this.testWidget.getHeight() / 2);
    }

    @Override
    protected void init() {
        super.init();

        WaiterEaseItPleaseClient.LOGGER.info("Initializing TestScreen");

        TestType.BUTTON_Y = 10;

        this.testWidget = new AlphaMultilineTextWidget(Text.literal("Hi this is your waiter waiting to ease it ! Hi hope you liked my amazing joke"), this.textRenderer)
                .setCentered(true)
                .setMaxWidth(115);

        this.addDrawableChild(this.testWidget);
        this.resetStates();

        this.addDrawableChild(
                createButtonBuilder(
                        TestType.BUTTON_Y,
                        Text.literal("Reset"),
                        widget -> this.resetStates()
                ).build()
        );

        TestType.BUTTON_Y += TestType.BUTTON_PADDING + BUTTON_HEIGHT;

        TESTS.forEach(testType ->
            testType.addButton(this, this::addDrawableChild)
        );
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        if (this.startBorder != null)
            this.startBorder.draw(context, this.textRenderer);

        if (this.endBorder != null)
            this.endBorder.draw(context, this.textRenderer);

        if (this.showWidgetBorder)
            context.drawBorder(this.testWidget.getX(), this.testWidget.getY(), this.testWidget.getWidth(), this.testWidget.getHeight(), PREVIEW_LINES_COLOR);

//        if (this.currentEndTestPreviewPos != null)

//            this.drawBow(context, this.currentEndTestPreviewPos);
    }

    private static ButtonWidget.Builder createButtonBuilder(int y, Text message, ButtonWidget.PressAction onPress) {
        return ButtonWidget.builder(message, onPress).dimensions(10, y, BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    /**
     * Registers a test for the "Ease It" {@linkplain EaseItTestScreen Test Screen}
     * @param name the name of the test. It's shown on the button
     * @param animationGetter the getter used to fetch the animation to run.
     *                        It's not cached and the getter is ran every time the button is clicked
     * @return the created {@link TestType}
     */
    public static <T extends Number> TestType<T> registerTest(String name, Function<EaseItTestScreen, WidgetAnimation<T>> animationGetter) {
        TestType<T> test = new TestType<>(name, animationGetter);
        TESTS.add(test);

        return test;
    }

    public static class TestType<T extends Number> {
        private static int BUTTON_Y = 10;
        private static final int BUTTON_PADDING = 3;

        private final String name;
        private final Function<EaseItTestScreen, WidgetAnimation<T>> animationGetter;

        private TestType(String name, Function<EaseItTestScreen, WidgetAnimation<T>> animationGetter) {
            this.name = name;
            this.animationGetter = animationGetter;
        }

        public WidgetAnimation<T> getAnimation(EaseItTestScreen screen) {
            WaiterEaseItPleaseClient.LOGGER.info("Running animation \"{}\" in TestScreen", this.name);
            return this.animationGetter.apply(screen);
        }

        public void run(EaseItTestScreen screen) {
            screen.resetBorders();
            screen.easeit$addAnimation(this.getAnimation(screen));
        }

        public ButtonWidget addButton(EaseItTestScreen screen, UnaryOperator<ButtonWidget> drawer) {
            var button = createButtonBuilder(
                    BUTTON_Y,
                    Text.literal(this.name),
                    widget -> this.run(screen)
            ).build();

            BUTTON_Y += BUTTON_PADDING + BUTTON_HEIGHT;

            return drawer.apply(button);
        }
    }

    public record Border(int x, int y, int width, int height, int color, @Nullable String text) {
        public void draw(DrawContext context, TextRenderer textRenderer) {
            if (this.text != null)
                context.drawText(textRenderer, this.text, this.x, this.y + 2 + this.height, ColorHelper.Argb.withAlpha(0, this.color), false);

            context.drawBorder(this.x, this.y, this.width, this.height, this.color);
        }

        public static Border fromVectors(Vector2ic pos, Vector2ic dimension, int color, @Nullable String text) {
            return new Border(pos.x(), pos.y(), dimension.x(), dimension.y(), color, text);
        }

        public static Border fromWidget(Widget widget, int color, @Nullable String text) {
            return new Border(widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight(), color, text);
        }
    }
}
