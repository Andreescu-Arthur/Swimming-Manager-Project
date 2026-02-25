package network;


import com.example.grpc.ParticipantServiceGrpc;
import com.example.grpc.ParticipantServiceGrpc.ParticipantServiceStub;
import com.example.grpc.ParticipantServiceGrpc.ParticipantServiceBlockingStub;
import com.example.grpc.AddParticipantRequest;
import com.example.grpc.AddParticipantResponse;
import com.example.grpc.ParticipantsList;
import com.example.grpc.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class GrpcClient {
    private final ManagedChannel channel;
    private final ParticipantServiceBlockingStub blockingStub;
    private final ParticipantServiceStub asyncStub;

    public GrpcClient(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        this.blockingStub = ParticipantServiceGrpc.newBlockingStub(channel);
        this.asyncStub = ParticipantServiceGrpc.newStub(channel);
    }

    public AddParticipantResponse addParticipant(String name, int age, int raceId) {
        AddParticipantRequest request = AddParticipantRequest.newBuilder()
                .setName(name)
                .setAge(age)
                .setRaceId(raceId)
                .build();
        return blockingStub.addParticipant(request);
    }

    public void subscribeUpdates(StreamObserver<ParticipantsList> responseObserver) {
        asyncStub.subscribeUpdates(Empty.newBuilder().build(), responseObserver);
    }

    public void shutdown() {
        channel.shutdown();
    }
}
