package fr.geming400.easeit.client;

import fr.geming400.easeit.client.debug.EaseItTestScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Util;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaiterEaseItPleaseClient implements ClientModInitializer {
	public static final String MOD_ID = "ease-it";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final boolean TEST_SCREEN_ALLOWED = FabricLoader.getInstance().isDevelopmentEnvironment() || Boolean.parseBoolean(System.getProperty("EASE_IT_TEST_SCREEN_ALLOWED"));
	@Nullable
	public static final KeyBinding OPEN_TEST_SCREEN_KEYBIND;

	static {
		if (TEST_SCREEN_ALLOWED) {
			OPEN_TEST_SCREEN_KEYBIND = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.ease-it.debug-screen", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_O, KeyBinding.MISC_CATEGORY));
		} else {
			OPEN_TEST_SCREEN_KEYBIND = null;
		}
	}

	private static final String[] COOL_QUOTES = new String[] {
			"Easing my easing rn",
			"Hi",
			"Scientists discover the hi",
			"Waiter, ease it please !",
			"This mod was vibecoded\n- Claude but it's a human because I HATE vibecoding",
			"Did you know that I made up this mod's name in 10 seconds",
			"Initializing, please wait...",
			"Generating mod's code, please wait...",
			"Also try Minecraft !",
			"Made in France",
			"LOGGER.info(Util.getRandom(COOL_QUOTES, Random.create()));",
			"I'm easing it",
			// Me when the shameful self promotion
			"Also try Mineciv !",
			"Also try Pesticides !",
			"Also try Screw You 4 !",
			"Also try Accurate Skies !",
			"Zeeeeesto™️"
	};

	@Override
	public void onInitializeClient() {
		LOGGER.info(Util.getRandom(COOL_QUOTES, Random.create()));

		ClientTickEvents.START_CLIENT_TICK.register(client -> {
			boolean isKeybindPressed = OPEN_TEST_SCREEN_KEYBIND != null && OPEN_TEST_SCREEN_KEYBIND.isPressed();
			if (client.currentScreen == null && isKeybindPressed) {
				client.setScreen(new EaseItTestScreen());
			}
		});
	}
}