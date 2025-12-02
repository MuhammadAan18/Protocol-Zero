package dao;

import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreDAO {

    public static void insertScore(int userId, int bombId, int strikeLeft, int timeLeft, int maxStrike) {
        String gameScore = ScoreUtil.calculateGameScore(strikeLeft, timeLeft, maxStrike);

        String sql = "INSERT INTO score (user_id, bomb_id, strike_left, time_left, game_score) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, bombId);
            ps.setInt(3, strikeLeft);
            ps.setInt(4, timeLeft);
            ps.setString(5, gameScore);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<ScoreEntry> getTopScores(int limit) {
        List<ScoreEntry> list = new ArrayList<>();

        String sql =
            "SELECT " +
        "   s.score_id, " +
        "   s.user_id, " +
        "   u.username, " +
        "   s.bomb_id, " +
        "   s.strike_left, " +
        "   s.time_left, " +
        "   s.game_score " +
        "FROM score s " +
        "JOIN user u ON s.user_id = u.user_id " +
        "ORDER BY " +
        "   CASE s.game_score " +
        "       WHEN 'SS' THEN 5 " +
        "       WHEN 'A'  THEN 4 " +
        "       WHEN 'B'  THEN 3 " +
        "       WHEN 'C'  THEN 2 " +
        "       WHEN 'F'  THEN 1 " +
        "       ELSE 0 " +
        "   END DESC, " +
        "   s.strike_left DESC, " +
        "   s.time_left DESC " +
        "LIMIT ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ScoreEntry e = new ScoreEntry();
                    e.setScoreId(rs.getInt("score_id"));
                    e.setUserId(rs.getInt("user_id"));
                    e.setUsername(rs.getString("username"));
                    e.setBombId(rs.getInt("bomb_id"));
                    e.setStrikeLeft(rs.getInt("strike_left"));
                    e.setTimeLeft(rs.getInt("time_left"));
                    e.setGameScore(rs.getString("game_score"));
                    list.add(e);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // menghitung score yang didapat
    public static class ScoreUtil {

        public static String calculateGameScore(int strikeLeft, int timeLeft, int maxStrike) {
            double strikeRatio = (double) strikeLeft / (double) maxStrike; 
            double timeRatio   = (double) timeLeft / 300.0;  

            double combined = (strikeRatio * 0.5) + (timeRatio * 0.5);

            if (combined >= 0.9) {
                return "SS";
            } else if (combined >= 0.8) {
                return "A";
            } else if (combined >= 0.65) {
                return "B";
            } else if (combined > 0.0) {
                return "C";
            } else {
                return "F";
            }
        }
    }
}
