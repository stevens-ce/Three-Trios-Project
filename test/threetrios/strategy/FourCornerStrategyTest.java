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
 * tests for FourCornerStrategy specifically.
 */
public class FourCornerStrategyTest {
  private TTStrategy fourCorners;

  private ThreeTriosModel model;

  private static final String FULL_CARD_DECK_STR = "hw5" + File.separator + "docs"
          + File.separator + "full_card_deck.txt";
  private String[] fullCardDeck;
  private String[] crossGrid = {"3 3", "XCX", "CCC", "XCX"};
  private String[] squareGrid = {"3 3", "XCC", "CCC", "CCC"};

  @Before
  public void setUp() {
    Random rand = new Random(3);
    model = new ThreeTriosBasicModel(rand);
    ConfigurationFileReader reader = new ConfigurationFileReader();

    fourCorners = new FourCornerStrategy();

    fullCardDeck = reader.readFile(FULL_CARD_DECK_STR);
  }

  @Test
  public void testFCStrategyIsConsistent() {
    model.setup(squareGrid, fullCardDeck);

    CardMove fMMove = fourCorners.chooseMove(model, Color.RED);

    List<Card> redHand = model.getPlayerHand(Color.RED);
    CardMove expected = new CardMove(redHand.get(0), new Move(0, 2));

    assertEquals(expected.move, fMMove.move);
    assertEquals(expected.card, fMMove.card);

    CardMove fCMove2 = fourCorners.chooseMove(model, Color.RED);

    assertEquals(expected.move, fCMove2.move);
    assertEquals(expected.card, fCMove2.card);
  }

  @Test
  public void testWhenCornersNotLegalStrategyFails() {
    model.setup(crossGrid, fullCardDeck);

    CardMove fail = fourCorners.chooseMove(model, Color.RED);

    assertNull(fail);
  }

  // when all corners are equally guarded
  @Test
  public void testCornersOfEqualDefense() {
    model.setup(squareGrid, fullCardDeck);

    CardMove fCMove = fourCorners.chooseMove(model, Color.RED);
    List<Card> hand = model.getPlayerHand(Color.RED);

    assertEquals("should return topright", new Move(0, 2), fCMove.move);
    assertEquals("should return first card", hand.get(0), fCMove.card);
  }

  @Test
  public void testFourCornerPicksMostGuardedMove() {
    String[] isolatedGrid = {"4 4", "CCXX", "CXXX", "CXXX", "CXXC"};
    model.setup(isolatedGrid, fullCardDeck);

    CardMove fCMove = fourCorners.chooseMove(model, Color.RED);
    List<Card> hand = model.getPlayerHand(Color.RED);

    assertEquals(new Move(3, 3), fCMove.move);

    assertEquals(hand.get(0), fCMove.card);
  }

  @Test
  public void testFourCornerTiedGuardPicksUpper() {
    String[] tiedGrid = {"4 3", "CCX", "XXX", "XXC", "XXC"};
    model.setup(tiedGrid, fullCardDeck);

    CardMove fCMove = fourCorners.chooseMove(model, Color.RED);
    assertEquals(0, fCMove.move.row);
    assertEquals(0, fCMove.move.col);

    List<Card> hand = model.getPlayerHand(Color.RED);
    assertEquals(hand.get(1), fCMove.card);
  }
}