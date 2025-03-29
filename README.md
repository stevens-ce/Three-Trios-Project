Part 1 **Overview**:

The Features interface is designed to work with interactions from both human and machine players. 
his ensures that the rules and logic of the game are followed through regardless of the type of
player. For human players, the Features interface interacts through the view. Human players perform
actions like selecting a card or clicking the grid cell. This is captured by the GUI using the 
HandPanel and GridPanel components, and passed to the Features interfaces. After, the features 
interface validates and processes those actions, and then the game model updates. The 
HumanActionPlayer technically serves as a pass-through got messages to the view, and this allows 
the GUI to handle those player actions.
For the machine players, the features interface is more-so integrated into the decision-making
process. The MachineActionPlayer uses a strategy to determine the optimal moves based on the current
game state. Once the strategy selects a move, the MachineActionPlayer uses the Features interface 
to execute by calling cardSelected and cellSelected methods.


Breaking down our Features interface:

cardSelected handles the selection of a card by the player, validating the card based on the 
player's color and either saving or unsaving the card based on its current state.

cellSelected processes the selection of a grid cell, attempting to play the currently selected 
card at the specified row and column on the grid if the move is valid.

getAssociatedColor provides the color of the player associated with the Feature implementation,
allowing the view to differentiate between players during interactions.

The Receiver interface basically facilitates the communication between the model and controller.
It acts as the messaging system to keep the controller informed about the state of the game. The 
ThreeTriosBasicModel implements the communication by keeping a list of Receiver instances and 
sending messages to them whenever an important game event occurs. An example of this is at the
start of a player’s turn, updating to the grid state, or the end of the game. The messageSent 
method allows the model to provide real-time updates to the control. This ensures that it can
make decisions or update the view based on the latest game state.

Breaking down the Receiver interface:

messageSent acts as a communication channel, allowing the model to send notifications about 
game state changes to the controller.


The controller serves as the mediator between the model and the view. It facilitates the 
communication and handling of player actions. It processes messages from the model through 
the Receiver interface to determine the game state changes. This would be the turn updates or 
the game completion. The Controller uses features to validate player actions and ensures they 
follow the rules of the game before updating the model. It also updates the view to reflect the 
changes in the game state. This ensures that the GUI always reflects the current state of the 
game as stored in the model.


**threetrios.model package**

[Messenger](/src/threetrios/model/Messenger) is an interface that sends messages to all subscribed Receivers.

[ReadOnlyThreeTriosModel](/src/threetrios/model/ReadOnlyThreeTriosModel) is model interface
containing all observations of the model's state that may be necessary.

[ThreeTriosModel](/src/threetrios/model/ThreeTriosModel) is a model interface extending 
ReadOnlyThreeTriosModel that adds the behaviors of a game of Three Trios.

[ThreeTriosModelMessenger](/src/threetrios/model/ThreeTriosModelMessenger) is a model interface extending ThreeTriosModel and Messenger, and adds no new methods.

[ThreeTriosBasicModel](../src/threetrios/model/ThreeTriosBasicModel) implements ThreeTriosModelMessenger.

It has fields:

- Two [HumanPlayer](../src/threetrios/model/HumanPlayer) redPlayer and bluePlayer, implementation of Player:
  - [Player](../src/threetrios/model/Player) is a representation of the player. This player has 
behaviors to:                     
    - Add a card to its hand. This is used in the dealing of the cards to hand. 

- A List<Receiver> receivers that represents all receivers of the model's messages.
  - intialized empty in constructor.

- A List<CardinalCard> deck to represent the deck. 
  - deck is initialized in constructor as an empty ArrayList<CardinalCard>.
    - Uses the CardinalCard implementation of Card.

[CardinalCard](../src/threetrios/model/CardinalCard)                               
- Represents its four values with the CardinalValues class.                                   
  - [CardinalValues](../src/threetrios/model/CardinalValues) conveniently groups four 
CardinalAttackValue class values, and allows easy access to them with NSEW specific observations.  
    - CardinalValues creates CAVs from ints, i.e 1 -> AttackValue.ONE, ...,  10 -> AttackValue.A
    - [CardinalAttackValue](../src/threetrios/model/CardinalAttackValue) has final AttackValue and 
Direction enums as public fields.         
      - We decided on public access because there's no way to mutate them once set, having no such 
methods + being final.                                               
      - CAV has a method getOppositeDirection(Direction) that, returns the opposite of 
the given Direction. This is used to find opponents during the battlePhase.                                                   
      - [AttackValue](../src/threetrios/model/AttackValue) has values 1-10, called ONE, TWO, ... , 
NINE, A.
      - [Direction](../src/threetrios/model/Direction) has the four cardinal directions, called NORTH
, SOUTH, EAST, WEST.            
- Has a String identifier and a Color color.                                
  - identifier is assumed to be unique within deck when given to constructor.                   
    - equals() relies on the unique identifier.                                               
- Within the model's setup, CardinalCard(CardinalValues values, String id) is used to create cards.

The model's observations from ReadOnlyThreeTriosModel are:
- boolean isGameOver(): is game over?
  - throws ISE if game has not started.
- Color winner(): if game is over, who won?
  - throws ISE if game has not start or is not over.
- GridCell[][] getGrid(): what does the grid look like? Also tells state of each cell via GridCell
- Color getCurrentPlayer(): whose turn is it?
- List<CardType> getPlayerHand(Color): what is the hand belonging to the player of given Color?
- List<Color> getPlayers(): who are all the players? 
- int getGridLength(): how many rows are in this grid?
  -  same effect as model.getGrid().length.
- int getGridWidth(): how many columns are in this grid?
  -  same effect as model.getGrid()[0].length.
- GridCell getCell(int row, int col): what is the cell at the specified position in grid?
  - same effect as model.getGrid()[row][col].
- Color getOwnerOfCell(GridCell cell): who is the owner of given cell?
  - same effect calling cell.getColor().
- boolean isMoveLegal(int row, int col): can something be placed to this position? i.e., is it a 
valid position on the grid, a card cell, and unoccupied
- int numCardsFlipped(Card, Color, int row, int col): if Color player places given Card to given row
and column on grid, how many cards will be flipped as result? 
  - throws IAE if move is invalid
  - NOTE: The placed card does not count towards this.
- int currentScore(Color): what is the current score of the current player? i.e. cards in hand + 
cards on board of given color.

The model's behaviors from Messenger are:
- void setReceiver(Receiver)
  - adds given Receiver to field receivers
- void sendMessageAll(String message)
  - loops through receivers and calls receiver.sentMessage(message) on each.
  - method called when:
    - turn changes
      - player should make a move (subsequent)
    - view needs to refresh
    - game is over; communicates winner in same message

The model's behaviors from ThreeTriosModel are:
- The setup of the model in setup(String[], String[]).
  - setup creates the Grid representation in the model using a String array representing the rows 
of the grid.
    - The creation of the grid happens within the Grid constructor. 
  - setup adds cards from a String array, each string representing a card, to the deck. 
    - If the size of the deck is less than the amt of card cells within the Grid, throws IAE. 
      - Cards are then dealt from the deck into the player's hands. 
        - We made the choice to shuffle the deck and linearly deal cards, rather than randomly 
select a card each time. The effect is the same, but does not require many Random calls.
        - Dealing happens within the model, instead of within the Player objects, so as not to
pass the deck to the players.
- Playing a card to grid in playCard(Card, int, int).
  - playCard has two distinct phases, placingPhase and battlePhase, divided into two helpers. 
    1. placingPhase:
       - Ensures that the card belongs to the current player, then calls [Grid](src/threetrios/Grid) 
method place.
       - Grid.place(Card, Position, Color) checks whether the position is within grid bounds.
Then it give control for actual placement to the GridCell at pos.
         - [Position](../src/threetrios/model/Position) is a class that simply stores a row and a 
column int, and allows a client to see them via observation. Once stored, they are immutable.
       - [GridCell](../src/threetrios/model/GridCell) is a class that represents a cell of the board. 
Within Grid, the grid is represented by a 2d array of GridCell.
         - field boolean placeable, which represents whether it's a card cell.
           - We chose to use a boolean instead of create an enum because the client accesses it 
via the method isPlaceable, which returns a boolean anyway.
         - field Card card, which stores the card of the cell when one is placed.
           - This field remains null until placeCard(Card) is called.
             - If card != null or !placeable when placeCard is called, throws ISE.
             - Card cannot be placed on an occupied cell, and cannot be placed on a hole.
         - field Color color, which stores the color of the cell. Field is null until a card is
first placed on it, at which point it is dyed to the color of player who placed it.
           -  flip() swaps color: can only switch between RED and BLUE.
               - This will be a problem if other colors are introduced in the future.
       - Control returns to placingPhase, and if placing was successful (ISE not thrown), removes 
the given card from the current player's hand.
    2. battlePhase: 
       - All functionality for this phase is within the Grid; starts with a call to Grid method
grid.engageAdj(Card) 
         - finds the card's position in grid, done to minimize reliance on accurate information 
from model. 
         - It then calls checkAdjCell(CardinalAttackValue, Position) on each of the 
Card's four CAVs. 
           - This method takes in what is necessary to determine whether the card adj
to each value is to be battled or not.
             - CAV's Direction is used to determine which adjacent cell is to be checked.  
             - The Position + Direction are used to calculate the position of the adjacent cell
           - Checks whether adj cell is card cell or hole: cannot battle nothing.
           - Checks whether adj cell is empty: cannot battle nothing.
         - Proceeds to battle(CardinalAttackValue atk, GridCell opp) if adj cell's color != color of 
GridCell at Position on grid.
           - CAV is used to determine which directional CAV of opp's to compare to.
             - If atk's AttackValue is greater than the op's AttackValue, the opp flips color.
           - engageAdj(Card opp) is called. This means that this battle process will be
called on every card that is flipped until there are no more flippable cards. 
       - Then process ends, and control returns to the model class.
  - playCard only proceeds to battlePhase if placingPhase is successful.
    - In the future controller, it can catch the IAE or ISE thrown by a failed placingPhase, and 
refuse to have the card placed down.
- After both phases are complete, goes to the next turn. 
  - This is tracked within the Player with a boolean. It can only be changed by Player method 
nextTurn() that performs this.turn = !this.turn
  - It is ensured that it can only ever one player's turn, as redPlayer is set as the first turn in 
the model constructor, and player.nextTurn is only called in the model helper nextTurn, which calls 
`bluePlayer.nextTurn; redPlayer.nextTurn;`

**threetrios.controller package**

[ThreeTriosController](../src/threetrios/controller/ThreeTriosController) is the interface defining all of the Controller's behaviors.

[Receiver](../src/threetrios/controller/Receiver) is an interface that gives ability to recieves messages.
- an Observer 

[ThreeTriosPlayerController](../src/threetrios/controller/ThreeTriosPlayerController) is a class implementing ThreeTriosController and Receiver, and is the main controller.

It has fields:
- final View view, which is the view associated with this controller. Initialized in constructor.
- final ActionPlayer player, same as view. Initialized in constructor.
- final Color color, the color of this controller/ associated view/player. Initialized in constructor.
- Color turn, which tracks who the current turn player is.

In constructor:
- sets itself as a receiver to model, so it gets all messages about gamestate.
- creates and initializes a Feature obj (detailed later) linked to itself and adds it to view and player.
- intializes given model, view, player, and color as fields.

The controller's behaviors from ThreeTriosController are:
- Checking if a player's card selection is valid or not, in boolean validCardClick(int cardIdx, Color hand)
  - hand represents the hand of the card that had been clicked
  - returns false if: hand != color, cardIdx not within that model.Player's hand, not this Player's turn
    - sends message through Player specifying reason for invalid click
  - else returns true, and calls view.highlightCard(color, cardIdx)
    - this is called regardless of if this card is alr selected. Unselecting of card handled in Feature/ View
- Checking if cell selection is valid/ actually playing card to model in void cardPlayed(int cardIdx, int row, int col)
  - attempts playCard with given, if fails sends message through player that card was played to invalid position, and to unhighlight current card
    - also calls player.makeMove(), which was meant to catch strategy failures (though they shouldn't happen at all).
- updates view in void updateView(); just calls view.refresh()

Behavior from Receiver is:
- Receiving messages from model in void sentMessage(String message), and deciding on further action depending on message.
  - contains "it's your turn", updates turn field based on color mentioned in message
  - contains "Make next move", calls player.playerMessenger and .makeMove
    - if player is human, gets a It's your turn pop-up, if machine it will simply make its move.
  - contains "Update grid state", calls updateView()
  - contains "Game is Over", directly tells view to displayMessage(message).

[Feature](../src/threetrios/controller/Feature) is an interface that covers all view -> controller interactions.

[ViewPlayerFeature](../src/threetrios/controller/ViewPlayerFeature) implements Feature:
- constructor:
  - takes in a ThreeTriosController and a Color; needs to communicated info from view to Controller, needs Color to give to View.
  - intializes stored card index cardIdx to -1
- Overriden methods:
  - void cardSelected(int, Color) calls validCardClick(int, Color), and if true stores the index to cardIdx. if it's already stores sets cardIdx to -1.
  - void cellSelected(int, int), if cardIdx is greater than -1, sets cardIdx to -1 and calls controller.playedCard with the old cardIdx and given ints.
  - Color getAssociatedColor() is just used so the title of the View is accurate to its associated color.

[ConfigurationFileReader](../src/threetrios/controller/ConfigurationFileReader) has method readFile(String)takes a path path and converts its contents to a String array.

**threetrios.strategy package**

[TTStrategy](../src/threetrios/strategy/TTStrategy) is the basic interface that defines Strategy behavior:
 - CardMove chooseMove(ReadOnlyThreeTriosModel, Color), which chooses a position to move to and a card to play, wrapped in a CardMove.
Subclasses of TTStrategy:
 - [TieBreakerStrategy](../src/threetrios/strategy/TieBreakerStrategy) is an abstract method extending TTStrategy. Adds one method for family use:
   - CardMove tieBreakingMove(ReadOnlyThreeTriosModel, Color, Map<Move, List<Card>>) which, given a map of tied moves and their possible cards, returns one by determining which is upperleftmost and which card is closest to 0 in hand.
   - All strategies are an implementation of this, 
 - [MaxFlipStrategy](../src/threetrios/strategy/MaxFlipStrategy) is an extension of TieBreaker.
   - iterates through all positions on grid and calls model.numCardsFlipped(Card, Color) on every Card in Color player's hand, and chooses the pair that will result in the most flipped cards
   - SIMPLEST STRATEGY: this is the one the transcript was made of
- [FourCornersStrategy](../src/threetrios/strategy/FourCornersStrategy) is an extension of TieBreaker.
  - checks the four corners, selects the upperleftmost one that is also the most defensible (has the fewest exposed sides), and then selects teh cards that has the highest average values facing those sides.
- [LastResortStrategy](../src/threetrios/strategy/LastResortStrategy) is an implementation of TTStrategy that simply returns the topleftmost position as the move and the card closest to zero in hand.
- [TTStrategyStacks](../src/threetrios/strategy/TTStrategyStacks) is an implementation of TTStrategy that allows two Strategies to be stacked
  - new TTStrategyStacks(strat1, strat2) -> tries to find a move with strat1 first, then if that returns null tries strat2.
- [InfallibleStack](../src/threetrios/strategy/TTStrategyInfallibleStack) is an extension of TTStrategyStacks only takes one strategy, with the second strategy set as LastResort on default.
  - This means that InfallibleStack will ALWAYS return a CardMove.

**threetrios.strategy package**

[ActionPlayer](../src/threetrios/player/ActionPlayer) is the base interface that defines player behavior:
- void addFeature(Feature) that initializes the player to a Feature impl, which will connect its actions to the main controller
- void makeMove() can trigger a move to be sent to Feature, depending on impl.
- void playerMessenger(String) can send a message to view, depending on impl.

Subclasses of ActionPlayer:
- [HumanActionPlayer](../src/threetrios/player/HumanActionPlayer) is an impl of ActionPlayer that represents a human player.
  - takes in a View upon construction
  - leaves all inherited methods blank except playerMessenger.
    - playerMessenger calls View.displayMessage(message) UNLESS
      - message.contains("highlight"), then it extracts the color and idx of the card to be highlighted and calls view.highlightCard(String color, idx).
- [MachineActionPlayer](../src/threetrios/player/MachineActionPlayer) is an impl of ActionPlayer represeting a machine player.
  - takes in a ReadOnlyThreeTriosModel, a Color, and a Strategy upon construction
  - implements all inherited methods but playerMessenger
    - this way, needless informational messages won't be sent when there is no human player that needs it.
  - addFeature intializes the Player to a Feature.
    - this method is called in the constructor of ThreeTriosPlayerController, so MachinePlayer will always have a Feature when the game starts.
  - makeMove generates a CardMove using the given Strategy, then calls Feature.cardClicked and .cellClicked.

**threetrios.view package**

The text view is the [TextualViewTTM](../src/threetrios/view/HumanPlayer) class, within the view 
package.

The view takes a ThreeTriosModel and StringBuilder as parameters, which are initialized to fields.

The view method render() appends to the Appendable in the format:
    Player: Color
      __
    ___ 
    ----
    Hand:
    Name 1 2 3 4
    Name2 1 2 3 4
    ...
If game is over, instead renders:
    Player: Color
      RR
    BRR 
    BBRR
    Red Wins!

The text view is the TextualViewTTM class, within the view package.

The view takes a ThreeTriosModel and StringBuilder as parameters, which are initialized to fields.

The view method render() appends to the Appendable in the format: Player: Color __ ___ ---- Hand: 
Name 1 2 3 4 Name2 1 2 3 4 ... If game is over, instead renders: Player: Color RR BRR BBRR Red Wins!

TThe ThreeTriosView class serves as the main container for the (GUI of the game. It is responsible 
for organizing and displaying all the visual components, such as the grid, the hands of both 
players, and their interactions. The view is tightly integrated with the model through a read-only 
interface, ensuring that it displays the current game state without directly modifying it. Panels 
for the red and blue player hands are placed on the left and right, respectively, while the central
panel is dedicated to the game grid. The refresh() method plays a critical role in maintaining
synchronization between the model and the view by re-rendering components whenever the game state 
changes. The view also allows for real-time feedback through the highlightCard() method, which 
visually indicates card selection based on user interactions.

The HandPanel represents the collection of cards in a player's hand. This panel vertically 
organizes CardPanel components, each of which represents an individual card. Using a mouse listener,
the HandPanel enables card selection by the user, and through the highlightSelected() method, it 
provides visual feedback by toggling a highlight on the selected card. The panel also manages the 
card layout and spacing, ensuring that the hand remains intuitive and visually appealing. By 
interacting with the parent view, the HandPanel connects the user's actions (e.g., clicking a card)
to the underlying game logic.


The CardPanel is responsible for rendering individual cards within the game. Each card displays 
its attack values in the four cardinal directions (north, south, east, and west) and highlights 
itself when selected. The toggleHighlight()method changes the card’s border to indicate whether 
it is active or selected, providing essential feedback for the player.

The GridPanel represents the game board, displaying the grid cells where cards can be placed. Each 
cell is rendered based on its state: playable, non-playable, or containing a card. If a cell 
contains a card, a temporary CardPanel is created to visually represent it. Mouse interactions 
are supported through the mouseClicked() method, which allows players to select grid cells. 
The panel communicates these interactions to the parent view, which forwards them to the game 
logic. The paintComponent() method is responsible for drawing the cells and their outlines, 
ensuring the grid remains clear and aesthetically consistent.

The JHandPanel is an abstract class that extends JPanel and provides additional functionality for 
highlighting selected cards. It defines the highlightSelected() method, which is implemented by 
HandPanel to toggle card highlights.

The View interface defines the behaviors required by any GUI implementation of the game. The 
refresh() method ensures the view stays up-to-date with the model's state. The addFeature() 
method connects the view to the game's Feature interface, allowing user actions like card selection
or cell clicks to trigger appropriate updates in the game logic. The displayMessage() method 
provides an easy way to show messages to players, such as errors or status updates. Through the
clickToFeature() method, the interface facilitates communication between the user's actions and 
the game logic by parsing commands and forwarding them to the appropriate methods in the Feature
interface. Lastly, highlightCard() ensures that card selections are visually represented in the 
player's hand.


**threetrios package**
[ThreeTrios](../src/threetrios/ThreeTrios) is our main class. 
- Uses main param String[] args as configuration terms: "human" for a human controlled player, and "machine" for a machine player.
  - If machine is the player term it must be followed by either "strategy1" or "strategy2".
- Creates one model to represent game and 2 ThreeTriosPlayerControllers, ActionPlayers, Views (one for each player).
  - ConstructPlayer.createPlayer() is used to build the ActionPlayers based on config terms.

[ConstructPlayer](../src/threetrios/ConstructPlayer) is a builder for ActionPlayers.
- createPlayer(String, String, ROTTM, View, Color) creates a player based on given info:
  - if String1 is human, returns a new HumanPlayer.
  - if String1 is machine, returns a MachinePlayer with:
    - if String2 is strategy1, MaxFlipStrategy.
    - if String2 is strategy2, FourCornersStrategy.
  - if none of these conditions are met, throws IAE.


<u>Quick Start:</u>


**Model Setup:**

‘ConfigurationFileReader reader = new ConfigurationFileReader();’

‘ThreeTriosBasicModel model = newThreeTriosBasicModel(new Random(3));’ ‘model.setup(reader.readFile(FILE_PATH_TO_GRID), reader.readFile(FILE_PATH_TO_DECK);’


**Player Setup:**

"human human": Both players are human.

"strategy1 strategy2": Both players are controlled by strategies.

"human strategy2": A human player vs. a machine.

‘ConstructPlayer factory = new ConstructPlayer();’

‘ActionPlayer redPlayer = factory.createPlayer(args[0], args[1], model, redView, Color.RED);’

‘ActionPlayer bluePlayer = factory.createPlayer(args[2], args[3], model, blueView, Color.BLUE);’


**View Setup:**

‘ThreeTriosView redView = new ThreeTriosView(model);’

‘ThreeTriosView blueView = new ThreeTriosView(model);’


**Controller Setup:**

‘ThreeTriosController redControl = new ThreeTriosPlayerController(redPlayer, redView, model, Color.RED);’

‘ThreeTriosController blueControl = new ThreeTriosPlayerController(bluePlayer, blueView, model, Color.BLUE);’


**Running the game:**

java ThreeTrios human strategy1



_____________

Source Organization

<b>src/threetrios:</b>
root package, which serves as the main entry point for the game and contains utility classes and configuration files.

- <b> ThreeTrios </b>: The main class that initializes and starts the game. This class creates and 
  sets up the model, view, and strategies and connects them to run the game.
- <b> MockNonTrivialStateModel </b>: A mock model that sets up a specific state in the game, useful 
  for testing intermediate game states.
- <b> ConstructPlayer </b>:  A utility class responsible for creating and configuring ActionPlayer 
instances, either as human or machine players, with optional strategies for machine players based 
on the provided input.



<b>src/threetrios/model:</b>
defines the core game model and its associated entities, which represent the data and game logic for ThreeTrios.

- <b>ThreeTriosModel</b>: The primary interface for the game model, which includes all game-related behaviors.
- <b>ReadOnlyThreeTriosModel</b>: A read-only interface for observing the game model without modifying it, which is used by the view.
- <b>ThreeTriosBasicModel</b>: The concrete implementation of the ThreeTriosModel interface, handling all game rules, setup, and turn management.
- <b>Player and HumanPlayer</b>: Represent the players in the game, including methods for interacting with their hands and turns.
- <b>CardinalCard, CardinalValues, CardinalAttackValue</b>: Classes that define the structure of a card and its cardinal values, with the necessary information for determining attack values and directions.
- <b>Grid, GridCell</b>: Represent the game grid and individual cells, including their placeable and occupied states. These classes are responsible for cell-based interactions on the grid.

<b>src/threetrios/player:</b>
contains classes and interfaces that represent and define the behaviors of players in the ThreeTrios 
game, including both human and machine players.

- <b>ActionPlayer</b>:  An interface that defines the actions a player can take, such as adding
features, making moves, and sending messages to the view.

- <b>HumanActionPlayer</b>:  A concrete implementation of ActionPlayer for human players, where all 
- actions are routed through the view and user interactions.

- <b>MachineActionPlayer</b>:  A concrete implementation of ActionPlayer for machine players, using 
- a strategy to automatically generate moves and interact with the game model.



<b>src/threetrios/controller:</b>
Handles player interactions, game logic updates, and communication between the model and view.


- <b>ConfigurationFileReader</b>: Utility for reading configuration files and initializing the game state with board and deck information.
- <b>Feature</b>: Defines the actions a player can perform, such as selecting a card or a grid cell, and links these actions to the game logic.
- <b>Receiver</b>: Provides a mechanism for receiving messages from the model and updating the state of the game accordingly.
- <b>ThreeTriosController</b>:  Defines methods for validating player actions, executing moves, and refreshing the game view.
- <b>ThreeTriosPlayerController</b>: The main controller that connects the model, view, and players, managing player actions and game state updates.
- <b>ViewPlayerFeature</b>: Implements Feature to connect the controller and view, enabling interaction with the player's associated actions like selecting cards and grid cells.


<b>src/threetrios/strategy</b>:
contains various strategies that can be used by players or the system to make moves in the game.

- <b>TTStrategy</b>: The main strategy interface, defining a method for choosing moves based on the current model state.
- <b>MaxFlipStrategy:</b> A strategy implementation focused on maximizing the number of card flips with each move.
- <b>FourCornerStrategy, LastResortStrategy, TieBreakerStrategy:</b> Additional strategy implementations with different decision-making criteria.
- <b>TTStrategyInfallibleStack:</b> A strategy that ensures the move chosen is infallible according to the main strategy's criteria.
- <b>TTStrategyStacks:</b> Allows combining multiple strategies into a stack, prioritizing the first valid move from the stack.
- <b>CardMove, Move:</b> Utility classes that encapsulate a move with its coordinates and associated card, used by strategies for planning actions.


<b>src/threetrios/view</b>:
includes all classes responsible for rendering the game and handling user interactions.

- <b>ThreeTriosView:</b> The primary view class, implementing the View interface, responsible for setting up the game window and displaying the game components.
- <b>GridPanel:</b> Displays the game grid and its cells. It can display cards within cells, and it responds to mouse interactions.
- <b>HandPanel:</b> Represents a player’s hand and displays their cards vertically. It allows for card selection and visual feedback on selection.
- <b>CardPanel:</b> Represents a single card in the game, rendering its attack values in each direction. This panel is used within both HandPanel and GridPanel to display cards.
- <b>View:</b> An interface that establishes required behavior for any view implementation in the game, such as refreshing the display.
- <b>JHandPanel:</b> An abstract class extending JPanel that provides functionality to highlight a selected card within the panel.



