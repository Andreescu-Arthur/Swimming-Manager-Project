package repository;

import model.Participants;
import model.Race;
import model.Registrations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class RegistrationsRepository implements IRepository<Integer, Registrations> {
    private static final Logger logger = LogManager.getLogger(RegistrationsRepository.class);
    private Connect connect;

    public RegistrationsRepository(Connect connect) {
        this.connect = connect;
    }

    public RegistrationsRepository(){}

    @Override
    public int size() {
        String sql = "SELECT COUNT(*) FROM Registrations";
        try (Connection connection = connect.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                logger.info("Fetched registrations count: {}", count);
                return count;
            }
        } catch (SQLException e) {
            logger.error("Error getting size: {}", e.getMessage());
        }
        return 0;
    }

    @Override
    public void save(Registrations registration) {
        String sql = "INSERT INTO Registrations (participant_id, race_id) VALUES (?, ?)";
        try (Connection connection = connect.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, registration.getParticipants().getParticipantId());
            statement.setInt(2, registration.getRace().getRaceId());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    registration.setRegistrationId(generatedKeys.getInt(1));
                }
            }
            logger.info("Registration saved: {}", registration);
        } catch (SQLException e) {
            logger.error("Error saving registration: {}", e.getMessage());
        }
    }

    @Override
    public void update(Integer registration_id, Registrations registration) {
        String sql = "UPDATE Registrations SET race_id = ?, participant_id = ? WHERE registration_id = ?";
        try (Connection connection = connect.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, registration.getRace().getRaceId());
            statement.setInt(2, registration.getParticipants().getParticipantId());
            statement.setInt(3, registration.getRegistrationId());
            statement.executeUpdate();
            logger.info("Registration updated: {}", registration);
        } catch (SQLException e) {
            logger.error("Error updating registration: {}", e.getMessage());
        }
    }

    @Override
    public void delete(Integer registrations_id) {
        String sql = "DELETE FROM Registrations WHERE registration_id = ?";
        try (Connection connection = connect.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, registrations_id);
            statement.executeUpdate();
            logger.info("Registration deleted with ID: {}", registrations_id);
        } catch (SQLException e) {
            logger.error("Error deleting registration: {}", e.getMessage());
        }
    }

    @Override
    public Registrations findOne(Integer id) {
        String sql = "SELECT * FROM Registrations WHERE registration_id = ?";
        try (Connection connection = connect.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Participants participant = new ParticipantRepository(connect).findOne(rs.getInt("participant_id"));
                Race race = new RaceRepository(connect).findOne(rs.getInt("race_id"));
                Registrations registration = new Registrations(
                        rs.getInt("registration_id"),
                        race,
                        participant
                );
                logger.info("Registration found: {}", registration);
                return registration;
            }
        } catch (SQLException e) {
            logger.error("Error finding registration: {}", e.getMessage());
        }
        return null;
    }

    @Override
    public Collection<Registrations> findAll() {
        List<Registrations> registrations = new ArrayList<>();
        String sql = "SELECT * FROM Registrations";
        try (Connection connection = connect.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Participants participant = new ParticipantRepository(connect).findOne(rs.getInt("participant_id"));
                Race race = new RaceRepository(connect).findOne(rs.getInt("race_id"));
                registrations.add(new Registrations(rs.getInt("registration_id"), race, participant));
            }
            logger.info("Fetched {} registrations", registrations.size());
        } catch (SQLException e) {
            logger.error("Error fetching all registrations: {}", e.getMessage());
        }
        return registrations;
    }

    public List<Participants> findParticipantsByRaceId(int raceId) {
        List<Participants> participants = new ArrayList<>();
        String sql = "SELECT p.participant_id, p.name, p.age FROM Participants p " +
                "JOIN Registrations r ON p.participant_id = r.participant_id " +
                "WHERE r.race_id = ?";
        try (Connection connection = connect.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, raceId);
            System.out.println("Executing query: " + sql + " with raceId: " + raceId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Participants participant = new Participants(
                        rs.getInt("participant_id"),
                        rs.getString("name"),
                        rs.getInt("age")
                );
                participants.add(participant);
            }
            System.out.println("Found " + participants.size() + " participants for race " + raceId);
        } catch (SQLException e) {
            logger.error("Error fetching participants for race: {}", e.getMessage());
        }
        return participants;
    }

    public boolean isRegisteredToRace(int participantId, int raceId) {
        String query = "SELECT COUNT(*) FROM Registrations WHERE participant_id = ? AND race_id = ?";
        try (Connection connection = connect.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, participantId);
            stmt.setInt(2, raceId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking registration: " + e.getMessage());
        }
        return false;
    }
}
