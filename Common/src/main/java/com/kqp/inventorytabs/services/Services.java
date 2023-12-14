package com.kqp.inventorytabs.services;

import java.util.ServiceLoader;

public class Services {

    public static final PlatformHelper PLATFORM_HELPER = loadService(PlatformHelper.class);
    public static final ConfigHelper CONFIG_HELPER = loadService(ConfigHelper.class);

    private static <T> T loadService(Class<T> clazz) {
        return ServiceLoader.load(clazz).findFirst().orElseThrow();
    }
}
