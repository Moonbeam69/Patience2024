package org.example;

import java.io.*;
import java.util.*;

public class GameDeck {
    public Card[][] gamedeck = new Card[7][20];
    public ArrayList<Card> carddeck;
    public ArrayList<Card> sparedeck;
    ArrayList<Card> heartsStack = new ArrayList<Card>();
    ArrayList<Card> diamondsStack = new ArrayList<Card>();
    ArrayList<Card> spadesStack = new ArrayList<Card>();
    ArrayList<Card> clubsStack = new ArrayList<Card>();
    int sparedeckindex = 0;
    String SPARE = "SPARE";
    String GAME = "GAME";
    String STACK = "STACK";

    public GameDeck() {
    }

    public GameDeck(String testdatafile) {

        if (testdatafile.equals("")) {
            carddeck = CardDeck.getCardDeck(true);
            sparedeck = carddeck;
            for (Card card : sparedeck) {
                card.setLocation(SPARE);
            }

            java.util.Random r = new Random();

            // Setup GameDeck
            for (int row = 0; row < 7; row++) {
                for (int col = row; col < 7; col++) {

                    int max = carddeck.size() - 1;

                    int p = r.nextInt(max + 1);
                    gamedeck[col][row] = carddeck.get(p);
                    gamedeck[col][row].setLocation(GAME);


                    if (col == row) {
                        gamedeck[col][row].setVisible(true);
                    } else {
                        gamedeck[col][row].setVisible(false);
                    }

                    carddeck.remove(p);
                    max--;
                }
            }

            // push aces to be closer to edge
            for (int col = 0; col < 7; col++) {
                for (int row = 0; row <= col; row++) {

                    if (gamedeck[col][row].getValue().equals("HA")) {
                        int newrow = row + 4;
                        if (newrow>col) {
                            newrow = col;
                        }
                        Card temp = gamedeck[col][row];
                        gamedeck[col][row] = gamedeck[col][newrow];
                        gamedeck[col][row].setVisible(false);

                        gamedeck[col][newrow] = temp;
                        gamedeck[col][newrow].setVisible(true);
                    }

                }
            }

            // Setup up stacks with an initial fictitious zero card
            Card hearts_null_card = new Card("H0", false);
            hearts_null_card.setLocation("STACK");
            heartsStack.add(hearts_null_card);

            Card diamonds_null_card = new Card("D0", false);
            diamonds_null_card.setLocation("STACK");
            diamondsStack.add(diamonds_null_card);

            Card spades_null_card = new Card("S0", false);
            spades_null_card.setLocation("STACK");
            spadesStack.add(spades_null_card);

            Card clubs_null_card = new Card("C0", false);
            clubs_null_card.setLocation("STACK");
            clubsStack.add(clubs_null_card);

            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter("c:\\data\\PatiencelatestGame.txt"));

                for (int row=0; row<7; row++) {
                    for (int col=0; col<7; col++) {
                        if (gamedeck[col][row]!=null) {
                            String visflag = "";
                            if (gamedeck[col][row].getVisible()) {
                                visflag = "#";
                            }
                            bw.write(visflag + gamedeck[col][row].getValue() + " ");
                        } else {
                            bw.write("   ");
                        }
                    }
                    bw.write("\n");
                }
                bw.write("Spare deck:");
                for (int n=0; n<24; n++ ) {
                    String visstr = "";
                    if (sparedeck.get(n).getVisible()) {
                        visstr = "#";
                    } else {
                        visstr = " ";
                    }
                    bw.write(visstr + sparedeck.get(n).getValue());
                }

                bw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // DEBUG FOR TESTING. Note only initial steps are useful for etsting as later steps are muddle when duplicate cards are extant
            //gamedeck[0][0] = new Card("C7", true, GAME);
            //gamedeck[0][1] = new Card("H6", true, GAME);
            //gamedeck[1][1] = new Card("D8", true, GAME);
            //gamedeck[2][2] = new Card("HK", true, GAME);
            //sparedeck.add(1, new Card("S2", true, SPARE));
            //sparedeck.add(3, new Card("SA", true, SPARE));
            // DEBUG FOR TESTING
        } else {

            String line;
            try {
                BufferedReader br = new BufferedReader(new FileReader(testdatafile));


                    try {
                        // read game deck
                        for (int row=0; row<7; row++) {
                            String[] linecards = br.readLine().trim().split(" ");
                            for (int col=row; col<7; col++) {
                                Boolean visflag = linecards[col-row].contains("#");
                                gamedeck[col][row]= new Card(linecards[col-row].replace("#", ""), visflag, GAME);
                            }
                        }

                        // read sparedeck
                        String[] sparecards = br.readLine().trim().split(" ");
                        sparedeck = new ArrayList<Card>();

                        for (int n=2; n<26; n++ ) {
                            Boolean visflag = sparecards[n].contains("#");
                            sparedeck.add (new Card(sparecards[n].replace("#", ""), visflag, SPARE));
                        }

                        // Setup up stacks with an initial fictitious zero card
                        Card hearts_null_card = new Card("H0", false);
                        hearts_null_card.setLocation("STACK");
                        heartsStack.add(hearts_null_card);

                        Card diamonds_null_card = new Card("D0", false);
                        diamonds_null_card.setLocation("STACK");
                        diamondsStack.add(diamonds_null_card);

                        Card spades_null_card = new Card("S0", false);
                        spades_null_card.setLocation("STACK");
                        spadesStack.add(spades_null_card);

                        Card clubs_null_card = new Card("C0", false);
                        clubs_null_card.setLocation("STACK");
                        clubsStack.add(clubs_null_card);

                        br.close();

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }



            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public Card getSpareDeckCard() {
        try {
            //System.out.println("sparedeckindex: " + sparedeckindex);
            if (sparedeckindex <= sparedeck.size() - 1) {
                return sparedeck.get(sparedeckindex);
            } else {
                return sparedeck.get(0);
            }
        } catch (Exception e) {
            //printGameDeck();
            //printSpareDeck();
            //printStacks();
            return null;
        }
    }

    public void removeCardFromDeck (Card playablecard) {
        Boolean bbreak = false;
        for (int row = 0; row < 20; row++) {
            for (int col = 0; col < 7; col++) {

                if (gamedeck[col][row] != null) {
                    if (gamedeck[col][row].getValue().equals(playablecard.getValue())) {
                        gamedeck[col][row] = null;
                        bbreak = true;
                        break;
                    }
                }
            }
            if (bbreak) break;
        }
    }
    public void add2Stack (Card playablecard) {
        try {
            switch(playablecard.getValue().substring(0,1)) {
                case "H":
                    heartsStack.add(playablecard);
                    playablecard.setLocation("STACK");
                    removeCardFromDeck(playablecard);
                    break;
                case "D":
                    diamondsStack.add(playablecard);
                    playablecard.setLocation("STACK");
                    removeCardFromDeck(playablecard);
                    break;
                case "S":
                    spadesStack.add(playablecard);
                    playablecard.setLocation("STACK");
                    removeCardFromDeck(playablecard);
                    break;
                case "C":
                    clubsStack.add(playablecard);
                    playablecard.setLocation("STACK");
                    removeCardFromDeck(playablecard);
                    break;

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }

    }
    public void move (Card playablecard, Card targetcard) {

        int initial_playable_row, initial_playable_col;

        initial_playable_col = getCol(playablecard);
        initial_playable_row = getRow(playablecard);

        if (playablecard.getLocation().equals(SPARE) && targetcard.getLocation().equals(GAME)) {

            playablecard.setVisible(true);
            playablecard.setLocation(GAME);

            if (targetcard.getValue().substring(0,1).equals("_")) { // in case of empty column
                gamedeck[getCol(targetcard)][0] = playablecard;
            } else {
                gamedeck[getCol(targetcard)][getRow(targetcard) + 1] = playablecard;
            }

            sparedeck.remove(sparedeckindex);
            if (sparedeckindex > sparedeck.size()-1) {
                sparedeckindex=0;
            }

        } else if (playablecard.getLocation().equals(GAME) && targetcard.getLocation().equals(GAME)) {

            if (!targetcard.getValue().substring(0,1).equals("_")) { // if target is not an empty column

                //int playablecard_row = getRow(playablecard);

                // move the playable card to the target card position col+1
                gamedeck[getCol(targetcard)][getRow(targetcard) + 1] = playablecard;

                // make the previous card in the playable card column visible
                if (initial_playable_row - 1 >= 0) {
                    gamedeck[initial_playable_col][initial_playable_row - 1].setVisible(true);
                }

                gamedeck[initial_playable_col][initial_playable_row] = null;

                // also move lower cards, if any
                int n = 0;
                while (gamedeck[initial_playable_col][initial_playable_row + n + 1] != null) {

                    Card nextCardDown = gamedeck[initial_playable_col][initial_playable_row + n + 1];

                    gamedeck[getCol(targetcard)][getRow(targetcard) + n + 2] = nextCardDown;

                    gamedeck[initial_playable_col][initial_playable_row + n + 1] = null;
                    n++;
                }

            } else { // move to the empty column (only difference is offset = 1

                gamedeck[initial_playable_col][initial_playable_row] = null;
                gamedeck[getCol(targetcard)][getRow(targetcard)] = playablecard;

                // make the previous card in the playable card column visible
                if (initial_playable_row - 1 >= 0) {
                    gamedeck[initial_playable_col][initial_playable_row - 1].setVisible(true);
                }

                // also move lower cards, if any
                int n = 0;

                while (gamedeck[initial_playable_col][initial_playable_row + n + 1] != null) {

                    Card nextCardDown = gamedeck[initial_playable_col][initial_playable_row + n + 1];

                    gamedeck[getCol(targetcard)][getRow(targetcard) + n + 1] = nextCardDown;

                    gamedeck[initial_playable_col][initial_playable_row + n + 1] = null;
                    n++;
                }
            }


        } else if (playablecard.getLocation().equals(GAME) && targetcard.getLocation().equals(STACK)) {

            // make the previous card in the playable card column visible
            if (getRow(playablecard) - 1 >= 0) {
                gamedeck[getCol(playablecard)][getRow(playablecard) - 1].setVisible(true);
            }

            add2Stack(playablecard);
            gamedeck[initial_playable_col][initial_playable_row] = null;

            playablecard.setLocation(STACK);

        } else if (playablecard.getLocation().equals(SPARE) && targetcard.getLocation().equals(STACK)) {

            playablecard.setLocation(STACK);

            add2Stack(playablecard);
            sparedeck.remove(sparedeckindex);
            if (sparedeckindex > sparedeck.size()-1) {
                sparedeckindex=0;
            }
        }

    }

    int getRow(Card card) {
        int r = -1;

        if (card != null) {
        try {
            if (card.getValue().substring(0, 1).equals("_")) {
                return 0; // always 0
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        block:
        {
            for (int row = 0; row < 20; row++) {
                for (int col = 0; col < 7; col++) {
                    if (gamedeck[col][row]!=null){
                        if (gamedeck[col][row].getValue().equals(card.getValue())) {
                            r = row;
                            break block;
                        }
                    }
                }
            }
        }
        return r;
        } else {
            System.out.println("Error null card");
            return r;
        }
    }

    int getCol(Card card) {
        int c = -1;

        // empty column
        if (card.getValue().substring(0,1).equals("_")) {
            return Integer.valueOf(card.getValue().substring(1,2)); // returns the column imprinted in the second char of the value, e.g. _1
        }

        block:
        {
            for (int row = 0; row < 20; row++) {
                for (int col = 0; col < 7; col++) {
                    if (gamedeck[col][row]!=null) {
                        if (gamedeck[col][row].getValue().equals(card.getValue())) {
                            c = col;
                            break block;
                        }
                    }
                }
            }
        }
        return c;
    }

    public Card getlast(ArrayList<Card> list) {
        Card card;

        try {
            card = list.get(list.size() - 1);
        } catch (Exception e) {
            card = null;
        }
        return card;
    }
    public Boolean isAllowed(Card playablecard, Card targetcard) {
        Boolean checksuit = false;
        Boolean checknumber = false;

        if (getRow(playablecard) ==0 && getRow(targetcard)==0 ) { // prevents Kings moving from one col to another
            return false;
        }
//        // Move to Stack: return true if suits are the same and playable - target = 1
//        if (playablecard.getSuit().equals(targetcard.getSuit()) &&
//                playablecard.getNumber() - targetcard.getNumber() == 1 &&
//                targetcard.getLocation().equals(STACK)) {
//            return true;
//        }

        if (playablecard.getValue().substring(1,2).equals("K") && targetcard.getValue().substring(0,1).equals("_")) {
            return true;
        }

        // illegal move of there cards hangng off the payable card
        if (playablecard.getLocation().equals(GAME) && gamedeck[getCol(playablecard)][getRow(playablecard)+1] != null) {
            return false;
        }

        if ((playablecard.getLocation().equals(GAME) || playablecard.getLocation().equals(SPARE)) && targetcard.getLocation().equals(GAME)) {
            if (playablecard.getSuit().equals("H") && (targetcard.getSuit().equals("C") || targetcard.getSuit().equals("S"))) checksuit = true;
            if (playablecard.getSuit().equals("D") && (targetcard.getSuit().equals("C") || targetcard.getSuit().equals("S"))) checksuit = true;
            if (playablecard.getSuit().equals("C") && (targetcard.getSuit().equals("H") || targetcard.getSuit().equals("D"))) checksuit = true;
            if (playablecard.getSuit().equals("S") && (targetcard.getSuit().equals("H") || targetcard.getSuit().equals("D"))) checksuit = true;
            if (targetcard.getNumber() - playablecard.getNumber() == 1) checknumber = true;
        }
        if (playablecard.getLocation().equals(GAME) && targetcard.getLocation().equals(STACK)) {
            if (playablecard.getSuit().equals("H") && targetcard.getSuit().equals("H") ) checksuit = true;
            if (playablecard.getSuit().equals("D") && targetcard.getSuit().equals("D") ) checksuit = true;
            if (playablecard.getSuit().equals("C") && targetcard.getSuit().equals("C") ) checksuit = true;
            if (playablecard.getSuit().equals("S") && targetcard.getSuit().equals("S") ) checksuit = true;
            if (playablecard.getNumber() - targetcard.getNumber() == 1) checknumber = true;
        }
        if (playablecard.getLocation().equals(SPARE) && targetcard.getLocation().equals(STACK)) {
            if (playablecard.getSuit().equals("H") && targetcard.getSuit().equals("H") ) checksuit = true;
            if (playablecard.getSuit().equals("D") && targetcard.getSuit().equals("D") ) checksuit = true;
            if (playablecard.getSuit().equals("C") && targetcard.getSuit().equals("C") ) checksuit = true;
            if (playablecard.getSuit().equals("S") && targetcard.getSuit().equals("S") ) checksuit = true;
            if (playablecard.getNumber() - targetcard.getNumber() == 1) checknumber = true;
        }

        return checksuit && checknumber;
    }

    public void printGameDeck() {
        StringBuilder output = new StringBuilder();

        System.out.println();
        for (int row = 0; row < 20; row++) {
            for (int col = 0; col < 7; col++) {

                if (gamedeck[col][row] == null) {
                    output.append("   ");
                } else {
                    if (gamedeck[col][row].visible) {
                        output.append("#");
                    } else {
                        output.append(" ");
                    }
                    output.append(gamedeck[col][row].getValue());
                }
            }
            if (output.toString().trim().length()!=0) System.out.println(output);
            output.setLength(0);
        }
    }

    public void printStacks() {
        int n= 0;
        System.out.println("Stacks:");

        System.out.print("  Hearts:  ");
        for (Card card: heartsStack) {
            if (n++!=0) System.out.print(card.getValue() + " ");
        }
        n= 0;
        System.out.println();
        System.out.print("  Diamonds:");
        for (Card card: diamondsStack) {
            if (n++!=0) System.out.print(card.getValue() + " ");
        }
        n= 0;
        System.out.println();
        System.out.print("  Spades:  ");
        for (Card card: spadesStack) {
            if (n++!=0) System.out.print(card.getValue() + " ");
        }
        n= 0;
        System.out.println();
        System.out.print("  Clubs:   ");
        for (Card card: clubsStack) {
            if (n++!=0) System.out.print(card.getValue() + " ");
        }
        System.out.println();
    }


    public void printSpareDeck() {
        int n=0;
        System.out.print("Spare deck: ");

        Iterator<Card> sparedeckitr = sparedeck.iterator();

        //for (int p=0; p< sparedeck.size(); p++) {
        while(sparedeckitr.hasNext()) {
            if (n==sparedeckindex) System.out.print("*");
            System.out.print(sparedeckitr.next().getValue()+" ");
            n++;
        }
        System.out.println(" " + sparedeckindex);
    }
}
