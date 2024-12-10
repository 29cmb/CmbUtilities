package xyz.devcmb.cmbutilities.client.util;

import java.util.HashMap;
import java.util.Map;

public class ClientOptionManager {
    public static Map<ClientOptions, Object> options = new HashMap<>();

    public static void setDefaults(){
        options.put(ClientOptions.WORLD_FILTER_ENABLED, false);
    }

    public static Object getOption(ClientOptions option) {
        return options.get(option);
    }

    public static void setOption(ClientOptions option, Object value) {
        options.put(option, value);
    }
}
