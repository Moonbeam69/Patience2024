package org.example;

import org.junit.jupiter.api.*;

import java.util.*;

import static org.example.Constants.GAMEGAME;
import static org.example.Constants.GAMESTACK;
import static org.example.Constants.SPAREGAME;
import static org.example.Constants.SPARESTACK;
import static org.junit.jupiter.api.Assertions.*;

class ActionManagerTest {

    public  ActionManager actionManager;
    private  ArrayList<String> processDirectives = new ArrayList<String>();
    private  boolean print = false;
    private  boolean prioritiseEmptyColumnCreation = false;

    @BeforeEach
    void setUp() {

        GameDeck gamdecktest = new GameDeck("c:\\data\\PatienceTestData1.txt");
        processDirectives.add (GAMEGAME);
        processDirectives.add (GAMESTACK);
        processDirectives.add (SPARESTACK);
        processDirectives.add (SPAREGAME);

        //gameDeckMock = Mockito.mock(GameDeck.class);   // not using Mockito for this, can use an actual object
        actionManager = new ActionManager(gamdecktest, processDirectives, print, prioritiseEmptyColumnCreation);

        assertNotNull(actionManager);
    }

    @Test
    void testDeterminePossibleActions() {

        ArrayList<Action> actions = actionManager.determinePossibleActions();

        // Verify the method's behaviors
        Action expectedaction = new Action();
        expectedaction.setDescription("DA:S2");
        assertTrue(actions.get(0).equals(expectedaction) );

        assertNotNull(actions);
        assertFalse(actions.isEmpty());
        assertEquals(actions.size(), 3);

    }

    @Test
    void testDoNextAction() {
        ArrayList<Action> actions = actionManager.determinePossibleActions();
        ArrayList<Action> nextactions = actionManager.doNextAction();
        assertEquals(2, nextactions.size());
    }
}

