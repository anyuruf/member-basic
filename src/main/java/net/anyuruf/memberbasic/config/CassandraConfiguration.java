package net.anyuruf.memberbasic.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractReactiveCassandraConfiguration;
import org.springframework.data.cassandra.repository.config.EnableReactiveCassandraRepositories;


@Configuration
@EnableReactiveCassandraRepositories
class ApplicationConfig extends AbstractReactiveCassandraConfiguration {


       @Value("${spring.cassandra.keyspace-name}")
       private String keySpace;

       @Value("${spring.cassandra.contact-points}")
       private String contactPoints;

       @Value("${spring.cassandra.port}")
       private int port;

       /*
        * Provide a keyspace name to the configuration.
        */
       @Override
       public String getKeyspaceName() {
           return keySpace;
       }

       @Override
       public String getContactPoints() {
           return contactPoints;
       }

       @Override
       public int getPort() {
           return port;
       }
}