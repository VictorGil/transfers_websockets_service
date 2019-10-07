package net.devaction.kafka.transferswebsocketsservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.devaction.util.JsonDeserializer;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class ConfigReader {
    private static final Logger log = LoggerFactory.getLogger(ConfigReader.class);

    // This file must be present in the classpath
    private static final String CONFIG_FILE = "config.json";

    private final JsonDeserializer deserializer = new JsonDeserializer();

    public ConfigValues read() throws Exception {
        ConfigValues config = deserializer.deserializeFromFile(
                CONFIG_FILE, ConfigValues.class);

        log.info("Application configuration: {}", config);
        return config;
    }
}
