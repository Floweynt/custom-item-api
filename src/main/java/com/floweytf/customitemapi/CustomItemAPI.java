package com.floweytf.customitemapi;

import com.floweytf.customitemapi.api.Version;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CustomItemAPI {
    public static final String MOD_ID = "customitemapi";
    public static final Version API_VERSION = Version.from("1.0.0");
    public static final Logger LOGGER = LogManager.getLogger("CustomItemAPI/" + API_VERSION);
}
