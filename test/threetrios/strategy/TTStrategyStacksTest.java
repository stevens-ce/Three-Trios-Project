package threetrios.strategy;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import java.util.Random;
import java.util.List;

import threetrios.model.ThreeTriosModel;
import threetrios.model.ThreeTriosBasicModel;
import threetrios.model.Card;
import threetrios.model.Color;
import threetrios.model.Direction;
import threetrios.controller.ConfigurationFileReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * tests for TTStrategyStacksTest & test involving comparison btwn TTTStrategy implementations.
 */
public class TTStrategyStacksTest {
  private TTStrategy maxFlip;
  private TTStrategy lastResort;
  private TTStrategy fourCorners;
  private TTStrategy fourMaxStrat;

  private ThreeTriosModel model;

  private final String[] crossGrid = {"3 3", "XCX", "CCC", "XCX"};
  private final String[] lGrid = {"3 4", "XXXC", "XXCC", "XXXX"};
  private final String[] holeGrid = {"3 3", "XXX", "XXX", "XXX"};
  private final String[] squareGrid = {"3 3", "CCC", "CCC", "CCC"};

  private static final String FULL_CARD_DECK_STR = "hw5" + File.separator + "docs"
          + File.separator + "full_card_deck.txt";
  private String[] fullCardDeck;

  @Before
  public void setup() {
    Random rand = new Random(3);
    model = new ThreeTriosBasicModel(rand);
    ConfigurationFileReader reader = new ConfigurationFileReader();

    fullCardDeck = reader.readFile(FULL_CARD_DECK_STR);

    maxFlip = new MaxFlipStrategy();
    fourCorners = new FourCornerStrategy();
    lastResort = new LastResortStrategy();

    fourMaxStrat = new TTStrategyStacks(fourCorners, maxFlip);
  }

  @Test
  public void testStrategyStackIsConsistent() {
    model.setup(crossGrid, fullCardDeck);

    List<Card> redHand = model.getPlayerHand(Color.RED);
    model.playCard(redHand.get(1), 1, 2);

    List<Card> blueHand = model.getPlayerHand(Color.BLUE);

    CardMove fMMove = fourMaxStrat.chooseMove(model, Color.BLUE);

    CardMove expected = new CardMove(blueHand.get(2), new Move(1, 1));
    assertEquals(expected.move, fMMove.move);
    assertEquals(expected.card, fMMove.card);

    CardMove fMMove2 = fourMaxStrat.chooseMove(model, Color.BLUE);

    assertEquals(expected.move, fMMove2.move);
    assertEquals(expected.card, fMMove2.card);

  }

  @Test
  public void testStackedStrategyFirstStratNull() {
    model.setup(crossGrid, fullCardDeck);
    List<Card> redHand = model.getPlayerHand(Color.RED);

    model.playCard(redHand.get(1), 1, 2);

    CardMove fCMove = fourCorners.chooseMove(model, Color.BLUE);
    assertNull(fCMove);

    List<Card> blueHand = model.getPlayerHand(Color.BLUE);
    CardMove fMMove = fourMaxStrat.chooseMove(model, Color.BLUE);
    CardMove expected = new CardMove(blueHand.get(2), new Move(1, 1));
    assertEquals(expected.move, fMMove.move);
    assertEquals(expected.card, fMMove.card);
  }

  @Test
  public void testStrategiesFailIfGameOver() {
    model.setup(holeGrid, fullCardDeck);
    Exception lRException = assertThrows(IllegalStateException.class, () -> {
      lastResort.chooseMove(model, Color.RED);
    });
    assertEquals("no moves when game is over.", lRException.getMessage());

    Exception fCException = assertThrows(IllegalStateException.class, () -> {
      fourCorners.chooseMove(model, Color.RED);
    });
    assertEquals("no moves when game is over.", fCException.getMessage());

    Exception mFException = assertThrows(IllegalStateException.class, () -> {
      maxFlip.chooseMove(model, Color.RED);
    });
    assertEquals("no moves when game is over.", mFException.getMessage());
  }

  @Test
  public void testMaxFlipFourCornersReturnDifferent() {
    model.setup(squareGrid, fullCardDeck);

    List<Card> redHand = model.getPlayerHand(Color.RED);
    model.playCard(redHand.get(0), 1, 1);

    List<Card> blueHand = model.getPlayerHand(Color.BLUE);

    CardMove fCMove = fourCorners.chooseMove(model, Color.BLUE);
    assertEquals(new Move(0, 0), fCMove.move);
    assertEquals(blueHand.get(0), fCMove.card);

    CardMove mFMove = maxFlip.chooseMove(model, Color.BLUE);
    assertEquals(new Move(1, 0), mFMove.move);
    assertEquals(blueHand.get(0), mFMove.card);
  }

  // so we can see what's in the hand
  private static void displayHandContents(List<Card> hand) {
    for (Card card : hand) {
      StringBuilder cardRep = new StringBuilder();
      cardRep.append(card.getIdentifier());
      for (Direction dir : Direction.values()) {
        cardRep.append(" " + card.getCardinalValue(dir).attackValue);
      }
    }
  }

}