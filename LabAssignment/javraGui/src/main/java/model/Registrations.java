package model;
import repository.HasId;

public class Registrations implements HasId<Integer> {
    private int registrationId;
    private Race race;
    private Participants participants;


    public Registrations(Race race, Participants participants) {
        this.race = race;
        this.participants = participants;
    }


    public Registrations(int registrationId, Race race, Participants participants) {
        this.registrationId = registrationId;
        this.race = race;
        this.participants = participants;
    }


    public int getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(int registrationId) {
        this.registrationId = registrationId;
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

    @Override
    public Participants getId() {
        return null;
    }

    @Override
    public void setId(Integer integer) {
        this.registrationId = integer;
    }

    public void setId(Registrations registrations) {
        registrations.setRegistrationId(registrationId);
    }
}
