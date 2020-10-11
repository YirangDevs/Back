package com.api.yirang.common.configure;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "h2EntityManager",
        transactionManagerRef = "h2TransactionManager",
        basePackages = {"com.api.yirang.auth.repository.persistence.h2"}
)
@PropertySource("classpath:properties/application-db.properties")
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

    @Bean
    public DataSource h2DataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USER_NAME, PASSWORD);
        dataSource.setDriverClassName(DRIVER);
        return dataSource;
    }

    @Bean(name = "h2EntityManager")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(h2DataSource());
        em.setPackagesToScan(new String[] {"com.api.yirang.auth.domain.kakaoToken.model"});
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
