package co.istad.reporting;

import co.istad.core.dto.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import javax.sql.DataSource;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
@SpringBootApplication
public class ReportingSystemApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ReportingSystemApplication.class, args);

        AuthResponse authResponse = new AuthResponse(
                "Bearer",
                "Access Token",
                "Refresh Token"
        );

    }

    @Autowired
    @Qualifier("primaryDataSource")
    private DataSource primaryDataSource;

    @Autowired
    @Qualifier("secondaryDataSource")
    private DataSource secondaryDataSource;


    @Override
    public void run(String... args) throws Exception {
        System.out.println(primaryDataSource.hashCode());
        System.out.println(secondaryDataSource.hashCode());
    }
}
