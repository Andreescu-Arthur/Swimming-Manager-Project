package org.example;

import model.Participants;
import model.Race;
import model.Registrations;
import repository.Connect;
import repository.ParticipantRepository;
import repository.RaceRepository;
import repository.RegistrationsRepository;

public class Main {
    public static void main(String[] args) {
        try {
            Connect connect = new Connect();

            // Instantiate repositories
            ParticipantRepository participantRepository = new ParticipantRepository(connect);
            RaceRepository raceRepository = new RaceRepository(connect);
            RegistrationsRepository registrationsRepository = new RegistrationsRepository(connect);

            // Test ParticipantRepository
            System.out.println("\nTesting ParticipantRepository:");

            // Create a new participant
            Participants participant = new Participants(25, "John Doe");
            participantRepository.save(participant);
            System.out.println("Participant added: " + participant);

            // Find the participant
            Participants foundParticipant = participantRepository.findOne(participant.getParticipantId());
            System.out.println("Participant found: " + foundParticipant);

            // Update the participant
            participant.setName("Jane Doe");
            participant.setAge(30);
            participantRepository.update(participant.getParticipantId(), participant);
            System.out.println("Participant updated: " + participantRepository.findOne(participant.getParticipantId()));

            // Delete the participant
            participantRepository.delete(participant.getParticipantId());
            System.out.println("Participant deleted. Fetching again: " + participantRepository.findOne(participant.getParticipantId()));

            // Test RaceRepository
            System.out.println("\nTesting RaceRepository:");

            // Create a new race
            Race race = new Race(200, "freestyle", 10);
            raceRepository.save(race);
            System.out.println("Race added: " + race);

            // Find the race
            Race foundRace = raceRepository.findOne(race.getRaceId());
            System.out.println("Race found: " + foundRace);

            // Update the race
            race.setDistance(200);
            race.setStyle("backstroke");
            race.setTotalParticipants(20);
            raceRepository.update(race.getRaceId(), race);
            System.out.println("Race updated: " + raceRepository.findOne(race.getRaceId()));

            // Delete the race
            raceRepository.delete(race.getRaceId());
            System.out.println("Race deleted. Fetching again: " + raceRepository.findOne(race.getRaceId()));

            // Test RegistrationsRepository
            System.out.println("\nTesting RegistrationsRepository:");

            // Create a new participant and race for registration
            Participants participantForRegistration = new Participants(22, "Alice Smith");
            participantRepository.save(participantForRegistration);
            Race raceForRegistration = new Race(50, "butterfly", 15);
            raceRepository.save(raceForRegistration);

            // Create a new registration
            Registrations registration = new Registrations(raceForRegistration, participantForRegistration);
            registrationsRepository.save(registration);
            System.out.println("Registration added: " + registration);

            // Find the registration
            Registrations foundRegistration = registrationsRepository.findOne(registration.getRegistrationId());
            System.out.println("Registration found: " + foundRegistration);

            // Update the registration
            Participants newParticipant = new Participants(28, "Bob Johnson");
            participantRepository.save(newParticipant);
            registration.setParticipants(newParticipant);
            registrationsRepository.update(registration.getRegistrationId(), registration);
            System.out.println("Registration updated: " + registrationsRepository.findOne(registration.getRegistrationId()));

            // Delete the registration
            registrationsRepository.delete(registration.getRegistrationId());
            System.out.println("Registration deleted. Fetching again: " + registrationsRepository.findOne(registration.getRegistrationId()));

        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}