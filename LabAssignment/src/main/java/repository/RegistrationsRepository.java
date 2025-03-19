package repository;

import model.Participants;
import model.Race;
import model.Registrations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
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
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM registrations");
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
        String sql = "INSERT INTO Registrations (id, raceId, participantId) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, registration.getRegistrationId());
            statement.setInt(2, registration.getRace().getRaceId());
            statement.setInt(3, registration.getParticipants().getParticipantId());
            statement.executeUpdate();
            logger.info("Registration saved: {}", registration); // Log the saved registration
        } catch (SQLException e) {
            logger.error("Error saving registration: {}", e.getMessage()); // Log the error
        }
    }

    @Override
    public void update(Integer id, Registrations registration) {
        String sql = "UPDATE Registrations SET raceId = ?, participantId = ? WHERE id = ?";
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
    public void delete(Integer id) {
        String sql = "DELETE FROM Registrations WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            logger.info("Registration deleted with ID: {}", id); // Log the deleted registration ID
        } catch (SQLException e) {
            logger.error("Error deleting registration: {}", e.getMessage()); // Log the error
        }
    }

    @Override
    public Registrations findOne(Integer id) {
        String sql = "SELECT * FROM Registrations WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Participants participant = new ParticipantRepository(connection).findOne(rs.getInt("participantId"));
                Race race = new RaceRepository(connection).findOne(rs.getInt("raceId"));
                Registrations registration = new Registrations(rs.getInt("id"), race, participant);
                logger.info("Registration found: {}", registration); // Log the found registration
                return registration;
            }
        } catch (SQLException e) {
            logger.error("Error finding registration: {}", e.getMessage()); // Log the error
        }
        return null;
    }

    @Override
    public Iterable<Registrations> findAll() {
        List<Registrations> registrations = new ArrayList<>();
        String sql = "SELECT * FROM Registrations";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Participants participant = new ParticipantRepository(connection).findOne(rs.getInt("participantId"));
                Race race = new RaceRepository(connection).findOne(rs.getInt("raceId"));
                registrations.add(new Registrations(rs.getInt("id"), race, participant));
            }
            logger.info("Fetched {} registrations", registrations.size()); // Log the number of registrations fetched
        } catch (SQLException e) {
            logger.error("Error fetching all registrations: {}", e.getMessage()); // Log the error
        }
        return registrations;
    }


}