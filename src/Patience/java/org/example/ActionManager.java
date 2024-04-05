package org.example;

import java.util.*;

import static org.example.Constants.GAME;

public class ActionManager {

    private GameDeck        gamedeck;
    private List<String>    historicalActions                   = new ArrayList<String>();
    private List<Card>      playableCards                       = new ArrayList<Card>();
    private List<Card>      targetCards                         = new ArrayList<Card>();
    ArrayList<String>       processDirectives;
    private boolean         print;
    private boolean         PRIORITISE_EMPTY_COLUMN_CREATION;
    ArrayList<Action>       actions;
    ArrayList<String>       forbiddenActions                    = new ArrayList<String>();
    public int              no_of_performed_actions             = 0;
    ArrayList<Action>       allActions;



    public ActionManager(GameDeck gameDeck, ArrayList<String> processDirectives, boolean print, boolean PRIORITISE_EMPTY_COLUMN_CREATION) {
        this.gamedeck = gameDeck;
        this.print = print;
        this.PRIORITISE_EMPTY_COLUMN_CREATION = PRIORITISE_EMPTY_COLUMN_CREATION;
        this.processDirectives = processDirectives;
    }

    public ArrayList<Action> determinePossibleActions() {

        allActions = new ArrayList<Action>();
        playableCards = new ArrayList();

        Card firstcandidate = null;
        Card lastcandidate = null;

        for (int col = 0; col < 7; col++) {
            for (int row = 0; row < 20; row++) {
                if (gamedeck.gamedeck[col][row] != null) {
                    if (gamedeck.gamedeck[col][row].getVisible()) {
                        firstcandidate = gamedeck.gamedeck[col][row];
                        break;
                    }
                }
            }
            for (int row = 19; row >= 0; row--) {
                if (gamedeck.gamedeck[col][row] != null) {
                    if (gamedeck.gamedeck[col][row].getVisible()) {
                        lastcandidate = gamedeck.gamedeck[col][row];
                        break;
                    }
                }
            }
            // make sure if there is only one card in column, it is not recognised twice
            if (firstcandidate != null && lastcandidate != null) {
                if (firstcandidate.getValue().equals(lastcandidate.getValue())) {
                    playableCards.add(firstcandidate);
                } else {
                    playableCards.add(lastcandidate);
                    playableCards.add(firstcandidate);
                }
            }
        }

        // add the indexed spare
        if (gamedeck.getSpareDeckCard() != null) playableCards.add(gamedeck.getSpareDeckCard());

        // find all possible targets cards
        targetCards = new ArrayList();

        for (int col = 0; col < 7; col++) {
            for (int row = 19; row > -1; row--) {
                if (gamedeck.gamedeck[col][row] != null && gamedeck.gamedeck[col][row].getVisible()) {
                    targetCards.add(gamedeck.gamedeck[col][row]);
                    break; // in each column just the first visible card
                }
                if (gamedeck.gamedeck[col][row] == null && row == 0) {
                    targetCards.add(new org.example.Card("_" + col, false, GAME));  // adds an empty column as a destination - should this be done in GameDeck?
                    break; // in each column just the first visible card
                }
            }
        }

        // last card on each <suite>Stack is a target card
        targetCards.add(gamedeck.getlast(gamedeck.heartsStack)); // get "last" card on stack
        targetCards.add(gamedeck.getlast(gamedeck.diamondsStack)); // get "last" card on stack
        targetCards.add(gamedeck.getlast(gamedeck.spadesStack)); // get "last" card on stack
        targetCards.add(gamedeck.getlast(gamedeck.clubsStack)); // get "last" card on stack

        // determine possible actions

        for (org.example.Card playablecard : playableCards) {
            for (org.example.Card targetcard : targetCards) {

                if (gamedeck.isAllowed(playablecard, targetcard)) {

                    Action action = new Action();
                    action.playableCard = playablecard;
                    action.targetCard = targetcard;
                    action.type = (String)action.createtype(playablecard.getLocation() + targetcard.getLocation()).get();
                    action.setDescription(playablecard.getValue() + ":" + targetcard.getValue());

                    allActions.add(action);
                }
            }
        }


        ArrayList<Action> filteredActions = filteredActions = new ArrayList<>();
        if (PRIORITISE_EMPTY_COLUMN_CREATION) {

            Iterator<org.example.Card> itr1 = targetCards.iterator();

            while (itr1.hasNext()) {
                org.example.Card nexttargetcard = itr1.next();
                Iterator<Action> itr2 = allActions.iterator();
                ArrayList<Action> tempList = new ArrayList<>();
                while (itr2.hasNext()) {
                    Action matchedaction = itr2.next();
                    if (nexttargetcard.getValue().equals(matchedaction.targetCard.getValue())) {
                        tempList.add(matchedaction);
                    }
                }
                Iterator<Action> itr3 = tempList.iterator();
                Action action_creates_empty_row = null;
                if (tempList.size() > 0) {
                    while (itr3.hasNext()) {
                        Action action = itr3.next();
                        if (gamedeck.getRow(action.playableCard) == 0) {
                            action_creates_empty_row = action;
                        }
                    }
                    if (action_creates_empty_row == null) {
                        filteredActions.addAll(tempList);
                    } else {
                        filteredActions.add(action_creates_empty_row);
                    }
                }
            }
        } else {
            filteredActions = allActions;
        }

        Iterator<Action> its = allActions.iterator();
        while (its.hasNext()) {
            Action action = its.next();
            if (forbiddenActions.contains(action.getDescription())) {
                its.remove();
            }
        }


        return filteredActions;

    }

    public ArrayList<Action> doNextAction() {

        // find and do the next permitted action in order of the directives
        Action nextAction = null;
        for (String directive: processDirectives) {
            nextAction = getNextActionOfType(directive);
            if (nextAction != null) {
                if (!forbiddenActions.contains(nextAction.getDescription())) {
                    if (print) System.out.println("Doing: " + nextAction.getDescription());
                    no_of_performed_actions++;
                    gamedeck.move(nextAction.playableCard, nextAction.targetCard);
                    forbiddenActions.add(nextAction.getDescription());
                    break;
                }
            }
        }
        if (nextAction != null) {
            allActions.remove(nextAction);
        }
        return allActions;

    }

    public Action getNextActionOfType(String type) {
        for (Action action: allActions) {
            if (action.type.equals(type)) return action;
        }

        return null;
    }
    public void PrintAll(String message) {
        if (print) {
            System.out.println();
            System.out.println(message);
            System.out.println("===========");
            System.out.print("Playable: ");
            for (org.example.Card card : playableCards) System.out.print(card.getValue() + " ");
            System.out.println();
            System.out.print("Target:   ");
            for (org.example.Card card : targetCards) System.out.print(card.getValue() + " ");
            System.out.println();

            gamedeck.printGameDeck();
            gamedeck.printSpareDeck();
            gamedeck.printStacks();
            System.out.println();
        }
    }

    public void PrintActions(String message) {
        if (print) {
            System.out.println();
            System.out.println(message);
            for (Action action : allActions) {
                System.out.println(action.playableCard.getValue() + " to " + action.targetCard.getValue() + " " + action.type);
            }
            System.out.println();
        }
    }

}
