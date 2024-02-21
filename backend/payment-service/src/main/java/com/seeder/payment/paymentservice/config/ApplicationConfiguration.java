package com.seeder.payment.paymentservice.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class ApplicationConfiguration {
    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String jdbcUsername;

    @Value("${spring.datasource.password}")
    private String jdbcPassword;

    @Bean
    public DataSource configureUMDataSource() {
        final DataSource dataSource = DataSourceBuilder.create()
                .url(jdbcUrl)
                .username(jdbcUsername)
                .password(jdbcPassword)
                .build();
        runFlywayMigrations(dataSource);
        return dataSource;
    }

    private void runFlywayMigrations(final DataSource dataSource) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .baselineOnMigrate(true)
                .baselineVersion("1")
                .locations("db/migration")
                .target(MigrationVersion.LATEST)
                .outOfOrder(true)
                .validateOnMigrate(true)
                .load();
        flyway.repair();
        flyway.migrate();
    }
    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }
}
