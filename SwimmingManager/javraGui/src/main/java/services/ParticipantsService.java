package services;

import model.Participants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.ParticipantRepository;

import java.util.Collection;
import java.util.List;

@Service
public class ParticipantsService {
    ParticipantRepository ParticipantRepository;

    @Autowired
    public ParticipantsService(ParticipantRepository repository) {
        this.ParticipantRepository = repository;
    }


    public void addParticipant(Participants participants) {
        this.ParticipantRepository.save(participants);
    }


    public void deleteParticipant(Participants participants) {
        this.ParticipantRepository.delete(participants.getParticipantId());
    }


    public void updateParticipant(Participants participants) {
        this.ParticipantRepository.update(participants.getParticipantId(), participants);
    }


    public Participants getParticipant(int participantId) {
        return this.ParticipantRepository.findOne(participantId);
    }


    public Collection<Participants> getAllParticipants() {
        return this.ParticipantRepository.findAll();
    }

}
