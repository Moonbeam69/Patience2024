package org.example;

public class Card {
    public String value;
    public Boolean visible;
    public String location; // STACK | GAME | SPARE
    String SPARE = "SPARE";
    String GAME = "GAME";
    String STACK = "STACK";

    public Card (String value, Boolean visible) {
        this.value = value;
        this.visible = visible;
        this.location = SPARE;
    }

    public Card (String value, Boolean visible, String location) {
        this.value = value;
        this.visible = visible;
        this.location = location;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {

        if (location.equals(STACK) || location.equals(GAME) || location.equals(SPARE) ) {
            this.location = location;
        } else {
            System.out.println("Set location incorrect value. Expecting STACK|GAME|SPARE. Got: " + location );
        }
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public String getValue() {
        return value;
    }

    public String getSuit() {
        return value.substring(0,1);
    }

    public int getNumber() {
        int ret = 0;
        String val = value.substring(1, value.length());

        if (val.equals("X") || val.equals("A") || val.equals("J") || val.equals("Q") || val.equals("K")) {
            if (val.equals("X")) ret = 10;
            if (val.equals("A")) ret = 1;
            if (val.equals("J")) ret = 11;
            if (val.equals("Q")) ret = 12;
            if (val.equals("K")) ret = 13;
            if (val.equals("0")) ret = 0;
        } else {
            ret = Integer.parseInt(val);
        }

        return ret;
    }
}
