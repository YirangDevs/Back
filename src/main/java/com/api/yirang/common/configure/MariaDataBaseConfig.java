package com.api.yirang.common.configure;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "mariaEntityManager",
        transactionManagerRef = "mariaTransactionManager",
        basePackages = {"com.api.yirang.auth.repository.persistence.maria",
                        "com.api.yirang.common.repository.persistence.maria"
        }
)
@PropertySource("classpath:properties/application-db.properties")
public class MariaDataBaseConfig {

    @Value("${spring.mariadb.datasource.driver-class-name}")
    private String DRIVER;

    @Value("${spring.mariadb.datasource.url}")
    private String URL;

    @Value("${spring.mariadb.datasource.username}")
    private String USER_NAME;

    @Value("${spring.mariadb.datasource.password}")
    private String PASSWORD;

    @Value("${spring.mariadb.hibernate.hbm2ddl.auto}")
    private String HDDL;

    @Value("${spring.mariadb.hibernate.dialect}")
    private String DIALECT;

    @Value("#{${spring.mariadb.datasource.models}}")
    private List<String> LIST_OF_MODELS;

    @Primary
    @Bean
    public DataSource mariaDataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USER_NAME, PASSWORD);
        dataSource.setDriverClassName(DRIVER);
        return dataSource;
    }

    @Primary
    @Bean(name = "mariaEntityManager")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(mariaDataSource());
        em.setPackagesToScan("com.api.yirang.auth.domain.user.model",
                             "com.api.yirang.common.domain.region.model",
                             "com.api.yirang.notices.domain.activity.model",
                             "com.api.yirang.notices.domain.notice.model",
                             "com.api.yirang.seniors.domain.senior.model");
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(hibernateProperties());
        return em;
    }

    @Primary
    @Bean(name= "mariaTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("mariaEntityManager")EntityManagerFactory entityManagerFactory){
        return new JpaTransactionManager(entityManagerFactory);
    }

    Properties hibernateProperties(){
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", HDDL);
        properties.setProperty("hibernate.dialect", DIALECT);
        return properties;
    }

}
