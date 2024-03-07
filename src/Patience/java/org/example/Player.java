package org.example;

import java.util.*;

public class Player {
    ArrayList<Action> Actions = new ArrayList<Action>();

    public static void main(String[] args) {
        Player main = new Player();
    }

    // Backlog
    //6. Need a to define, create, track and report KPIs

    // Working on
    // testing and fixing:

    // Bugs:
    // occasionally, game execution does not work. cards are not played to the stacks and score remains at 0 even though actions on deck are completed. Problem in isAllowed?
    // Ace (any card?) in last place on spare deck does not get played

    // Done
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

    Player() {

        GameDeck gamedeck;
        int moveCount;
        int GameNo;
        ArrayList<String> historicalActions = new ArrayList<String>();
        String SPARE = "SPARE";
        String GAME = "GAME";
        String STACK = "STACK";

        // Number of games to play
        for (GameNo = 1; GameNo < 3; GameNo++) {

            // GameDeck usage: GameDeck(file_to_use)  or GameDeck("") to create new file c:\\data\\PatiencelatestGame.txt
            gamedeck = new GameDeck("c:\\data\\PatienceTestData1.txt"); // c:\data\PatienceTestData1.txt

            Boolean print               = true;
            moveCount                   = 1;
            int no_of_performed_actions = 0;
            Boolean DoNextMove          = true;

            System.out.println("Print setting: " + print);

            // Gameloop
            while (DoNextMove) {

                if (moveCount==400) System.exit(0);

                // Playbook:

                // 1. Create a new List of all possible moves

                Actions.clear();
                // 2. parse all playable cards in game deck & spare deck
                // 3. create (all) moves for playable cards and assign a priority
                // [TODO] place one/some cards from RemovedCards and check if progress

                // Find the playable cards:
                //      first and last visible cards in each column
                //      sparedeck index card
                //      technically the top card from each stack is playable but leaving that out for now

                ArrayList<Card> playableCards = new ArrayList();

                Card firstcandidate= null;
                Card lastcandidate = null;

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
                playableCards.add(gamedeck.getSpareDeckCard());

                // find all possible targets cards
                ArrayList<Card> targetCards = new ArrayList();

                for (int col = 0; col < 7; col++) {
                    for (int row = 19; row > -1; row--) {
                        if (gamedeck.gamedeck[col][row] != null && gamedeck.gamedeck[col][row].getVisible()) {
                            targetCards.add(gamedeck.gamedeck[col][row]);
                            break; // in each column just the first visible card
                        }
                        if (gamedeck.gamedeck[col][row] == null && row == 0 ) {
                            targetCards.add(new Card("_" + col, false, GAME));  // adds an empty column as a destination - should this be done in GameDeck?
                            break; // in each column just the first visible card
                        }
                    }
                }

                // last card on each suiteStack is a target card
                targetCards.add(gamedeck.getlast(gamedeck.heartsStack)); // get "last" card on stack
                targetCards.add(gamedeck.getlast(gamedeck.diamondsStack)); // get "last" card on stack
                targetCards.add(gamedeck.getlast(gamedeck.spadesStack)); // get "last" card on stack
                targetCards.add(gamedeck.getlast(gamedeck.clubsStack)); // get "last" card on stack

                if (print) {
                    System.out.println("Before move");
                    System.out.println("===========");
                    System.out.print("Playable: ");
                    for (Card card : playableCards) System.out.print(card.getValue() + " ");
                    System.out.println();
                    System.out.print("Target:   ");
                    for (Card card : targetCards) System.out.print(card.getValue() + " ");
                    System.out.println();
                    System.out.println("Actions:");
                    gamedeck.printGameDeck();
                    gamedeck.printSpareDeck();
                    gamedeck.printStacks();
                }

                // determine possible actions
                int n = 0;
                for (Card playablecard : playableCards) {
                    for (Card targetcard : targetCards) {

                        if (gamedeck.isAllowed(playablecard, targetcard)) {


                            Action action = new Action();
                            action.playableCard = playablecard;
                            action.targetCard = targetcard;
                            action.type = playablecard.getLocation() + targetcard.getLocation();
                            action.setDescription(playablecard.getValue() + ":" + targetcard.getValue());

                            Actions.add(action);

                            n = n + 1;
                        }
                    }
                }

                Iterator<Action> its = Actions.iterator();
                while (its.hasNext()) {
                    Action action = its.next();
                    if (historicalActions.contains(action.getDescription())) {
                        its.remove();
                    }
                }

                if (print) {
                    for (Action action : Actions) {
                        System.out.println(action.playableCard.getValue() + " to " + action.targetCard.getValue() + " " + action.type);
                    }
                }

                // 2. Play moves according to ranking

                Action nextAction = null;
                try {

                    processactions:
                    {
                        nextAction = getNextActionType("GAMESTACK");
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
                        nextAction = getNextActionType("SPARESTACK");
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
                      }

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                // record current action
                if (nextAction!=null) historicalActions.add(nextAction.getDescription());


                // determine end-of-game (game loop) conditions
                if (Actions.size() > 0 && gamedeck.sparedeckindex <= gamedeck.sparedeck.size()-2) { // more to go
                    DoNextMove = true;
                    if (print) System.out.println("# action: "+ no_of_performed_actions);
                } else if (gamedeck.sparedeckindex >= gamedeck.sparedeck.size()-1 && no_of_performed_actions == 0) {
                    DoNextMove = false;

                } else if (Actions.size() == 0 && gamedeck.sparedeckindex <= gamedeck.sparedeck.size()-1) { // no actions & reached penultimate sparedeckindex
                    gamedeck.sparedeckindex++; // move to next index sparedeck card

                    if (gamedeck.sparedeckindex > gamedeck.sparedeck.size()-1 ) { // for past last card
                        gamedeck.sparedeckindex=0;
                        no_of_performed_actions = 0;
                    }
                    if (print) System.out.println("# action: "+ no_of_performed_actions);
                    DoNextMove = true;
                }
                System.out.println("After move");
                System.out.println("===========");
                if (print) {
                    gamedeck.printGameDeck();
                    gamedeck.printSpareDeck();
                    gamedeck.printStacks();
                }

                moveCount++;
                }


            if (print) {
                System.out.println("End of game");
                System.out.println("===========");

                gamedeck.printGameDeck();
                gamedeck.printSpareDeck();
                gamedeck.printStacks();
            }

            int score = gamedeck.heartsStack.size()-1 + gamedeck.diamondsStack.size()-1 + gamedeck.spadesStack.size()-1 + gamedeck.clubsStack.size()-1;

            //System.out.println("====");
            System.out.println("Sim iterations: " + GameNo + ", gamesteps: " + moveCount + ", score: " + score + "/52");
            //System.out.println("====");


        }

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
}