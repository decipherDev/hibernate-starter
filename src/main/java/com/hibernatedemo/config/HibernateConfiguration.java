package com.hibernatedemo.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.logging.Logger;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import com.hibernatedemo.entities.ParkingGarage;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HibernateConfiguration {
    private static final Logger log = Logger.getLogger(HibernateConfiguration.class.getName());

    public static SessionFactory getSessionFactory() {
        SessionFactory sfacory = getMetadata().buildSessionFactory();
        return sfacory;
    }

    private static Metadata getMetadata() {
        return new MetadataSources(getServiceRegistry()).addAnnotatedClass(ParkingGarage.class).getMetadataBuilder()
                .build();
    }

    private static ServiceRegistry getServiceRegistry() {
        StandardServiceRegistryBuilder sRegistryBuilder = new StandardServiceRegistryBuilder();
        sRegistryBuilder.applySettings(getConfigurationMap());
        return sRegistryBuilder.build();
    }

    private static Map<String, String> getConfigurationMap() {
        Map<String, String> configMap = new HashMap<>();
        Function<String, String> getHibernateProperty = getHibernateProperties()::getProperty;

        configMap.computeIfAbsent(Environment.DRIVER, getHibernateProperty);
        configMap.computeIfAbsent(Environment.URL, getHibernateProperty);
        configMap.computeIfAbsent(Environment.USER, getHibernateProperty);
        configMap.computeIfAbsent(Environment.PASS, getHibernateProperty);
        configMap.computeIfAbsent(Environment.DIALECT, getHibernateProperty);
        configMap.computeIfAbsent(Environment.HBM2DDL_AUTO, getHibernateProperty);
        configMap.computeIfAbsent(Environment.AUTOCOMMIT, getHibernateProperty);

        return configMap;
    }

    private static Properties getHibernateProperties() {
        Properties hibernateProperties = new Properties();
        try {
            hibernateProperties
                    .load(HibernateConfiguration.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (Exception exception) {
            log.severe(String.format("Error during reading from application.properties %s", exception));
        }

        return hibernateProperties;
    }
}