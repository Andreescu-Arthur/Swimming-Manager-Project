package model;
import model.Participants;
import model.Race;

public class Registrations {

    Race race;
    Participants participants;

    public Registrations(Race race, Participants participants) {
        this.race = race;
        this.participants = participants;
    }


    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Participants getParticipants() {
        return participants;
    }

    public void setParticipants(Participants participants) {
        this.participants = participants;
    }

}
