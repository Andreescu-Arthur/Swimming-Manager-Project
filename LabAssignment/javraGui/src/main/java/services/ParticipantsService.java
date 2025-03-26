package services;

import model.Participants;
import repository.ParticipantRepository;

import java.util.Collection;
import java.util.List;


public class ParticipantsService {
    ParticipantRepository ParticipantRepository;

    public ParticipantsService(ParticipantRepository repository) {
        this.ParticipantRepository = repository;
    }

    public void addParticipant(Participants participants) {
        this.ParticipantRepository.delete(participants.getParticipantId());
    }

    public void deleteParticipant(Participants participants) {
        this.ParticipantRepository.delete(participants.getParticipantId());
    }

    public void updateParticipant(Participants participants) {
        this.ParticipantRepository.delete(participants.getParticipantId());
    }

    public Participants getParticipant(int participantId) {
        return this.ParticipantRepository.findOne(participantId);
    }
    public Collection<Participants> getAllParticipants() {
        return this.ParticipantRepository.findAll();
    }

}
