package joelbits.plugins;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public class Settings {
    private static final Logger log = LoggerFactory.getLogger(Settings.class);
    private final Properties properties = new Properties();
    private static final int DEFAULT_NUMBER_THREADS = 10;

    public Settings() {
        try {
            properties.load(this.getClass().getResourceAsStream("/config.properties"));
        } catch (IOException e) {
            log.error(e.toString(), e);
        }
    }

    public int numberOfThreads() {
        String threads = properties.getProperty("threads");
        return StringUtils.isEmpty(threads) ? DEFAULT_NUMBER_THREADS : Integer.parseInt(threads);
    }
}
