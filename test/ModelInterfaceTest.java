import org.junit.Test;
import org.junit.Before;

import java.io.File;

import java.util.List;
import java.util.Random;

import threetrios.model.Card;
import threetrios.model.CardinalCard;
import threetrios.model.CardinalValues;
import threetrios.model.Color;
import threetrios.model.Direction;
import threetrios.model.Position;
import threetrios.model.ThreeTriosModel;
import threetrios.model.ThreeTriosBasicModel;
import threetrios.model.GridCell;

import threetrios.controller.ConfigurationFileReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * tests all public ThreeTriosModel methods.
 */
public class ModelInterfaceTest {

  private ThreeTriosModel model;
  private ThreeTriosModel winnerModel;

  private static final String BASE_PATH = "hw5" + File.separator + "docs";
  private static final String GRID_NO_HOLES_STR = BASE_PATH + File.separator
          + "board_no_holes.txt";
  private String[] gridNoHoles;
  private static final String GRID_WINNER_STR = BASE_PATH + File.separator
          + "board_winner.txt";
  private String[] gridWinner;
  private static final String GRID_REACHABLE_STR = BASE_PATH + File.separator
          + "board_reachable.txt";
  private String[] gridReachable;
  private static final String GRID_UNREACHABLE_STR = BASE_PATH + File.separator
          + "board_unreachable.txt";
  private String[] gridUnreachable;
  private static final String FULL_CARD_DECK_STR = BASE_PATH + File.separator
          + "full_card_deck.txt";
  private String[] fullCardDeck;
  private static final String SMALL_CARD_DECK_STR = BASE_PATH + File.separator
          + "small_card_deck.txt";
  private String[] smallCardDeck;

  @Before
  public void setUp() {
    Random random = new Random(2);
    model = new ThreeTriosBasicModel(random);
    winnerModel = new ThreeTriosBasicModel(random);
    ConfigurationFileReader reader = new ConfigurationFileReader();

    gridNoHoles = reader.readFile(GRID_NO_HOLES_STR);
    gridWinner = reader.readFile(GRID_WINNER_STR);
    gridReachable = reader.readFile(GRID_REACHABLE_STR);
    gridUnreachable = reader.readFile(GRID_UNREACHABLE_STR);
    fullCardDeck = reader.readFile(FULL_CARD_DECK_STR);
    smallCardDeck = reader.readFile(SMALL_CARD_DECK_STR);

    winnerModel.setup(gridWinner, fullCardDeck);
  }

  // SET UP
  @Test
  public void testSetupValidGridAndDeck() {
    // test setting up with valid grid and deck files
    model.setup(gridNoHoles, fullCardDeck);
    assertEquals(4, model.getGrid().length);
    assertEquals(4, model.getGrid()[0].length);
  }

  @Test
  public void testSetupInvalidGridFile() {
    String[] invalidConfig = {"hey", "what's", "up"};
    assertThrows(IllegalArgumentException.class, () -> {
      model.setup(invalidConfig, fullCardDeck);
    });
  }

  @Test
  public void testSetupInvalidDeckFile() {
    String[] invalidConfig = {"oh", "not", "much"};
    assertThrows(IllegalArgumentException.class, () -> {
      model.setup(gridNoHoles, invalidConfig);
    });
  }

  // PLAYCARD
  private CardinalValues createMockCardinalValues() {
    return new CardinalValues(5, 3, 1, 2);
  }

  @Test
  public void testPlayCardSuccess() {
    model.setup(gridNoHoles, fullCardDeck);
    List<Card> hand = model.getPlayerHand(model.getCurrentPlayer());
    Card card = hand.get(0);
    model.playCard(card, 0, 0);
    assertEquals(card, model.getGrid()[0][0].getCard());
  }

  @Test
  public void testPlayCardGameNotStarted() {
    CardinalValues values = createMockCardinalValues();
    CardinalCard card = new CardinalCard(values, "Card1");
    assertThrows(IllegalStateException.class, () -> {
      model.playCard(card, 0, 0);
    });
  }

  @Test
  public void testPlayCardCardNotInHand() {
    model.setup(gridNoHoles, fullCardDeck);
    CardinalValues values = createMockCardinalValues();
    CardinalCard fakeCard = new CardinalCard(values, "Card2");
    assertThrows(IllegalArgumentException.class, () -> {
      model.playCard(fakeCard, 0, 0);
    });
  }

  @Test
  public void testPlayCardOccupiedCell() {
    model.setup(gridNoHoles, fullCardDeck);
    List<Card> hand1 = model.getPlayerHand(model.getCurrentPlayer());
    Card card1 = hand1.get(0);
    model.playCard(card1, 0, 0);
    assertFalse(model.isMoveLegal(0, 0));
    List<Card> hand2 = model.getPlayerHand(model.getCurrentPlayer());
    Card card2 = hand2.get(0);
    assertThrows(IllegalStateException.class, () -> {
      model.playCard(card2, 0, 0);
    });
  }

  @Test
  public void testIsGameOver() {
    playToEndGame();
    assertTrue(winnerModel.isGameOver());
  }

  private void playToEndGame() {
    List<Card> hand1 = winnerModel.getPlayerHand(winnerModel.getCurrentPlayer());
    winnerModel.playCard(hand1.get(0), 0, 0);
    List<Card> hand2 = winnerModel.getPlayerHand(winnerModel.getCurrentPlayer());
    winnerModel.playCard(hand2.get(0), 1, 0);
  }

  private static void displayHandContents(List<Card> hand) {
    for (Card card : hand) {
      StringBuilder cardRep = new StringBuilder("");
      cardRep.append(card.getIdentifier());
      for (Direction dir : Direction.values()) {
        cardRep.append(" " + card.getCardinalValue(dir).attackValue);
      }
    }
  }

  @Test
  public void testWinner() {
    playToEndGame();
    Color winner = winnerModel.winner();
    assertNotNull("Expected a winner, but winner was null", winner);
    assertEquals(Color.BLUE, winner);
  }

  @Test
  public void testSetupReachableGrid() {
    model.setup(gridReachable, fullCardDeck);
    assertEquals(3, model.getGrid().length);
    assertEquals(3, model.getGrid()[0].length);
    Position position = new Position(0, 0);
    assertNotNull("Expected cell to be reachable at (0, 0)",
            model.getGrid()[position.getRow()][position.getCol()]);
  }

  @Test
  public void testSetupUnreachableGrid() {
    model.setup(gridUnreachable, fullCardDeck);
    assertEquals(4, model.getGrid().length);
    assertEquals(4, model.getGrid()[0].length);
    List<Card> hand = model.getPlayerHand(model.getCurrentPlayer());
    assertThrows(IllegalStateException.class, () -> {
      Card card = hand.get(0);
      model.playCard(card, 0, 2); // (0, 2) is a blocked position
    });
  }

  @Test
  public void testSetupWithSmallDeckInadequateSize() {
    assertThrows(IllegalArgumentException.class, () -> {
      model.setup(gridReachable, smallCardDeck);
    });
  }

  //getCell returns copy aka the real cell should not mutate
  //getCell returns the right cell

  @Test
  public void testGetCellReturnsCopy() {
    model.setup(gridWinner, fullCardDeck);
    GridCell cell = model.getCell(0, 0);
    assertFalse(cell.hasCard());

    CardinalValues values = new CardinalValues(1, 2, 3, 4);
    cell.placeCard(new CardinalCard(values, "card 1"), Color.RED);
    assertTrue(cell.hasCard());

    GridCell cellAgain = model.getCell(0, 0);
    assertFalse(cellAgain.hasCard());
  }

  @Test
  public void testGetOwnerOfCellEmpty() {
    model.setup(gridWinner, fullCardDeck);
    assertNull(model.getOwnerOfCell(model.getCell(0, 0)));
  }

  @Test
  public void testGetOwnerOfCellFull() {
    model.setup(gridWinner, fullCardDeck);
    List<Card> hand = model.getPlayerHand(model.getCurrentPlayer());
    model.playCard(hand.get(0), 0, 0);
    assertEquals(Color.RED, model.getOwnerOfCell(model.getCell(0, 0)));
  }

  @Test
  public void testIsMoveLegalHole() {
    model.setup(gridWinner, fullCardDeck);
    assertFalse(model.isMoveLegal(0, 1));
  }

  @Test
  public void testIsMoveLegalFull() {
    model.setup(gridWinner, fullCardDeck);
    List<Card> hand = model.getPlayerHand(model.getCurrentPlayer());
    model.playCard(hand.get(0), 0, 0);
    assertFalse(model.isMoveLegal(0, 0));
  }

  @Test
  public void testIsMoveLegalOutOfBounds() {
    model.setup(gridWinner, fullCardDeck);
    assertFalse(model.isMoveLegal(2, 2));
  }

  @Test
  public void testIsMoveLegalEmptyCardCell() {
    model.setup(gridWinner, fullCardDeck);
    assertTrue(model.isMoveLegal(0, 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNumCardsFlippedInvalidMove() {
    model.setup(gridWinner, fullCardDeck);
    assertFalse(model.isMoveLegal(0, 1));
    CardinalValues values = createMockCardinalValues();
    CardinalCard fakeCard = new CardinalCard(values, "Card2");
    model.numCardsFlipped(fakeCard, Color.RED, 0, 1);
  }

  @Test
  public void testNumCardsFlippedNoNeighbors() {
    model.setup(gridWinner, fullCardDeck);
    CardinalValues values = createMockCardinalValues();
    CardinalCard fakeCard = new CardinalCard(values, "Card2");
    int numFlipped = model.numCardsFlipped(fakeCard, Color.RED, 0, 0);
    assertEquals(0, numFlipped);
  }

  @Test
  public void testNumCardsFlippedSameColorNeighbor() {
    model.setup(gridWinner, fullCardDeck);
    List<Card> hand = model.getPlayerHand(Color.RED);
    model.playCard(hand.get(0), 0, 0);
    CardinalValues values = createMockCardinalValues();
    CardinalCard fakeCard = new CardinalCard(values, "Card2");
    int numFlipped = model.numCardsFlipped(fakeCard, Color.RED, 1, 0);
    assertEquals(0, numFlipped);
  }

  @Test
  public void testNumCardsFlippedDiffColorNeighbor() {
    model.setup(gridWinner, fullCardDeck);
    List<Card> hand = model.getPlayerHand(Color.RED);
    model.playCard(hand.get(0), 0, 0);
    CardinalValues values = new CardinalValues(10, 10, 10, 10);
    CardinalCard fakeCard = new CardinalCard(values, "Card2");
    int numFlipped = model.numCardsFlipped(fakeCard, Color.BLUE, 1, 0);
    assertEquals(1, numFlipped);
  }

  @Test
  public void testNumCardsFlippedChainReaction() {
    model.setup(gridReachable, fullCardDeck);
    List<Card> redHand = model.getPlayerHand(Color.RED);
    List<Card> blueHand = model.getPlayerHand(Color.BLUE);

    model.playCard(redHand.get(0), 0, 0);
    model.playCard(blueHand.get(0), 0, 2); // Thunder 2 2 3 1
    model.playCard(redHand.get(1), 1, 0);
    model.playCard(blueHand.get(4), 1, 2); // Lightning 4 3 2 1
    model.playCard(redHand.get(2), 2, 0);
    model.playCard(blueHand.get(2), 2, 2); // Earth 4 1 3 2

    CardinalValues values = new CardinalValues(6, 6, 6, 6);
    Card card = new CardinalCard(values, "Angel");

    assertEquals(1, model.numCardsFlipped(card, Color.RED, 0, 1));
    assertEquals(2, model.numCardsFlipped(card, Color.RED, 1, 1));
    assertEquals(3, model.numCardsFlipped(card, Color.RED, 2, 1));
  }

  @Test
  public void testGetCurrentScoreHandSizeUntilFlip() {
    model.setup(gridReachable, fullCardDeck);
    List<Card> redHand = model.getPlayerHand(Color.RED);
    List<Card> blueHand = model.getPlayerHand(Color.BLUE);

    int initScoreRed = redHand.size();
    int initScoreBlue = blueHand.size();
    assertEquals(initScoreRed, model.currentScore(Color.RED));
    assertEquals(initScoreBlue, model.currentScore(Color.BLUE));

    model.playCard(redHand.get(0), 1, 1);
    assertEquals(initScoreRed, model.currentScore(Color.RED));

    assertEquals(1, model.numCardsFlipped(blueHand.get(3), Color.BLUE, 1, 2));

    model.playCard(blueHand.get(3), 1, 2);
    assertEquals(initScoreBlue + 1, model.currentScore(Color.BLUE));
    assertEquals(initScoreRed - 1, model.currentScore(Color.RED));
  }
}
