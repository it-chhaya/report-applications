package co.istad.reporting.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "co.istad.reporting.core",
        entityManagerFactoryRef = "primaryEntityManagerFactory",
        transactionManagerRef = "primaryTransactionManager"
)
public class PrimaryDatasourceConfig {

    // Bean of Datasource
    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.primary-datasource")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    // Bean of Entity Manager
    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(EntityManagerFactoryBuilder primaryBuilder,
                                                                              DataSource primaryDataSource) {

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        //properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

        return primaryBuilder
                .dataSource(primaryDataSource)
                .packages("co.istad.reporting.domain.primary")
                .properties(properties)
                .persistenceUnit("primary")
                .build();
    }

    // Bean of Transaction Manager
    @Primary
    @Bean
    public PlatformTransactionManager primaryTransactionManager(LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory) {
        return new JpaTransactionManager(primaryEntityManagerFactory.getObject());
    }

}
