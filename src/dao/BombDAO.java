package dao;

import model.*;
import java.sql.*;
import java.util.*;

public class BombDAO extends DatabaseConnection {

    public Bomb loadRandomBomb() throws SQLException {
        String sqlBomb = """
            SELECT b.bomb_id, b.serial_code, b.max_strikes, b.time_limit
            FROM bomb b
            ORDER BY RAND()
            LIMIT 1
        """;
        
        try (Connection c = getConnection();
            PreparedStatement psBomb = c.prepareStatement(sqlBomb);
            ResultSet rsBomb = psBomb.executeQuery()) {

            if (!rsBomb.next()) return null; // jika tidak ada bomb

            int bombId     = rsBomb.getInt("bomb_id");
            String serial  = rsBomb.getString("serial_code");
            int maxStrikes = rsBomb.getInt("max_strikes");
            int timeLimit  = rsBomb.getInt("time_limit");

            // load wire module
            String sqlWire = """
                SELECT wire_index, wire_color
                FROM bomb_wire
                WHERE bomb_id = ?
                ORDER BY wire_index ASC
            """;

            List<Wire> wires = new ArrayList<>();

            try (PreparedStatement psWire = c.prepareStatement(sqlWire)) {
                psWire.setInt(1, bombId);
                try (ResultSet rsWire = psWire.executeQuery()) {
                    while (rsWire.next()) {
                        String color = rsWire.getString("wire_color"); 
                        wires.add(new Wire(color, false, false));
                    }
                }   
            }
            
            List<BombModule> modules = new ArrayList<>();

            if (!wires.isEmpty()) {
                WireModule wireModule = new WireModule(wires);
                wireModule.applySerialRules(serial);   
                modules.add(wireModule);
            }

            // load button module
            String sqlButton = """
                SELECT button_color, button_label, solve_type
                FROM bomb_button
                WHERE bomb_id = ?
                LIMIT 1
            """;

            try (PreparedStatement psButton = c.prepareStatement(sqlButton)) {
                psButton.setInt(1, bombId);
                try (ResultSet rsButton = psButton.executeQuery()) {
                    if (rsButton.next()) {
                        String btnColor = rsButton.getString("button_color");
                        String btnLabel = rsButton.getString("button_label");
                        String solveType = rsButton.getString("solve_type");
                        Button button = new Button(btnColor, btnLabel);
                        
                        ButtonModule.ButtonAction baseAction;
                        switch (solveType.toUpperCase().trim()) {
                            case "TAP":
                                baseAction = ButtonModule.ButtonAction.TAP;
                                break;
                            case "HOLD1":
                                baseAction = ButtonModule.ButtonAction.HOLD_RELEASE_ON_1;
                                break;
                            case "HOLD4":
                                baseAction = ButtonModule.ButtonAction.HOLD_RELEASE_ON_4;
                                break;
                            default:
                                baseAction = ButtonModule.ButtonAction.TAP; // fallback aman
                        }
                        ButtonModule buttonModule = new ButtonModule(button, baseAction);
                        buttonModule.applySerialRules(serial);
                        modules.add(buttonModule);
                    }
                }
            }

            return new Bomb(bombId, serial, maxStrikes, timeLimit, modules);
        }
    }
}
