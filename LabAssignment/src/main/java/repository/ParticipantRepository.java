package repository;

import model.Participants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParticipantRepository implements IRepository<Integer, Participants> {
    private static final Logger logger = LogManager.getLogger(ParticipantRepository.class); // Logger instance
    private Connection connection;

    public ParticipantRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int size() {
        String sql = "SELECT COUNT(*) FROM participants";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                logger.info("Fetched participant count: {}", count); // Log the count
                return count;
            }
        } catch (SQLException e) {
            logger.error("Error getting size: {}", e.getMessage()); // Log the error
        }
        return 0;
    }

    @Override
    public void save(Participants participant) {
        String sql = "INSERT INTO Participants (id, name, age) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, participant.getParticipantId());
            statement.setString(2, participant.getName());
            statement.setInt(3, participant.getAge());
            statement.executeUpdate();
            logger.info("Participant saved: {}", participant); // Log the saved participant
        } catch (SQLException e) {
            logger.error("Error saving participant: {}", e.getMessage()); // Log the error
        }
    }

    @Override
    public void update(Integer id, Participants participant) {
        String sql = "UPDATE Participants SET name = ?, age = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, participant.getName());
            statement.setInt(2, participant.getAge());
            statement.setInt(3, participant.getParticipantId());
            statement.executeUpdate();
            logger.info("Participant updated: {}", participant); // Log the updated participant
        } catch (SQLException e) {
            logger.error("Error updating participant: {}", e.getMessage()); // Log the error
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM Participants WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            logger.info("Participant deleted with ID: {}", id); // Log the deleted participant ID
        } catch (SQLException e) {
            logger.error("Error deleting participant: {}", e.getMessage()); // Log the error
        }
    }

    @Override
    public Participants findOne(Integer id) {
        String sql = "SELECT * FROM Participants WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Participants participant = new Participants(rs.getInt("id"), rs.getString("name"), rs.getInt("age"));
                logger.info("Participant found: {}", participant); // Log the found participant
                return participant;
            }
        } catch (SQLException e) {
            logger.error("Error finding participant: {}", e.getMessage()); // Log the error
        }
        return null;
    }

    @Override
    public Iterable<Participants> findAll() {
        List<Participants> participants = new ArrayList<>();
        String sql = "SELECT * FROM Participants";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Participants participant = new Participants(rs.getInt("id"), rs.getString("name"), rs.getInt("age"));
                participants.add(participant);
            }
            logger.info("Fetched {} participants", participants.size()); // Log the number of participants fetched
        } catch (SQLException e) {
            logger.error("Error fetching all participants: {}", e.getMessage()); // Log the error
        }
        return participants;
    }
}