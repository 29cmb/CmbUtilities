package xyz.devcmb.cmbutilities;

import net.fabricmc.api.ModInitializer;

import java.util.logging.Logger;

public class CmbUtilities implements ModInitializer {
    public static Logger LOGGER;
    @Override
    public void onInitialize() {
        LOGGER = Logger.getLogger("CmbUtilities");
    }
}
