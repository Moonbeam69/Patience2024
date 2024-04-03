package org.example;

import java.util.*;
import static org.example.Constants.*;

// added a comment
public class Action {
    String Description;
    Integer Priority;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String type;

    public Card playableCard;
    public Card targetCard;


    public Optional<Object> createtype (String suggestedtype)  {

        if (suggestedtype.equals(GAMEGAME) ||
                suggestedtype.equals(GAMESTACK) ||
                    suggestedtype.equals(SPAREGAME) ||
                        suggestedtype.equals(SPARESTACK)) {
            return Optional.of(suggestedtype);
        }
        return Optional.empty();

    }

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

    public void testme() {
        int a=1;
        if (a>0) {
            int b = 2;
        } else {
            int c = 3;
        }

    }
}
