package repository;

import model.Race;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RaceRepository implements IRepository<Integer, Race> {
    private static final Logger logger = LogManager.getLogger(RaceRepository.class); // Logger instance
    private Connection connection;

    public RaceRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int size() {
        String sql = "SELECT COUNT(*) FROM Races";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                logger.info("Fetched race count: {}", count); // Log the count
                return count;
            }
        } catch (SQLException e) {
            logger.error("Error getting size: {}", e.getMessage()); // Log the error
        }
        return 0;
    }

    @Override
    public void save(Race race) {
        String sql = "INSERT INTO Races (distance, style, total_participants) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, race.getDistance());
            statement.setString(2, race.getStyle());
            statement.setInt(3, race.getTotalParticipants());
            statement.executeUpdate();

            // Retrieve the auto-generated race_id
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    race.setRaceId(generatedKeys.getInt(1));
                }
            }

            logger.info("Race saved: {}", race);
        } catch (SQLException e) {
            logger.error("Error saving race: {}", e.getMessage());
        }
    }

    @Override
    public void delete(Integer race_id) {
        String sql = "DELETE FROM Races WHERE race_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, race_id);
            statement.executeUpdate();
            logger.info("Race deleted with ID: {}", race_id); // Log the deleted race ID
        } catch (SQLException e) {
            logger.error("Error deleting race: {}", e.getMessage()); // Log the error
        }
    }

    @Override
    public void update(Integer race_id, Race race) {
        String sql = "UPDATE Races SET distance = ?, style = ?, total_Participants = ? WHERE race_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, race.getDistance());
            statement.setString(2, race.getStyle());
            statement.setInt(3, race.getTotalParticipants());
            statement.setInt(4, race.getRaceId());
            statement.executeUpdate();
            logger.info("Race updated: {}", race); // Log the updated race
        } catch (SQLException e) {
            logger.error("Error updating race: {}", e.getMessage()); // Log the error
        }
    }

    @Override
    public Race findOne(Integer id) {
        String sql = "SELECT * FROM Races WHERE race_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Race race = new Race(
                        rs.getInt("race_id"),
                        rs.getInt("distance"),
                        rs.getString("style"),
                        rs.getInt("total_participants")
                );
                logger.info("Race found: {}", race);
                return race;
            }
        } catch (SQLException e) {
            logger.error("Error finding race: {}", e.getMessage());
        }
        return null;
    }

    @Override
    public Collection<Race> findAll() {
        List<Race> races = new ArrayList<>();
        String sql = "SELECT * FROM Races";
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                Race race = new Race(rs.getInt("race_id"), rs.getInt("distance"), rs.getString("style"), rs.getInt("totalParticipants"));
                races.add(race);
            }
            logger.info("Fetched {} races", races.size()); // Log the number of races fetched
        } catch (SQLException e) {
            logger.error("Error fetching all races: {}", e.getMessage()); // Log the error
        }
        return races;
    }
}