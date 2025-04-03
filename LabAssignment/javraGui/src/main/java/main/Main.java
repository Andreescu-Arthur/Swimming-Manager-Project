package main;

import model.Participants;
import model.Race;
import model.Registrations;
import repository.ParticipantRepository;
import repository.RaceRepository;
import repository.RegistrationsRepository;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;


import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import services.RaceService;
import model.Race;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(config.javaConfig.class);

        RaceService raceService = context.getBean(RaceService.class);

        Race testRace = new Race(1, 200, "freestyle", 10);
        raceService.addRace(testRace);

        System.out.println("Race added: " + raceService.getRaceById(1));

        System.out.println("All races: " + raceService.getAllRaces());
    }
}