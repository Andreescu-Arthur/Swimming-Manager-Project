package network;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import repository.Connect;
import repository.ParticipantRepository;
import repository.RaceRepository;
import repository.RegistrationsRepository;

import java.io.IOException;

public class ServerMain {
    private static final int PORT = 12345;

    public static void main(String[] args) throws IOException, InterruptedException {
        Connect connect = new Connect();
        
        RaceRepository raceRepo = new RaceRepository(connect);
        ParticipantRepository partRepo = new ParticipantRepository(connect);
        RegistrationsRepository regRepo = new RegistrationsRepository(connect);
        
        ParticipantServiceImpl participantService = new ParticipantServiceImpl(raceRepo, partRepo, regRepo);

        Server server = ServerBuilder.forPort(PORT)
                .addService(participantService)
                .build();

        System.out.println("gRPC Server started on port " + PORT);
        server.start();
        
        // Keep the server running
        server.awaitTermination();
    }
}

