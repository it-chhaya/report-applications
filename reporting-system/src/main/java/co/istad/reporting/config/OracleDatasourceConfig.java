package co.istad.reporting.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "co.istad.reporting.order",
        entityManagerFactoryRef = "oracleEntityManagerFactory",
        transactionManagerRef = "oracleTransactionManager"
)
public class OracleDatasourceConfig {

    // Bean of Datasource
    @Bean(name = "oracleDataSource")
    @ConfigurationProperties(prefix = "spring.oracle-datasource")
    public DataSource oracleDataSource() {
        return DataSourceBuilder.create().build();
    }

    // Bean of Entity Manager
    @Bean(name = "oracleEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean oracleEntityManagerFactory(EntityManagerFactoryBuilder oracleBuilder,
                                                                              @Qualifier("oracleDataSource") DataSource oracleDataSource) {

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "none");
        //properties.put("hibernate.dialect", "org.hibernate.dialect.OracleDialect");

        return oracleBuilder
                .dataSource(oracleDataSource)
                .packages("co.istad.reporting.domain.oracle")
                .properties(properties)
                .persistenceUnit("oracle")
                .build();
    }

    // Bean of Transaction Manager
    @Bean
    public PlatformTransactionManager oracleTransactionManager(@Qualifier("oracleEntityManagerFactory") LocalContainerEntityManagerFactoryBean oracleEntityManagerFactory) {
        return new JpaTransactionManager(oracleEntityManagerFactory.getObject());
    }

}
