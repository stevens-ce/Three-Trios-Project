package threetrios.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

import threetrios.controller.Receiver;

/**
 * Implementation of TreeTriosModel that uses CardinalCard, a Grid grid, and has two players.
 * Players are Red and Blue.
 */
public class ThreeTriosBasicModel implements ThreeTriosModelMessenger<CardinalCard> {
  private Grid grid;
  private List<CardinalCard> deck;
  private Player<CardinalCard> redPlayer;
  private Player<CardinalCard> bluePlayer;
  private int cardCellCount;
  private final Random shuffler;
  private List<Receiver> receivers;

  /**
   * Constructor initializing redPlayer and bluePlayer, and deck to an empty ArrayList.
   * redPlayer always goes first.
   * INVARIANT: It is always either redPlayer or bluePlayer's turn (never neither or both).
   */
  public ThreeTriosBasicModel() {
    setsGame();
    shuffler = new Random();
  }

  /**
   * Constructor that specifies the Random that shuffles the deck. All other functions mirror
   * default constructor.
   *
   * @param shuffler Random obj
   */
  public ThreeTriosBasicModel(Random shuffler) {
    setsGame();
    if (shuffler == null) {
      throw new IllegalArgumentException("bunk shuffler.");
    }
    this.shuffler = shuffler;
  }

  @Override
  public void playCard(CardinalCard card, int row, int col) throws IllegalStateException,
          IllegalArgumentException {
    gameStartedOrFailure();
    gameOverFailure();
    placingPhase(card, new Position(row, col));
    battlePhase(card);
    if (isGameOver()) {
      sendGameStateMsg();
      sendGameOverMsg();
    } else {
      nextTurn();
    }
  }

  @Override
  public void setup(String[] gridConfig, String[] deckConfig) {
    gameStartedIsFailure();
    grid = new Grid(gridConfig);
    addToDeckFromFile(deckConfig);
    cardCellCount = grid.getCardCellCount();
    if (cardCellCount >= deck.size()) {
      deck.clear();
      throw new IllegalArgumentException("Deck size is inadequate!");
    }
    Collections.shuffle(deck, shuffler);
    int handSize = (cardCellCount + 1) / 2;
    deal(redPlayer, handSize);
    deal(bluePlayer, handSize);
    cueNextMove();
  }

  // observations

  @Override
  public boolean isGameOver() {
    return grid.areAllCellsOccupied();
  }

  @Override
  public Color winner() {
    gameStartedOrFailure();
    if (!isGameOver()) {
      throw new IllegalStateException("Game not yet over.");
    }
    int redCards = grid.countCellsOfColor(Color.RED);
    int blueCards = grid.countCellsOfColor(Color.BLUE);

    if (redCards > blueCards) {
      return Color.RED;
    } else if (blueCards > redCards) {
      return Color.BLUE;
    } else {
      return null;
    }
  }

  @Override
  public GridCell[][] getGrid() {
    return grid.deepCopy();
  }

  @Override
  public Color getCurrentPlayer() {
    return redPlayer.isTurn() ? Color.RED : Color.BLUE;
  }

  @Override
  public CardinalCard getCardFromHand(int cardIdx, Color player) {
    List<CardinalCard> hand = getPlayerHand(player);
    try {
      return hand.get(cardIdx);
    } catch (IndexOutOfBoundsException ex) {
      throw new IllegalArgumentException("card index not in hand.");
    }
  }

  @Override
  public List<CardinalCard> getPlayerHand(Color player) {
    return playerOfColor(player).getHand();
  }

  @Override
  public List<Color> getPlayers() {
    return List.of(Color.RED, Color.BLUE);
  }

  @Override
  public int getGridLength() {
    return grid.getLength();
  }

  @Override
  public int getGridWidth() {
    return grid.getWidth();
  }

  @Override
  public GridCell getCell(int row, int col) {
    GridCell realCell = grid.cellAt(new Position(row, col));
    GridCell copyCell = new GridCell(realCell.isPlaceable());
    if (realCell.hasCard()) {
      copyCell.placeCard(realCell.getCard(), realCell.getColor());
    }
    return copyCell;
  }

  // necessary? why can't the lazy view just call this itself
  @Override
  public Color getOwnerOfCell(GridCell cell) {
    return cell.getColor();
  }

  @Override
  public boolean isMoveLegal(int row, int col) {
    if (row < 0 || col < 0 || row >= getGridLength() || col >= getGridWidth()) {
      return false;
    }
    GridCell proposedCell = getCell(row, col);
    return proposedCell.isValid();
  }

  @Override
  public int numCardsFlipped(Card card, Color player, int row, int col) {
    Grid simGrid = createSimGrid();
    try {
      simGrid.place(card, new Position(row, col), player);
      int initColorCount = simGrid.countCellsOfColor(player);
      simGrid.engageAdj(card);
      int finalColorCount = simGrid.countCellsOfColor(player);
      return finalColorCount - initColorCount;
    } catch (IllegalStateException ex) {
      throw new IllegalArgumentException("can't place card there.");
    }
  }

  @Override
  public int currentScore(Color player) {
    return playerOfColor(player).getHand().size()
            + grid.countCellsOfColor(player);
  }

  @Override
  public void setReceiver(Receiver receiver) {
    receivers.add(receiver);
  }

  @Override
  public void sendMessageAll(String message) {
    for (Receiver receiver : receivers) {
      receiver.messageSent(message);
    }
  }

  // send messages
  private void cueNextMove() {
    sendMessageAll(playerOfColor(getCurrentPlayer()).toString() + ": it's your turn.");
    sendGameStateMsg();
    sendMessageAll("Make next move.");
  }

  private void sendGameStateMsg() {
    sendMessageAll("Update grid state.");
  }

  private void sendGameOverMsg() {
    String winner = winner() == null ? "No One" : winner().toString() + " Player";
    String winningMsg = String.format("Game is Over! %s Wins!", winner);
    sendMessageAll(winningMsg);
  }

  // to simulate moves
  // creates a deep copy of the current grid, for the purpose of simulating moves
  private Grid createSimGrid() {
    String[] gridConfig = grid.gridToConfigArray();
    Map<Position, GridCell> cardsOnGrid = grid.allCardsOnGrid();
    Grid simGrid = new Grid(gridConfig);
    for (Position pos : cardsOnGrid.keySet()) {
      GridCell ogCell = cardsOnGrid.get(pos);
      simGrid.cellAt(pos).placeCard(ogCell.getCard(), ogCell.getColor());
    }
    return simGrid;
  }

  // color to player
  private Player playerOfColor(Color color) {
    switch (color) {
      case RED:
        return redPlayer;
      case BLUE:
        return bluePlayer;
      default:
        throw new IllegalArgumentException("not a valid color.");
    }
  }

  // playCard methods
  private void placingPhase(CardinalCard card, Position pos) {
    Player currentPlayer = playerOfColor(getCurrentPlayer());
    if (!currentPlayer.getHand().contains(card)) {
      throw new IllegalArgumentException("Card is not in player's hand.");
    }
    grid.place(card, pos, currentPlayer.getColor());
    currentPlayer.removeFromHand(card);
  }

  private void battlePhase(CardinalCard card) {
    grid.engageAdj(card);
  }

  private void nextTurn() {
    redPlayer.nextTurn();
    bluePlayer.nextTurn();
    cueNextMove();
  }

  // constructor helper

  private void setsGame() {
    redPlayer = new HumanPlayer<CardinalCard>(Color.RED);
    bluePlayer = new HumanPlayer<CardinalCard>(Color.BLUE);
    redPlayer.nextTurn();
    deck = new ArrayList<CardinalCard>();
    receivers = new ArrayList<Receiver>();
  }

  // setup helpers

  /*
    deckFile should be of form:
    {"CARD_NAME NORTH SOUTH EAST WEST",
    "CARD_NAME NORTH SOUTH EAST WEST",
    "CARD_NAME NORTH SOUTH EAST WEST",
    ...}
  */
  private void addToDeckFromFile(String[] deckFile) {
    for (String card : deckFile) {
      String[] attributes = card.split(" ");
      if (attributes.length != 5) {
        throw new IllegalArgumentException(
                "deck file not correctly formatted.");
      }
      try {
        int north = Integer.parseInt(attributes[1]);
        int south = Integer.parseInt(attributes[2]);
        int east = Integer.parseInt(attributes[3]);
        int west = Integer.parseInt(attributes[4]);
        createAndAddCardToDeck(attributes[0], north, south, east, west);
      } catch (NumberFormatException ex) {
        throw new IllegalArgumentException("deck file lacks proper attack value ints.");
      }
    }
  }

  private void createAndAddCardToDeck(String id, int north, int south, int east, int west) {
    try {
      CardinalValues values = new CardinalValues(north, south, east, west);
      CardinalCard card = new CardinalCard(values, id);
      if (deck.contains(card)) {
        throw new IllegalArgumentException("duplicate card configuations.");
      }
      deck.add(card);
    } catch (IllegalArgumentException ex) {
      throw new IllegalArgumentException("file contained invalid NSEW value.");
    }
  }

  private void deal(Player player, int handSize) {
    if (handSize != (cardCellCount + 1) / 2) {
      throw new IllegalArgumentException("invalid hand size.");
    }
    for (int deckIdx = 0; deckIdx < handSize; deckIdx++) {
      player.addToHand(deck.remove(0));
    }
  }

  // game started/over state helpers

  private void gameOverFailure() {
    if (isGameOver()) {
      throw new IllegalStateException("game has already ended");
    }
  }

  private boolean gameStarted() {
    return deck != null && grid != null;
  }

  private void gameStartedOrFailure() throws IllegalStateException {
    if (!gameStarted()) {
      throw new IllegalStateException("game not yet started.");
    }
  }

  private void gameStartedIsFailure() throws IllegalStateException {
    if (gameStarted()) {
      throw new IllegalStateException("game has already started.");
    }
  }
}

