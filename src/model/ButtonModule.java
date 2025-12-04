package model;

public class ButtonModule extends BombModule {

	public static enum ButtonAction {
        TAP,
        HOLD_RELEASE_ON_1,
        HOLD_RELEASE_ON_4
    }
	
    private final Button button;
    private final ButtonAction baseAction;
    private ButtonAction requiredAction;

    public ButtonModule(Button button, ButtonAction baseAction) {
        super("BUTTON");
        this.button = button;
        this.baseAction = baseAction;
    }

    public Button getButton() {
        return button;
    }

    public ButtonAction getRequiredAction() {
        return requiredAction;
    }

    @Override
    public void applySerialRules(String serial) {
        if (serial == null) {
            throw new IllegalArgumentException("Serial tidak boleh null");
        }

        String upperSerial = serial.toUpperCase().trim();
        // ambil digit terakhir
        int lastDigit = 0;
        if (!upperSerial.isEmpty()) {
            char lastChar = upperSerial.charAt(upperSerial.length() - 1);
            if (Character.isDigit(lastChar)) {
                lastDigit = lastChar - '0';   // '0'..'9' → 0..9
            } else {
                lastDigit = 0;  // jika terakhir huruf/simbol → 0
            }
        }
        // mapping baseAction -> baseValue
        int baseValue;

        switch (baseAction) {
            case TAP:
                baseValue = 0;
                break;
            case HOLD_RELEASE_ON_1:
                baseValue = 1;
                break;
            case HOLD_RELEASE_ON_4:
                baseValue = 4;
                break;
            default:
                baseValue = 0;
        }

        // hitung sum & tentukan requiredAction
        int sum = baseValue + lastDigit;
        int r = sum % 3;

        switch (r) {
            case 0:
                requiredAction = ButtonAction.TAP;
                break;
            case 1:
                requiredAction = ButtonAction.HOLD_RELEASE_ON_1;
                break;
            case 2:
            default:
                requiredAction = ButtonAction.HOLD_RELEASE_ON_4;
                break;
        }
    }

    public boolean tap() {
        boolean ok = (requiredAction == ButtonAction.TAP);
        if (ok) {
            solvedStatus = true;
        }
        return ok;
    }

    public boolean solveByHoldSeconds(long roundedSeconds) {
        boolean ok = false;

        switch (requiredAction) {
            case HOLD_RELEASE_ON_1:
                ok = (roundedSeconds == 1);
                break;
            case HOLD_RELEASE_ON_4:
                ok = (roundedSeconds == 4);
                break;
            case TAP:
                ok = false;
                break;
        }

        if (ok) {
            solvedStatus = true;
        }
        return ok;
    }
}
