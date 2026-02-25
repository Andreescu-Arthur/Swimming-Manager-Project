package repository;

import model.Race;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component

public class RaceRepository implements IRepository<Integer, Race> {

    private static final Logger logger = LogManager.getLogger(RaceRepository.class);



    private Connect connect;



    public RaceRepository(Connect connect) {

        this.connect = connect;

    }



    public RaceRepository() {

    }



    @Override

    public int size() {

        String sql = "SELECT COUNT(*) FROM Races";

        try (Connection connection = connect.getConnection();

             PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {

                int count = rs.getInt(1);

                logger.info("Fetched race count: {}", count);

                return count;

            }

        } catch (SQLException e) {

            logger.error("Error getting size: {}", e.getMessage());

        }

        return 0;

    }



    @Override

    public void save(Race race) {

        String sql = "INSERT INTO Races (distance, style, total_participants) VALUES (?, ?, ?)";

        try (Connection connection = connect.getConnection();

             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, race.getDistance());

            statement.setString(2, race.getStyle());

            statement.setInt(3, race.getTotalParticipants());

            statement.executeUpdate();



            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {

                    int generatedId = generatedKeys.getInt(1);

                    race.setRaceId(generatedId);

                    System.out.println("Inserted race with ID: " + generatedId);

                }

            }

        } catch (SQLException e) {

            System.err.println("Error saving race: " + e.getMessage());

        }

    }



    @Override

    public void delete(Integer race_id) {

        String sql = "DELETE FROM Races WHERE race_id = ?";

        try (Connection connection = connect.getConnection();

             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, race_id);

            statement.executeUpdate();

            logger.info("Race deleted with ID: {}", race_id);

        } catch (SQLException e) {

            logger.error("Error deleting race: {}", e.getMessage());

        }

    }



    @Override

    public void update(Integer race_id, Race race) {

        String sql = "UPDATE Races SET distance = ?, style = ?, total_Participants = ? WHERE race_id = ?";

        try (Connection connection = connect.getConnection();

             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, race.getDistance());

            statement.setString(2, race.getStyle());

            statement.setInt(3, race.getTotalParticipants());

            statement.setInt(4, race.getRaceId());

            statement.executeUpdate();

            logger.info("Race updated: {}", race);

        } catch (SQLException e) {

            logger.error("Error updating race: {}", e.getMessage());

        }

    }



    @Override

    public Race findOne(Integer id) {

        String sql = "SELECT * FROM Races WHERE race_id = ?";

        try (Connection connection = connect.getConnection();

             PreparedStatement statement = connection.prepareStatement(sql)) {

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



    public int getParticipantCountForRace(int raceId) {

        String sql = "SELECT COUNT(*) AS count FROM Registrations WHERE race_id = ?";

        try (Connection connection = connect.getConnection();

             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, raceId);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {

                return rs.getInt("count");

            }

        } catch (SQLException e) {

            System.err.println("Error fetching participant count: " + e.getMessage());

        }

        return 0;

    }



    @Override

    public Collection<Race> findAll() {

        List<Race> races = new ArrayList<>();

        String sql = """

        SELECT r.race_id, r.distance, r.style, 

               (SELECT COUNT(*) FROM Registrations reg WHERE reg.race_id = r.race_id) AS total_participants

        FROM Races r

    """;



        try (Connection connection = connect.getConnection();

             PreparedStatement statement = connection.prepareStatement(sql);

             ResultSet rs = statement.executeQuery()) {



            while (rs.next()) {

                int raceId = rs.getInt("race_id");

                int distance = rs.getInt("distance");

                String style = rs.getString("style");

                int totalParticipants = rs.getInt("total_participants");



                System.out.println("Race loaded: ID=" + raceId + ", Distance=" + distance + ", Style=" + style + ", Participants=" + totalParticipants);

                races.add(new Race(raceId, distance, style, totalParticipants));

            }

        } catch (SQLException e) {

            System.err.println("Error fetching races: " + e.getMessage());

        }

        return races;

    }

}
