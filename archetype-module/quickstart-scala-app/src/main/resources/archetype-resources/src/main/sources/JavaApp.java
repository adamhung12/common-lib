package $package;

import me.xethh.utils.dateManipulation.BaseTimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.TimeZone;
/**
 * Hello world!
 *
 */
@SpringBootApplication
public class JavaApp
{
    public static void systemInit(){
        if(System.getProperty("base.log.path")==null){
            File file = new File("./target/test-app/logs/");
            file.mkdirs();
            System.setProperty("base.log.path",file.toString());
        }
    }
    public static void main( String[] args )
    {
        systemInit();
        TimeZone.setDefault(BaseTimeZone.Hongkong.timeZone());
        SpringApplication.run(JavaApp.class, args);
    }
}
