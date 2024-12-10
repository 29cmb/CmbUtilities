package xyz.devcmb.cmbutilities.client;

import net.fabricmc.api.ClientModInitializer;
import xyz.devcmb.cmbutilities.client.util.ClientOptionManager;

public class CmbUtilitiesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientOptionManager.setDefaults();
    }
}
