package Meowtro.Game;

import java.util.Properties;
import java.util.Set;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Config {

    String config_path = null;
    private Properties properties = new Properties();

    public Config(String config_path) {
        try {
            // load .properties
            FileInputStream in = new FileInputStream(config_path);
            this.properties.load(in);
            in.close();
            // store config_path for writing back
            this.config_path = config_path;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String get(String key) {
        return this.properties.getProperty(key);
    }
    
    public void set(String key, String value) {
        this.properties.setProperty(key, value);
        try {
            FileOutputStream out = new FileOutputStream(this.config_path);
            this.properties.store(out, null);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Set<String> getAllKeys() {
        return this.properties.stringPropertyNames();
    }
    
    /****** MAIN ******/
    public static void main(String[] args) {
        Config config = new Config("./config.properties");
        System.out.println(config.get("mountain.r") + ", " + config.get("mountain.g") + ", " + config.get("mountain.b"));
        config.set("mountain.r", "19");
        System.out.println(config.get("mountain.r") + ", " + config.get("mountain.g") + ", " + config.get("mountain.b"));
    }
}
