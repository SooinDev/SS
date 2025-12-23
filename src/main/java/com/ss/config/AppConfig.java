package com.ss.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource; // [추가]
import org.springframework.core.env.Environment; // [추가]
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "com.ss")
@MapperScan("com.ss.mapper")
@EnableTransactionManagement
@PropertySource("classpath:database.properties")
public class AppConfig {

    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.mariadb.jdbc.Driver");

        // 한글 인코딩 최적화: UTF-8 설정 + useUnicode 옵션
        String jdbcUrl = "jdbc:mariadb://localhost:3306/ss" +
                "?useUnicode=true" +
                "&characterEncoding=UTF-8" +
                "&connectionCollation=utf8mb4_unicode_ci" +
                "&serverTimezone=Asia/Seoul";

        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUsername(env.getProperty("db.id"));
        dataSource.setPassword(env.getProperty("db.pw"));

        // 커넥션 풀 설정
        dataSource.setMaximumPoolSize(10);
        dataSource.setMinimumIdle(2);

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:sql/*.xml")
        );
        sessionFactory.setTypeAliasesPackage("com.ss.vo");

        org.apache.ibatis.session.Configuration config = new org.apache.ibatis.session.Configuration();
        config.setMapUnderscoreToCamelCase(true);
        sessionFactory.setConfiguration(config);

        return sessionFactory.getObject();
    }
}