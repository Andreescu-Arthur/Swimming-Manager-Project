package network;

import com.example.grpc.*;
import io.grpc.stub.StreamObserver;
import model.Participants;
import model.Race;
import model.Registrations;
import repository.ParticipantRepository;
import repository.RaceRepository;
import repository.RegistrationsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ParticipantServiceImpl extends ParticipantServiceGrpc.ParticipantServiceImplBase {
    private final RaceRepository raceRepository;
    private final ParticipantRepository participantRepository;
    private final RegistrationsRepository registrationsRepository;
    private final List<StreamObserver<ParticipantsList>> observers = new ArrayList<>();

    public ParticipantServiceImpl(RaceRepository raceRepo, ParticipantRepository partRepo, RegistrationsRepository regRepo) {
        this.raceRepository = raceRepo;
        this.participantRepository = partRepo;
        this.registrationsRepository = regRepo;
    }

    @Override
    public void getAllRaces(Empty request, StreamObserver<RacesList> responseObserver) {
        List<com.example.grpc.Race> grpcRaces = raceRepository.findAll().stream()
                .map(r -> com.example.grpc.Race.newBuilder()
                        .setRaceId(r.getRaceId())
                        .setDistance(r.getDistance())
                        .setStyle(r.getStyle())
                        .setTotalParticipants(r.getTotalParticipants())
                        .build())
                .collect(Collectors.toList());

        responseObserver.onNext(RacesList.newBuilder().addAllRaces(grpcRaces).build());
        responseObserver.onCompleted();
    }

    @Override
    public void getParticipantsForRace(RaceIdRequest request, StreamObserver<ParticipantsList> responseObserver) {
        List<com.example.grpc.Participant> grpcParticipants = registrationsRepository.findParticipantsByRaceId(request.getRaceId()).stream()
                .map(p -> com.example.grpc.Participant.newBuilder()
                        .setId(p.getParticipantId())
                        .setName(p.getName())
                        .setAge(p.getAge())
                        .build())
                .collect(Collectors.toList());

        responseObserver.onNext(ParticipantsList.newBuilder().addAllParticipants(grpcParticipants).build());
        responseObserver.onCompleted();
    }

    @Override
    public void addParticipant(AddParticipantRequest request, StreamObserver<AddParticipantResponse> responseObserver) {
        // Simple logic: check if participant exists by name (very basic) or just create new
        // For this assignment, we usually just add the registration.
        // Let's assume we find or create the participant first.
        
        Participants p = new Participants(request.getAge(), request.getName());
        participantRepository.save(p); // This should set the ID in the object
        
        Race r = raceRepository.findOne(request.getRaceId());
        if (r != null) {
            Registrations reg = new Registrations(r, p);
            registrationsRepository.save(reg);
            
            responseObserver.onNext(AddParticipantResponse.newBuilder().setSuccess(true).build());
            responseObserver.onCompleted();
            
            notifyObservers();
        } else {
            responseObserver.onNext(AddParticipantResponse.newBuilder().setSuccess(false).build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void subscribeUpdates(Empty request, StreamObserver<ParticipantsList> responseObserver) {
        synchronized (observers) {
            observers.add(responseObserver);
        }
    }

    private void notifyObservers() {
        synchronized (observers) {
            List<StreamObserver<ParticipantsList>> failed = new ArrayList<>();
            for (StreamObserver<ParticipantsList> observer : observers) {
                try {
                    // Just send an empty update to trigger a refresh in the client
                    observer.onNext(ParticipantsList.newBuilder().build());
                } catch (Exception e) {
                    failed.add(observer);
                }
            }
            observers.removeAll(failed);
        }
    }
}
