package org.example;

import java.util.*;
import static org.example.Constants.*;

// added a comment
public class Action {
    private String Description;
    private Integer Priority;
    public String type;
    public Card playableCard;
    public Card targetCard;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


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

    @Override
    public boolean equals(Object object) {

        if(this.hashCode()==object.hashCode()) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getDescription());
    }
}
