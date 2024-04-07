package org.example;

import java.util.*;
import java.util.logging.Logger;
import static org.example.Constants.*;

// Add some commentary here
public class Player {
    Logger                              logger = Logger.getLogger(getClass().getName());
    ArrayList<Action>                   actions;

    GameDeck                            gamedeck;
    int                                 moveCount;
    int                                 GameNo;
    Boolean                             print;
    ArrayList<org.example.Card>         playableCards;
    ArrayList<org.example.Card>         targetCards;
    Action                              nextAction = null;
    int                                 no_of_wins = 0;
    int                                 maxplays = 0;
    GameState                           gamestate = new GameState();
    ArrayList<GameState>                gamestatehistory = new ArrayList<GameState>();
    ArrayList <ArrayList<GameState>>    allgamestatehistory = new ArrayList<ArrayList<GameState>>();

    public static void main(String[] args) throws InterruptedException {
        int maxplays=0;

        if (args[0] != null) {
            maxplays = Integer.valueOf(args[0]);
        }

        Player p =new Player(maxplays);
    }

    Player(int maxplays) throws InterruptedException {

        //maxplays=100;

        // run-time parameters
        print                                           = false;
        final Boolean PRIORITISE_EMPTY_COLUMN_CREATION  = true;

        // set the processing priority of action types. The order of adding creates the order of processing
        ArrayList<String> processDirectives = new ArrayList<>();
        processDirectives.add (GAMEGAME);
        processDirectives.add (GAMESTACK);
        processDirectives.add (SPARESTACK);
        processDirectives.add (SPAREGAME);

        ArrayList<String> results = new ArrayList<>();
        ArrayList<Integer> scores = new ArrayList<Integer>();
        gamestatehistory          = new ArrayList<GameState>();

        System.out.println("Print setting: " + print);

        // Number of games to play
        for (GameNo = 1; GameNo <= maxplays; GameNo++) {

            // GameDeck usage: GameDeck(file_to_use)  or GameDeck("") to create new file c:\\data\\PatiencelatestGame.txt
            gamedeck                    = new GameDeck(""); // "c:\\data\\PatienceTestData1.txt"
            //gamedeck                    = new GameDeck("c:\\data\\PatienceTestData1.txt");
            moveCount                   = 0;
            Boolean DoNextMove          = true;
            ActionManager actionManager = new ActionManager(gamedeck, processDirectives, print, PRIORITISE_EMPTY_COLUMN_CREATION);

            // Gameloop
            while (DoNextMove) {

                if (moveCount == 405) System.exit(0);
                if (print) System.out.println("GameHistory No/No Considered Moves/No Performed Moves: " + GameNo + "/" + moveCount + "/" + actionManager.no_of_performed_actions);



                actionManager.PrintAll("Before action");
                actions = actionManager.determinePossibleActions();
                actionManager.PrintActions("All filtered actions");
                actions = actionManager.doNextAction();



                // determine end-of-game (game loop) conditions
                if (actions.size() > 0 && gamedeck.sparedeckindex <= gamedeck.sparedeck.size()-2) { // more to go
                    DoNextMove = true;
                    moveCount++;
                    if (print) System.out.println("# performed actions: "+ actionManager.no_of_performed_actions);

                } else if (gamedeck.sparedeckindex >= gamedeck.sparedeck.size()-1 && actionManager.no_of_performed_actions == 0) {
                    DoNextMove = false;

                } else if (actions.size() == 0 && (gamedeck.sparedeckindex <= gamedeck.sparedeck.size()-1 || gamedeck.sparedeckindex == gamedeck.sparedeck.size())) { // no actions & reached penultimate sparedeckindex
                    gamedeck.sparedeckindex++; // move to next index sparedeck card

                    if (gamedeck.sparedeckindex > gamedeck.sparedeck.size()-1 ) { // for past last card
                        gamedeck.sparedeckindex = 0;
                        actionManager.no_of_performed_actions = 0;
                    }
                    if (print) System.out.println("# action: "+ actionManager.no_of_performed_actions);
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

        float avg = 0f;
        for (int score: scores) {
            avg += score;
        }
        System.out.println("Number of games: " + --GameNo);
        System.out.println("average score: " + avg/ scores.size());
        System.out.println("max score: " + Collections.max(scores));
        System.out.println("no of wins: " + no_of_wins);

        System.out.println("wins/total plays: " + 100*(float)no_of_wins/maxplays + "%");

    }

}