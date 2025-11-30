package model;

import java.util.List;

public class KeypadColumns {

    public static final Keypad S1  = new Keypad("S1",  "BOMB");
    public static final Keypad S2  = new Keypad("S2",  "ACCESS");
    public static final Keypad S3  = new Keypad("S3",  "SYSTEM");
    public static final Keypad S4  = new Keypad("S4",  "MISSION");
    public static final Keypad S5  = new Keypad("S5",  "ERROR");
    public static final Keypad S6  = new Keypad("S6",  "DATA");
    public static final Keypad S7  = new Keypad("S7",  "SIGNAL");
    public static final Keypad S8  = new Keypad("S8",  "REBOOTING");
    public static final Keypad S9  = new Keypad("S9",  "CALM");
    public static final Keypad S10 = new Keypad("S10", "WIRES");
    public static final Keypad S11 = new Keypad("S11", "CUT");
    public static final Keypad S12 = new Keypad("S12", "HOLD");
    
	// 4 kombinasi nya
    public static final List<Keypad> COLUMN_1 = List.of(S1, S8, S4, S5, S10, S11);
    public static final List<Keypad> COLUMN_2 = List.of(S2, S3, S5, S7, S11, S1);
    public static final List<Keypad> COLUMN_3 = List.of(S12, S10, S8, S6, S4, S2);
    public static final List<Keypad> COLUMN_4 = List.of(S5, S6, S2, S4, S9, S11);

    public static final List<List<Keypad>> ALL_COLUMNS = List.of(COLUMN_1, COLUMN_2, COLUMN_3, COLUMN_4);
}
