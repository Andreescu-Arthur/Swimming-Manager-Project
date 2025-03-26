package services;

import model.Race;
import repository.RaceRepository;

import java.util.Collection;
import java.util.List;


public class RaceService {
    RaceRepository raceRepository;

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
}
