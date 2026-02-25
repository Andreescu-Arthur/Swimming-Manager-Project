package services;

import model.Race;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import repository.RaceRepository;

import java.util.Collection;
import java.util.List;

@Component
public class RaceService {
    RaceRepository raceRepository;

    @Autowired
    public RaceService(RaceRepository repository) {
        this.raceRepository = repository;
    }

    public void addRace(Race race) {
        this.raceRepository.save(race);
    }

    public void deleteRace(Race race) {
        this.raceRepository.delete(race.getRaceId());
    }

    public void updateRace(Race race) {
        this.raceRepository.update(race.getRaceId(), race);
    }

    public Race getRaceById(int id) {
        return this.raceRepository.findOne(id);
    }

    public Collection<Race> getAllRaces() {
        return this.raceRepository.findAll();
    }


    public int getRaceCount(int raceId) {
        return this.raceRepository.getParticipantCountForRace(raceId);
    }

}
