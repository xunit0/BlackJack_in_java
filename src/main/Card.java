package main;

public class Card {

    private String value;
    private String type;

    public Card(String value, String type) {
        this.value = value;
        this.type = type;
    }

    public int getValue() {
        if ("AJQK".contains(value)) { //A J Q K
            if (value.equals("A")) {
                return 11;
            } else {
                return 10;
            }
        } else {
            return Integer.parseInt(value);
        }
    }

    @Override
    public String toString() {
        return value + "-" + type;
    }

    public boolean isAce() {
        return value.equals("A");
    }

    public String getImagePath() {
        return "/res/cards/" + toString() + ".png";
    }
}
