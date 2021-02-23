package com.api.yirang.common.configure;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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
        entityManagerFactoryRef = "h2EntityManager",
        transactionManagerRef = "h2TransactionManager",
        basePackages = { "com.api.yirang.auth.repository.persistence.maria",
                         "com.api.yirang.notices.repository.persistence.maria",
                         "com.api.yirang.seniors.repository.persistence.maria",
                         "com.api.yirang.apply.repository.persistence.maria",
                         "com.api.yirang.email.repository"}
)
@PropertySource("classpath:properties/application-test.properties")
@Profile("test")
public class H2DataBaseConfig {

    @Value("${spring.h2.datasource.driver-class-name}")
    private String DRIVER;

    @Value("${spring.h2.datasource.url}")
    private String URL;

    @Value("${spring.h2.datasource.username}")
    private String USER_NAME;

    @Value("${spring.h2.datasource.password}")
    private String PASSWORD;

    @Value("${spring.h2.hibernate.hbm2ddl.auto}")
    private String HDDL;

    @Value("${spring.h2.hibernate.dialect}")
    private String DIALECT;

    @Value("#{${spring.h2.datasource.models}}")
    private List<String> LIST_OF_MODELS;

    @Bean
    public DataSource h2DataSource(){
        System.out.println("TEST 모드로 DataBaseSource를 실행합니다.");
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USER_NAME, PASSWORD);
        dataSource.setDriverClassName(DRIVER);
        return dataSource;
    }

    @Bean(name = "h2EntityManager")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(h2DataSource());
        em.setPackagesToScan(LIST_OF_MODELS.toArray(new String[LIST_OF_MODELS.size()]));
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(hibernateProperties());
        return em;
    }

    @Bean(name= "h2TransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("h2EntityManager") EntityManagerFactory entityManagerFactory){
        return new JpaTransactionManager(entityManagerFactory);
    }

    Properties hibernateProperties(){
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", HDDL);
        properties.setProperty("hibernate.dialect", DIALECT);
        return properties;
    }
}
