package readProperties;

import java.io.IOException;
import org.junit.jupiter.api.Test;


public class PropertiesTest {
    /**
     * Чтение пропертей из файла
     */
    @Test
    public void readProperties() throws IOException {
        System.getProperties().load(ClassLoader.getSystemResourceAsStream("application.properties"));
        String urlFromProperty = System.getProperty("url");
        System.out.println(urlFromProperty);
    }

    /**
     * Чтение conf файла
     */
    @Test
    public void readFromConf(){
        String urlFromConf = ConfigProvider.URL;
        System.out.println(urlFromConf);
        Boolean isDemoAdmin = ConfigProvider.IS_DEMO_ADMIN;
        System.out.println(isDemoAdmin);

        if(ConfigProvider.readConfig().getBoolean("usersParams.admin.isAdmin")){
            System.out.println("Админ действительно админ");
        } else {
            System.out.println("12");
        }
    }
}
