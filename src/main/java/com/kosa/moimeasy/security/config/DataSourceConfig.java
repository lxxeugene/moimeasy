//package com.kosa.moimeasy.security.config;
//
//
//import lombok.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.stereotype.Component;
//
//import javax.sql.DataSource;
//
//@Component
//public class DataSourceConfig {
//    @Value("${db.url}")
//    private String url;
//
//    @Value("${db.username}")
//    private String username;
//
//    @Value("${db.password}")
//    private String password;
//
//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//            dataSource.setUrl(url);
//            dataSource.setUsername(username);
//            dataSource.setPassword(password);
//            return dataSource;
//    }
//}
