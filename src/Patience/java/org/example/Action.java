package org.example;

public class Action {
    String Description;
    Integer Priority;

    public String type;

    public Card playableCard;
    public Card targetCard;
    String SPARE = "SPARE";
    String GAME = "GAME";
    String STACK = "STACK";

    public String getDescription() {
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
}
