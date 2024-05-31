package bgu.adss.fff.dev.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

public class SystemConfiguration {
    private static final String PATH = "static/configuration.json";
    private File jsonFile;

    private static void error() {
        throw new RuntimeException(
                "Server is struggling to find system resources, please contact support.");
    }

    /**
     * Loads the json config file.
     */
    private void loadConfig() {
        try {
            jsonFile = new ClassPathResource(PATH).getFile();
        } catch (IOException e) {
            error();
        }
    }

    /**
     * @return the cutoff time in hours.
     */
    public int getCutoffTime() {
        loadConfig();
        try {
            ConfigDetails config = new ObjectMapper().readValue(jsonFile, ConfigDetails.class);
            return config.cutoffTime;
        } catch (IOException e) {
            error();
        }
        return 0;
    }

    /**
     * Sets the cutoff time.
     * @param cutoff cutoff of time period in hours.
     */
    public void setCutoffTime(int cutoff) {
        loadConfig();
        try {
            new ObjectMapper().writeValue(jsonFile, new ConfigDetails(cutoff));
        } catch(IOException e) {
            error();
        }
    }

    private record ConfigDetails(@JsonProperty("cutoff-period") int cutoffTime) {}
}
