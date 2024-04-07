package org.example;

import java.util.*;

public class CardDeck {
    public static ArrayList<Card> getCardDeck(Boolean shuffle) {

        ArrayList<Card> carddeck = new ArrayList<Card>();

        // HSCD hearts, spades, clover, diamond
        carddeck.add(new Card("HA", false));
        carddeck.add(new Card("H2", false));
        carddeck.add(new Card("H3", false));
        carddeck.add(new Card("H4", false));
        carddeck.add(new Card("H5", false));
        carddeck.add(new Card("H6", false));
        carddeck.add(new Card("H7", false));
        carddeck.add(new Card("H8", false));
        carddeck.add(new Card("H9", false));
        carddeck.add(new Card("HX", false));
        carddeck.add(new Card("HJ", false));
        carddeck.add(new Card("HQ", false));
        carddeck.add(new Card("HK", false));

        carddeck.add(new Card("SA", false));
        carddeck.add(new Card("S2", false));
        carddeck.add(new Card("S3", false));
        carddeck.add(new Card("S4", false));
        carddeck.add(new Card("S5", false));
        carddeck.add(new Card("S6", false));
        carddeck.add(new Card("S7", false));
        carddeck.add(new Card("S8", false));
        carddeck.add(new Card("S9", false));
        carddeck.add(new Card("SX", false));
        carddeck.add(new Card("SJ", false));
        carddeck.add(new Card("SQ", false));
        carddeck.add(new Card("SK", false));

        carddeck.add(new Card("CA", false));
        carddeck.add(new Card("C2", false));
        carddeck.add(new Card("C3", false));
        carddeck.add(new Card("C4", false));
        carddeck.add(new Card("C5", false));
        carddeck.add(new Card("C6", false));
        carddeck.add(new Card("C7", false));
        carddeck.add(new Card("C8", false));
        carddeck.add(new Card("C9", false));
        carddeck.add(new Card("CX", false));
        carddeck.add(new Card("CJ", false));
        carddeck.add(new Card("CQ", false));
        carddeck.add(new Card("CK", false));

        carddeck.add(new Card("DA", false));
        carddeck.add(new Card("D2", false));
        carddeck.add(new Card("D3", false));
        carddeck.add(new Card("D4", false));
        carddeck.add(new Card("D5", false));
        carddeck.add(new Card("D6", false));
        carddeck.add(new Card("D7", false));
        carddeck.add(new Card("D8", false));
        carddeck.add(new Card("D9", false));
        carddeck.add(new Card("DX", false));
        carddeck.add(new Card("DJ", false));
        carddeck.add(new Card("DQ", false));
        carddeck.add(new Card("DK", false));

        if (shuffle) Collections.shuffle(carddeck);

        return carddeck;
    }

}
