package fr.geming400.easeit.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.Util;
import net.minecraft.util.math.random.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaiterEaseItPleaseClient implements ClientModInitializer {
	public static final String MOD_ID = "ease-it";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

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
	}
}