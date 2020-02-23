package com.hibernatedemo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.hibernatedemo.config.HibernateConfiguration;
import com.hibernatedemo.entities.ParkingGarage;

public class App {
    public static void main(String[] args) {
        ParkingGarage garage = new ParkingGarage();
        garage.setOccupied(true);
        garage.setSlotName("A1");
        garage.setType("motorbike");

        ParkingGarage garageOne = new ParkingGarage();
        garageOne.setOccupied(false);
        garageOne.setSlotName("A2");
        garageOne.setType("motorbike");

        SessionFactory factory = HibernateConfiguration.getSessionFactory();
        Session session = factory.openSession();
        session.beginTransaction();

        session.save(garage);
        session.save(garageOne);

        session.flush();
        session.close();
    }
}
