package dao;

import model.*;
import java.sql.*;
import java.util.*;

public class BombDAO extends DatabaseConnection {

    public Bomb loadRandomBomb() throws SQLException {
        String sqlBomb = "SELECT b.bomb_id, b.serial_code, b.max_strikes, b.time_limit, w.wire_index FROM bomb b LEFT JOIN bomb_wire w ON w.bomb_id = b.bomb_id ORDER BY RAND() LIMIT 1"; //acak pilih satu bomb
        
        try (Connection c = getConnection();
            PreparedStatement psBomb = c.prepareStatement(sqlBomb);
            ResultSet rsBomb = psBomb.executeQuery()) {

            if (!rsBomb.next()) return null; // jika tidak ada bomb

            int bombId      = rsBomb.getInt("bomb_id");
            String serial   = rsBomb.getString("serial_code");
            int maxStrikes  = rsBomb.getInt("max_strikes");
            int timeLimit = rsBomb.getInt("time_limit");
            int correctWireIndex = rsBomb.getInt("wire_index");

            List<BombModule> modules = new ArrayList<>();
            
            WireModule wireModule = new WireModule(correctWireIndex);
            modules.add(wireModule);

            // nanti kalau sudah ada button/keypad/simon:
            // modules.add(new ButtonModule(...));
            // modules.add(new KeypadModule(...));
            // modules.add(new SimonModule(...));

            return new Bomb(bombId, serial, maxStrikes, timeLimit, modules);
        }
    }

    // private BombModule createModuleByName(String name) {
    //     switch (name.toUpperCase()) {
    //         case "WIRE":
    //             return new W;
    //         // case "BUTTON":
    //         //     return new ButtonModule(bomb);
    //         // case "KEYPAD":
    //         //     return new KeypadModule(bomb);
    //         // case "SIMON":
    //         //     return new SimonModule(bomb);
    //         default:
    //             throw new IllegalArgumentException("Unknown module type: " + name);
    //     }
    // }
}
