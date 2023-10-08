
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.oubao*"})
@MapperScan("com.oubao.mapper")
@SpringBootApplication
@EnableDiscoveryClient
public class OubaoGameApplication {
    public static void main(String[] args) {
        SpringApplication.run(OubaoGameApplication.class, args);
    }
}
