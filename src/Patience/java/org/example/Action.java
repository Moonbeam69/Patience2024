package org.example;

public class Action {
    String Description;
    Integer Priority;

    public String type;

    public Card playableCard;
    public Card targetCard;
    static String SPARE = "SPARE";
    static String GAME = "GAME";
    static String STACK = "STACK";

    public String getDescription() {
        int a = 1;
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Integer getPriority() {
        return Priority;
    }

    public void setPriority(Integer priority) {
        Priority = priority;
    }

    private void testme() {

    }
}
