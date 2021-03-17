package com.api.yirang.common.configure;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
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
        basePackages = {
                "com.api.yirang.auth.repository.persistence.maria",
                "com.api.yirang.notices.repository.persistence.maria",
                "com.api.yirang.seniors.repository.persistence.maria",
                "com.api.yirang.apply.repository.persistence.maria",
                "com.api.yirang.email.repository",
                "com.api.yirang.img.repository"
        }
)
@PropertySource("classpath:properties/application-db.properties")
@Profile("dev")
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
        System.out.println("DEV 모드로 DataBaseSource를 실행합니다.");
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USER_NAME, PASSWORD);
        dataSource.setDriverClassName(DRIVER);
        return dataSource;
    }

    @Primary
    @Bean(name = "mariaEntityManager")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(mariaDataSource());
        em.setPackagesToScan(LIST_OF_MODELS.toArray(new String[LIST_OF_MODELS.size()]));
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
