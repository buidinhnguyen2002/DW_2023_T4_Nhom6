package getConnection;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
public class ConfigReader {
     private Properties prop;
 public enum ConfigurationProperty {
         URL("url"),
         USERNAME("username"),
         PASSWORD("password"),
         DRIVER_CLASS_NAME("driverClassName"),
         MODULE_LOAD_FILE("Module.LoadToFile"),
         MODULE_PROCESS("Module.Process"),
         MODULE_SUCCESS("Module.Success");

         private final String propertyName;

         ConfigurationProperty(String propertyName) {
             this.propertyName = propertyName;
         }

         public String getPropertyName() {
             return propertyName;
         }
     }
    public ConfigReader() {
        prop = new Properties();
        try {
            InputStream input = new FileInputStream("Load_from_Source_To_File/src/main/java/getConnection/Config.properties");
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getProperty(String propertyName) {
        return prop.getProperty(propertyName);
    }

    public static void main(String[] args) {
        String propertyName = ConfigurationProperty.URL.getPropertyName();
        System.out.println("Property name: " + propertyName);
    }
}