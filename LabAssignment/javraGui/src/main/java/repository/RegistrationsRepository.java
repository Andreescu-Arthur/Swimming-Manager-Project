package repository;

import model.Participants;
import model.Race;
import model.Registrations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RegistrationsRepository implements IRepository<Integer, Registrations> {
    private static final Logger logger = LogManager.getLogger(RegistrationsRepository.class); // Logger instance
    private Connection connection;

    public RegistrationsRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int size() {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM Registrations");
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                logger.info("Fetched registrations count: {}", count); // Log the count
                return count;
            }
        } catch (SQLException e) {
            logger.error("Error getting size: {}", e.getMessage()); // Log the error
        }
        return 0;
    }

    @Override
    public void save(Registrations registration) {
        String sql = "INSERT INTO Registrations (participant_id, race_id) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, registration.getParticipants().getParticipantId());
            statement.setInt(2, registration.getRace().getRaceId());
            statement.executeUpdate();

            // Retrieve the auto-generated registration_id
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
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, registration.getRace().getRaceId());
            statement.setInt(2, registration.getParticipants().getParticipantId());
            statement.setInt(3, registration.getRegistrationId());
            statement.executeUpdate();
            logger.info("Registration updated: {}", registration); // Log the updated registration
        } catch (SQLException e) {
            logger.error("Error updating registration: {}", e.getMessage()); // Log the error
        }
    }

    @Override
    public void delete(Integer registrations_id) {
        String sql = "DELETE FROM Registrations WHERE registration_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, registrations_id);
            statement.executeUpdate();
            logger.info("Registration deleted with ID: {}", registrations_id); // Log the deleted registration ID
        } catch (SQLException e) {
            logger.error("Error deleting registration: {}", e.getMessage()); // Log the error
        }
    }

    @Override
    public Registrations findOne(Integer id) {
        String sql = "SELECT * FROM Registrations WHERE registration_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Participants participant = new ParticipantRepository(connection).findOne(rs.getInt("participant_id"));
                Race race = new RaceRepository(connection).findOne(rs.getInt("race_id"));
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
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Participants participant = new ParticipantRepository(connection).findOne(rs.getInt("participant_id"));
                Race race = new RaceRepository(connection).findOne(rs.getInt("race_id"));
                registrations.add(new Registrations(rs.getInt("registrations_id"), race, participant));
            }
            logger.info("Fetched {} registrations", registrations.size()); // Log the number of registrations fetched
        } catch (SQLException e) {
            logger.error("Error fetching all registrations: {}", e.getMessage()); // Log the error
        }
        return registrations;
    }


}