package repository;

import model.Race;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RaceRepository implements IRepository<Integer, Race> {
    private static final Logger logger = LogManager.getLogger(RaceRepository.class); // Logger instance
    private Connection connection;

    public RaceRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int size() {
        String sql = "SELECT COUNT(*) FROM races";
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
        String sql = "INSERT INTO Race (id, distance, style, totalParticipants) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, race.getRaceId());
            statement.setInt(2, race.getDistance());
            statement.setString(3, race.getStyle());
            statement.setInt(4, race.getTotalParticipants());
            statement.executeUpdate();
            logger.info("Race saved: {}", race); // Log the saved race
        } catch (SQLException e) {
            logger.error("Error saving race: {}", e.getMessage()); // Log the error
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM Race WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            logger.info("Race deleted with ID: {}", id); // Log the deleted race ID
        } catch (SQLException e) {
            logger.error("Error deleting race: {}", e.getMessage()); // Log the error
        }
    }

    @Override
    public void update(Integer id, Race race) {
        String sql = "UPDATE Race SET distance = ?, style = ?, totalParticipants = ? WHERE id = ?";
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
        String sql = "SELECT * FROM Race WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Race race = new Race(rs.getInt("id"), rs.getInt("distance"), rs.getString("style"), rs.getInt("totalParticipants"));
                logger.info("Race found: {}", race); // Log the found race
                return race;
            }
        } catch (SQLException e) {
            logger.error("Error finding race: {}", e.getMessage()); // Log the error
        }
        return null;
    }

    @Override
    public Iterable<Race> findAll() {
        List<Race> races = new ArrayList<>();
        String sql = "SELECT * FROM Race";
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                Race race = new Race(rs.getInt("id"), rs.getInt("distance"), rs.getString("style"), rs.getInt("totalParticipants"));
                races.add(race);
            }
            logger.info("Fetched {} races", races.size()); // Log the number of races fetched
        } catch (SQLException e) {
            logger.error("Error fetching all races: {}", e.getMessage()); // Log the error
        }
        return races;
    }
}