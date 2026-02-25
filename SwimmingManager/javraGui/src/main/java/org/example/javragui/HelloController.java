package org.example.javragui;

import com.example.grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Participants;
import model.Race;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public class HelloController {

    @FXML
    private TextField nameText;
    @FXML
    private TextField ageText;
    @FXML
    private Button addParticipantButton;
    @FXML
    private TableView<Participants> participantTable;
    @FXML
    private TableColumn<Participants, String> nameColumn;
    @FXML
    private TableColumn<Participants, Integer> ageColumn;
    @FXML
    private TableColumn<Participants, Integer> participantIdColumn;
    @FXML
    private TableView<Race> raceTable;
    @FXML
    private TableColumn<Race, String> styleColumn;
    @FXML
    private TableColumn<Race, Integer> distanceColumn;
    @FXML
    private TableColumn<Race, Integer> raceIdColumn;
    @FXML
    private TableColumn<Race, Integer> totalParticipantsColumn;

    private ManagedChannel channel;
    private ParticipantServiceGrpc.ParticipantServiceBlockingStub blockingStub;
    private ParticipantServiceGrpc.ParticipantServiceStub asyncStub;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private ObservableList<Race> raceList = FXCollections.observableArrayList();
    private ObservableList<Participants> participantList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        channel = ManagedChannelBuilder.forAddress("localhost", 12345)
                .usePlaintext()
                .build();
        blockingStub = ParticipantServiceGrpc.newBlockingStub(channel);
        asyncStub = ParticipantServiceGrpc.newStub(channel);

        setupTableColumns();
        loadRaceData();
        subscribeToUpdates();

        raceTable.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            if (n != null) loadParticipantsForRace(n.getRaceId());
        });
        addParticipantButton.setOnAction(e -> addParticipant());
    }

    private void setupTableColumns() {
        raceIdColumn.setCellValueFactory(new PropertyValueFactory<>("raceId"));
        distanceColumn.setCellValueFactory(new PropertyValueFactory<>("distance"));
        styleColumn.setCellValueFactory(new PropertyValueFactory<>("style"));
        totalParticipantsColumn.setCellValueFactory(new PropertyValueFactory<>("totalParticipants"));

        participantIdColumn.setCellValueFactory(new PropertyValueFactory<>("participantId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
    }

    private void loadRaceData() {
        executor.submit(() -> {
            RacesList resp = blockingStub.getAllRaces(Empty.newBuilder().build());
            List<Race> races = resp.getRacesList().stream()
                    .map(r -> new Race(r.getRaceId(), r.getDistance(), r.getStyle(), r.getTotalParticipants()))
                    .collect(Collectors.toList());

            Platform.runLater(() -> {
                raceList.setAll(races);
                raceTable.setItems(raceList);
            });
        });
    }

    private void loadParticipantsForRace(int raceId) {
        executor.submit(() -> {
            RaceIdRequest req = RaceIdRequest.newBuilder().setRaceId(raceId).build();
            ParticipantsList resp = blockingStub.getParticipantsForRace(req);

            List<Participants> parts = resp.getParticipantsList().stream()
                    .map(p -> new Participants(p.getId(), p.getName(), p.getAge()))
                    .collect(Collectors.toList());

            Platform.runLater(() -> {
                System.out.println("▶ loading participants for race " + raceId + ": count=" + parts.size());

                participantList.setAll(parts);
                participantTable.setItems(participantList);
            });
        });
    }

    private void addParticipant() {
        String name = nameText.getText();
        String ageVal = ageText.getText();
        if (name.isEmpty() || ageVal.isEmpty()) return;

        try {
            int age = Integer.parseInt(ageVal);
            Race sel = raceTable.getSelectionModel().getSelectedItem();
            if (sel == null) return;

            AddParticipantRequest req = AddParticipantRequest.newBuilder()
                    .setName(name)
                    .setAge(age)
                    .setRaceId(sel.getRaceId())
                    .build();

            executor.submit(() -> {
                blockingStub.addParticipant(req);
                loadParticipantsForRace(sel.getRaceId());
            });

            nameText.clear();
            ageText.clear();
        } catch (NumberFormatException ex) {
            System.err.println("Invalid age");
        }
    }

    private void subscribeToUpdates() {
        asyncStub.subscribeUpdates(Empty.newBuilder().build(), new StreamObserver<ParticipantsList>() {
            @Override
            public void onNext(ParticipantsList unused) {

                System.out.println("s-a primit update, se da refresh");
                Platform.runLater(() -> {
                    Race sel = raceTable.getSelectionModel().getSelectedItem();
                    if (sel != null) {
                        loadParticipantsForRace(sel.getRaceId());
                    }
                });
            }
            @Override public void onError(Throwable t) {
                System.err.println("Stream error: " + t);
            }
            @Override public void onCompleted() {
                System.out.println("Updates stream completed");
            }
        });
    }

    public void shutdown() {
        if (channel != null) channel.shutdownNow();
        executor.shutdown();
    }
}
