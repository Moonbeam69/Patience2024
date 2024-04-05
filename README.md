Patience2024


This app plays Solitaire X times (in-code config) using a selected strategy (in-code config). At the end of each run KPI (% win, max score) is presented
to allow the optimal strategy to be selected. There are four directives and one option that determine the agents strategy (all in Player class).
THe game recognises 3 card sour ces:

GAME  Cards on the table organised in columns (open and closed)
STACK Cards in stacks organised by suite in order 
SPARE Cards left over after dealing on to the table

The play order (strategy) is determined by the order in which following directives are added

        processDirectives.add (GAMEGAME);
        processDirectives.add (GAMESTACK);
        processDirectives.add (SPARESTACK);
        processDirectives.add (SPAREGAME);

where e.g. GAMEGAME directs play to select a card from the table to another valid position on the table.


and this boolean:

        PRIORITISE_EMPTY_COLUMN_CREATION  = true|false // flag that prioritises moves that create empty columns over those that don't
