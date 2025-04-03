package services;

import model.Participants;
import model.Registrations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.RegistrationsRepository;

import java.util.Collection;
import java.util.List;

@Service
public class RegistrationsService {
    RegistrationsRepository registrationsRepository;

    @Autowired
    public RegistrationsService(RegistrationsRepository repository) {
        this.registrationsRepository = repository;
    }

    public void addRegistration(Registrations registration) {
        this.registrationsRepository.save(registration);
    }


    public void removeRegistration(Registrations registration) {
        this.registrationsRepository.delete(registration.getRegistrationId());
    }

    public void updateRegistration(Registrations registration) {
        this.registrationsRepository.update(registration.getRegistrationId(), registration);
    }

    public Registrations getRegistration(int registrationId) {
        return this.registrationsRepository.findOne(registrationId);
    }

    public Collection<Registrations> getAllRegistrations() {
        return this.registrationsRepository.findAll();
    }

    public List<Participants> getParticipantsById(int registrationId) {
        return this.registrationsRepository.findParticipantsByRaceId(registrationId);
    }


}
