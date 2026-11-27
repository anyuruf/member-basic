package net.anyuruf.memberbasic.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

@Component
@Profile({"local-liquibase"})
public class LiquibaseRunner {
	 private static final Logger LOG =
		        LoggerFactory.getLogger(LiquibaseRunner.class);

    private final Environment environment;
    
    public LiquibaseRunner(Environment environment) {
        this.environment = environment;
    }


    @PostConstruct
    public void liquibaseRunner() {
        try {
            //Ensure driver is on classpath
            Class.forName("com.ing.data.cassandra.jdbc.CassandraDriver");
            var url = environment.getRequiredProperty("spring.liquibase.url");
            var username = environment.getProperty("spring.liquibase.username");
            var password = environment.getProperty("spring.liquibase.password");

            Properties properties = new Properties();

            if (username != null && !username.isBlank()) {
                properties.setProperty("user", username);
            }

            if (password != null && !password.isBlank()) {
                properties.setProperty("password", password);
            }

            try (Connection conn = DriverManager.getConnection(url, properties)) {
                Database database = DatabaseFactory.getInstance()
                        .findCorrectDatabaseImplementation(new JdbcConnection(conn));

                try (Liquibase liquibase = new Liquibase(
                        "config/liquibase/master.xml",
                        new ClassLoaderResourceAccessor(),
                        database)) {

                    liquibase.update(); // run all pending changes
                }
            }
        } catch (Exception exception) {
			LOG.error("Exception occurred during Liquibase execution", exception);	
			throw new IllegalStateException("Cassandra migration failed", exception);
        }
    }

}