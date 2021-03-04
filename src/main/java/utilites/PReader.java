package utilites;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PReader {

    public static String read(String prop) {
        final String config = "config.properties";
        try (InputStream is = PReader.class.getClassLoader().getResourceAsStream(config)){
            final Properties property = new Properties();
            property.load(is);
            return property.getProperty(prop);
        } catch (IOException e) {
            e.printStackTrace();
            return "Can not read property " + prop;
        }
    }
}
