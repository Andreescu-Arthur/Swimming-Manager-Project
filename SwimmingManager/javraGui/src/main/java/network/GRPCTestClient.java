package network;



import com.example.grpc.AddParticipantRequest;
import com.example.grpc.AddParticipantResponse;
import com.example.grpc.ParticipantServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GRPCTestClient {
    public static void main(String[] args) {

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 12345)
                .usePlaintext()
                .build();


        ParticipantServiceGrpc.ParticipantServiceBlockingStub stub =
                ParticipantServiceGrpc.newBlockingStub(channel);


        AddParticipantRequest request = AddParticipantRequest.newBuilder()
                .setName("Arthur-Test")
                .setAge(19)
                .setRaceId(1)
                .build();


        try {
            AddParticipantResponse response = stub.addParticipant(request);
            System.out.println("Server responded:");
            System.out.println("Success: " + response.getSuccess());
            System.out.println("Message: " + response.getMessage());
        } catch (Exception e) {
            System.err.println("RPC failed: " + e.getMessage());
            e.printStackTrace();
        }


        channel.shutdown();
    }
}
