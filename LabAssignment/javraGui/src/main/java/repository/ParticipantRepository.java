package repository;

import model.Participants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ParticipantRepository implements IRepository<Integer, Participants> {
    private static final Logger logger = LogManager.getLogger(ParticipantRepository.class); // Logger instance
    private Connection connection;

    public ParticipantRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int size() {
        String sql = "SELECT COUNT(*) FROM Participants";
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
        String sql = "INSERT INTO Participants (name, age) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, participant.getName());
            statement.setInt(2, participant.getAge());
            statement.executeUpdate();

            // Retrieve the auto-generated participant_id
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    participant.setParticipantId(generatedKeys.getInt(1));
                }
            }

            logger.info("Participant saved: {}", participant);
        } catch (SQLException e) {
            logger.error("Error saving participant: {}", e.getMessage());
        }
    }

    @Override
    public void update(Integer participant_id, Participants participant) {
        String sql = "UPDATE Participants SET name = ?, age = ? WHERE participant_id = ?";
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
    public void delete(Integer participant_id) {
        String sql = "DELETE FROM Participants WHERE participant_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, participant_id);
            statement.executeUpdate();
            logger.info("Participant deleted with ID: {}", participant_id); // Log the deleted participant ID
        } catch (SQLException e) {
            logger.error("Error deleting participant: {}", e.getMessage()); // Log the error
        }
    }

    @Override
    public Participants findOne(Integer id) {
        String sql = "SELECT * FROM Participants WHERE participant_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Participants participant = new Participants(
                        rs.getInt("participant_id"),
                        rs.getString("name"),
                        rs.getInt("age")
                );
                logger.info("Participant found: {}", participant);
                return participant;
            }
        } catch (SQLException e) {
            logger.error("Error finding participant: {}", e.getMessage());
        }
        return null;
    }

    @Override
    public Collection<Participants> findAll() {
        List<Participants> participants = new ArrayList<>();
        String sql = "SELECT * FROM Participants";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Participants participant = new Participants(rs.getInt("participant_id"), rs.getString("name"), rs.getInt("age"));
                participants.add(participant);
            }
            logger.info("Fetched {} participants", participants.size()); // Log the number of participants fetched
        } catch (SQLException e) {
            logger.error("Error fetching all participants: {}", e.getMessage()); // Log the error
        }
        return participants;
    }
}