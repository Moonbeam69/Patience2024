package org.example;

import java.util.*;

import static org.example.Constants.*;

// Add some commentary here
public class Player {
    ArrayList<Action>                   Actions;
    ArrayList<String>                   historicalActions;
    GameDeck                            gamedeck;
    int                                 moveCount;
    int                                 GameNo;
    Boolean                             print;
    ArrayList<org.example.Card>         playableCards;
    ArrayList<org.example.Card>         targetCards;
    Action                              nextAction = null;
    int                                 no_of_wins = 0;
    int                                 maxplays = 0;

    GameState                           gamestate;
    ArrayList<GameState>                gamestatehistory = new ArrayList<GameState>();
    ArrayList <ArrayList<GameState>>    allgamestatehistory = new ArrayList<ArrayList<GameState>>();

    public static void main(String[] args) {
        Player main = new Player();
    }

    // Backlog
    // some backlog item
    // Working on

    // Bugs

    // Done
    {
        // need to make sure higher prob. that aces are in the gamedeck not the sparedeck
        //6. Need a to define, create, track and report KPIs
        // occasionally, game execution does not work. cards are not played to the stacks and score remains at 0 even though actions on deck are completed. Problem in isAllowed?
        // Ace (any card?) in last place on spare deck does not get played
        // if no actions were done for sparedeckindex=0 to sparedeckindex=sparedeck.size quit game
        //3. Need to be able  inject specific test data / cases
        // Hearts cards are added to stack in 2-4-6 fashion. Other suits are fine. Investigate: "Index 13 out of bounds for length 13" at move 50
        // Of a group of visible cards (in a single column) in the deck, only the bottom card can be played to the stack.
        // GAMEGAME actions randomly do not set the newly exposed card in the playable column to visible
        // 1. Only king can move into empty columns
        //2. First Sparedeck card can be played or next card selected
        //4. Need a mechanism to collate all actions and assign and process priorities
        //5. add spare card logic (select & choose next)
        // first AND last of visible cards are playable not just first - DONE
        // if last Spare  card is played, get index out of bounds fatal error - CLOSED
        // prevent infinitely switching two valid target cards
        //    SJ CJ  ->  SJ CJ etc
        //    HX            HX
        // BUILD IN HYSTERESIS? What are the rules? Simply prebent the same action (from:to) in the Actions list? How to recognise switching?
        // infinite loops possible (last of visible cards does not move to other col in GAMEGAME move: action is recognised but not implemented)
    }

    Player() {

        maxplays = 10000;
        print    = false;
        final Boolean PRIORITISE_EMPTY_COLUMN_CREATION = true;


        ArrayList<String> results = new ArrayList<>();
        ArrayList<Integer> scores = new ArrayList<Integer>();
        gamestatehistory = new ArrayList<GameState>();

        System.out.println("Print setting: " + print);

        // Number of games to play
        for (GameNo = 1; GameNo <= maxplays; GameNo++) {

            // GameDeck usage: GameDeck(file_to_use)  or GameDeck("") to create new file c:\\data\\PatiencelatestGame.txt
            //gamedeck = new GameDeck("c:\\data\\PatienceTestData1.txt");
            gamedeck = new GameDeck("");
            historicalActions = new ArrayList<String>();

            moveCount                   = 0;
            int no_of_performed_actions = 0;
            Boolean DoNextMove          = true;

            // Gameloop
            while (DoNextMove) {

                if (moveCount==400) System.exit(0);
                if (print) System.out.println("GameHistory No/No Considered Moves/No Performed Moves: " + GameNo + "/" + moveCount + "/" + no_of_performed_actions);

                Actions         = new ArrayList<Action>();
                playableCards   = new ArrayList();
                gamestate       = new GameState();

                org.example.Card firstcandidate= null;
                org.example.Card lastcandidate = null;

                for (int col = 0; col < 7; col++) {
                    for (int row = 0; row < 20; row++) {
                        //System.out.println(col + ":" + row);
                        if (gamedeck.gamedeck[col][row] != null) {
                            if (gamedeck.gamedeck[col][row].getVisible()) {
                                firstcandidate = gamedeck.gamedeck[col][row];
                                break;
                            }
                        }
                    }
                    for (int row = 19; row >= 0; row--) {
                        //System.out.println(col + ":" + row);
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
                        if (gamedeck.gamedeck[col][row] == null && row == 0 ) {
                            targetCards.add(new org.example.Card("_" + col, false, GAME));  // adds an empty column as a destination - should this be done in GameDeck?
                            break; // in each column just the first visible card
                        }
                    }
                }

                // last card on each suiteStack is a target card
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
                            action.type = playablecard.getLocation() + targetcard.getLocation();
                            action.setDescription(playablecard.getValue() + ":" + targetcard.getValue());

                            Actions.add(action);
                        }
                    }
                }

                PrintAll ("Before action");
                PrintActions("All possible actions");

                if (PRIORITISE_EMPTY_COLUMN_CREATION) {

                    ArrayList<Action> filteredActions = new ArrayList<>();
                    Iterator<org.example.Card> itr1 = targetCards.iterator();
                    while (itr1.hasNext()) {
                            org.example.Card nexttargetcard = itr1.next();
                            Iterator<Action> itr2 = Actions.iterator();
                            ArrayList<Action> tempList = new ArrayList<>();
                            while (itr2.hasNext()) {
                                Action matchedaction = itr2.next();
                                if (nexttargetcard.getValue().equals(matchedaction.targetCard.getValue())) {
                                    tempList.add(matchedaction);
                                }
                            }
                            Iterator<Action> itr3 = tempList.iterator();
                            Action action_creates_empty_row = null;
                            if (tempList.size()>0) {
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

                    Actions = filteredActions;
                }

                Iterator<Action> its = Actions.iterator();
                while (its.hasNext()) {
                    Action action = its.next();
                    if (historicalActions.contains(action.getDescription())) {
                        its.remove();
                    }
                }

                PrintActions ("All filtered actions");

                nextAction = null;
                try {
                    processactions:
                    {
                        nextAction = getNextActionType("SPARESTACK");
                        if (nextAction != null) {

                            if (!historicalActions.contains(nextAction.getDescription())) {
                                if (print) System.out.println("Doing: " + nextAction.getDescription());
                                no_of_performed_actions++;
                                gamedeck.move(nextAction.playableCard, nextAction.targetCard);
                                break processactions; // break block
                            }
                        }

                        nextAction = getNextActionType("GAMESTACK");
                        if (nextAction != null) {

                            if (!historicalActions.contains(nextAction.getDescription())) {
                                if (print) System.out.println("Doing: " + nextAction.getDescription());
                                no_of_performed_actions++;
                                gamedeck.move(nextAction.playableCard, nextAction.targetCard);
                                break processactions; // break block
                            }
                        }

                        nextAction = getNextActionType("SPAREGAME");
                        if (nextAction != null) {

                            if (!historicalActions.contains(nextAction.getDescription())) {
                                if (print) System.out.println("Doing: " + nextAction.getDescription());
                                no_of_performed_actions++;
                                gamedeck.move(nextAction.playableCard, nextAction.targetCard);
                                break processactions; // break block
                            }
                        }

                        nextAction = getNextActionType("GAMEGAME");
                        if (nextAction != null) {

                            if (!historicalActions.contains(nextAction.getDescription())) {
                                if (print) System.out.println("Doing: " + nextAction.getDescription());
                                no_of_performed_actions++;
                                gamedeck.move(nextAction.playableCard, nextAction.targetCard);
                                break processactions; // break block
                            }
                        }
                      }

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                // record current action
                if (nextAction!=null) historicalActions.add(nextAction.getDescription());


                // determine end-of-game (game loop) conditions
                if (Actions.size() > 0 && gamedeck.sparedeckindex <= gamedeck.sparedeck.size()-2) { // more to go
                    DoNextMove = true;
                    moveCount++;
                    if (print) System.out.println("# performed actions: "+ no_of_performed_actions);

                } else if (gamedeck.sparedeckindex >= gamedeck.sparedeck.size()-1 && no_of_performed_actions == 0) {
                    DoNextMove = false;

                } else if (Actions.size() == 0 && (gamedeck.sparedeckindex <= gamedeck.sparedeck.size()-1 || gamedeck.sparedeckindex == gamedeck.sparedeck.size())) { // no actions & reached penultimate sparedeckindex
                    gamedeck.sparedeckindex++; // move to next index sparedeck card

                    if (gamedeck.sparedeckindex > gamedeck.sparedeck.size()-1 ) { // for past last card
                        gamedeck.sparedeckindex=0;
                        no_of_performed_actions = 0;
                    }
                    if (print) System.out.println("# action: "+ no_of_performed_actions);
                    DoNextMove = true;
                    moveCount++;
                }

                // package game data for later analysis
                gamestate.gamedeck = gamedeck;
                gamestate.action = nextAction;
                gamestatehistory.add(gamestate);
            }

            allgamestatehistory.add(gamestatehistory);

            int score = gamedeck.heartsStack.size()-1 + gamedeck.diamondsStack.size()-1 + gamedeck.spadesStack.size()-1 + gamedeck.clubsStack.size()-1;

            results.add("Games: " + GameNo + ", score: " + score + "/52");
            scores.add(score);

            if (score == 52) {
//                PrintAll("Jackpot", true);
                no_of_wins++;
            }


        }



        //if (print) {
//            for (String result : results) {
//                System.out.println(result);
//            }
        //}
        float avg = 0f;
        for (int score: scores) {
            avg += score;
        }
        System.out.println("Number of games: " + --GameNo);
        System.out.println("average score: " + avg/ scores.size());
        System.out.println("max score: " + Collections.max(scores));
        System.out.println("no of wins: " + no_of_wins);

        System.out.println("wins/total plays: " + 100*(float)no_of_wins/maxplays + "%");


        //System.out.println(allgamestatehistory.get(0).get(0).gamedeck.gamedeck[0][0].getValue());


    }

    public Action getNextActionType(String type) {
        for (Action action: Actions) {
            if (action.type.equals(type)) return action;
        }

        return null;
    }

    public Action getNextGameStack() {
        for(Action action: Actions ) {

            if (action.type.equals("GAMESTACK")) {
                return action;
            }
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

    public void PrintAll(String message, Boolean Override) {

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


    public void PrintActions() {
        if (print) {
            System.out.println();
            System.out.println("Possible actions:");
            for (Action action : Actions) {
                System.out.println(action.playableCard.getValue() + " to " + action.targetCard.getValue() + " " + action.type);
            }
            System.out.println();
        }
    }

    public void PrintActions(String message) {
        if (print) {
            System.out.println();
            System.out.println(message);
            for (Action action : Actions) {
                System.out.println(action.playableCard.getValue() + " to " + action.targetCard.getValue() + " " + action.type);
            }
            System.out.println();
        }
    }
}