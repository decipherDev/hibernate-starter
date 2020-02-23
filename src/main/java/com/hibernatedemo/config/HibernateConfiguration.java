package com.hibernatedemo.config;

import static org.hibernate.cfg.AvailableSettings.AUTOCOMMIT;
import static org.hibernate.cfg.AvailableSettings.DIALECT;
import static org.hibernate.cfg.AvailableSettings.DRIVER;
import static org.hibernate.cfg.AvailableSettings.HBM2DDL_AUTO;
import static org.hibernate.cfg.AvailableSettings.PASS;
import static org.hibernate.cfg.AvailableSettings.URL;
import static org.hibernate.cfg.AvailableSettings.USER;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.UnaryOperator;
import java.util.logging.Logger;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import com.hibernatedemo.entities.ParkingGarage;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HibernateConfiguration {
    private static final Logger log = Logger.getLogger(HibernateConfiguration.class.getName());

    public static SessionFactory getSessionFactory() {
        return getMetadata().buildSessionFactory();
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

        UnaryOperator<String> getHibernateProperty = getHibernateProperties()::getProperty;

        configMap.computeIfAbsent(DRIVER, getHibernateProperty);
        configMap.computeIfAbsent(URL, getHibernateProperty);
        configMap.computeIfAbsent(USER, getHibernateProperty);
        configMap.computeIfAbsent(PASS, getHibernateProperty);
        configMap.computeIfAbsent(DIALECT, getHibernateProperty);
        configMap.computeIfAbsent(HBM2DDL_AUTO, getHibernateProperty);
        configMap.computeIfAbsent(AUTOCOMMIT, getHibernateProperty);

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