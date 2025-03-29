package threetrios.strategy;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Random;

import threetrios.controller.ConfigurationFileReader;
import threetrios.model.Card;
import threetrios.model.Color;
import threetrios.model.ThreeTriosBasicModel;
import threetrios.model.ThreeTriosModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * tests for TTStrategyInfallibleStack and LastResortStrategy.
 */
public class TTStrategyInfallibleStackTest {
  private TTStrategy infallible;
  private TTStrategy lastResort;
  private TTStrategy fourCorners;

  private ThreeTriosModel model;

  private final String[] crossGrid = {"3 3", "XCX", "CCC", "XCX"};
  private final String[] lGrid = {"3 4", "XXXC", "XXCC", "XXXX"};

  private static final String FULL_CARD_DECK_STR = "hw5" + File.separator + "docs"
          + File.separator + "full_card_deck.txt";
  private String[] fullCardDeck;

  @Before
  public void setUp() {
    Random rand = new Random(3);
    model = new ThreeTriosBasicModel(rand);
    ConfigurationFileReader reader = new ConfigurationFileReader();

    fullCardDeck = reader.readFile(FULL_CARD_DECK_STR);

    fourCorners = new FourCornerStrategy();
    lastResort = new LastResortStrategy();

    infallible = new TTStrategyInfallibleStack(fourCorners);
  }

  @Test
  public void testLastResortStrategyIsConsistent() {
    model.setup(crossGrid, fullCardDeck);
    List<Card> blueHand = model.getPlayerHand(Color.BLUE);

    CardMove lRMove = lastResort.chooseMove(model, Color.BLUE);
    CardMove expected = new CardMove(blueHand.get(0), new Move(0, 1));
    assertEquals(expected.move, lRMove.move);
    assertEquals(expected.card, lRMove.card);

    CardMove lRMove2 = lastResort.chooseMove(model, Color.BLUE);
    assertEquals(expected.move, lRMove2.move);
    assertEquals(expected.card, lRMove2.card);
  }

  @Test
  public void testInfallibleReturnsLastResort() {
    model.setup(crossGrid, fullCardDeck);

    List<Card> blueHand = model.getPlayerHand(Color.BLUE);

    CardMove lRMove = lastResort.chooseMove(model, Color.BLUE);
    CardMove expected = new CardMove(blueHand.get(0), new Move(0, 1));

    assertEquals(expected.move, lRMove.move);
    assertEquals(expected.card, lRMove.card);

    CardMove cMove = infallible.chooseMove(model, Color.BLUE);
    assertEquals(expected.move, cMove.move);
    assertEquals(expected.card, cMove.card);
  }

  @Test
  public void testInfallibleAlwaysReturnsSomething() {
    model.setup(crossGrid, fullCardDeck);

    CardMove fCMove = fourCorners.chooseMove(model, Color.BLUE);
    assertNull(fCMove);

    CardMove cMove = infallible.chooseMove(model, Color.BLUE);
    List<Card> blueHand = model.getPlayerHand(Color.BLUE);
    CardMove expected = new CardMove(blueHand.get(0), new Move(0, 1));
    assertEquals(expected.move, cMove.move);
    assertEquals(expected.card, cMove.card);
  }

  @Test
  public void testLastResortUpTrumpsLeft() {
    model.setup(lGrid, fullCardDeck);

    CardMove actual = lastResort.chooseMove(model, Color.RED);
    List<Card> hand = model.getPlayerHand(Color.RED);
    CardMove expected = new CardMove(hand.get(0), new Move(0, 3));

    assertEquals(expected.move, actual.move);
    assertEquals(expected.card, actual.card);
  }
}