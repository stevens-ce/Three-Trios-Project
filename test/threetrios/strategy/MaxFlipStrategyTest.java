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

/**
 * tests for MaxFlipStrategy specifically.
 */
public class MaxFlipStrategyTest {
  private TTStrategy maxFlip;

  private ThreeTriosModel model;
  private ConfigurationFileReader reader;

  private final String[] squareGrid = {"3 3", "CCC", "CCC", "CCC"};
  private final String[] crossGrid = {"3 3", "XCX", "CCC", "XCX"};

  private static final String START = "hw5" + File.separator + "docs" + File.separator;
  private static final String FULL_CARD_DECK_STR = START + "full_card_deck.txt";
  private String[] fullCardDeck;

  @Before
  public void setUp() {
    Random rand = new Random(3);
    model = new ThreeTriosBasicModel(rand);
    reader = new ConfigurationFileReader();

    fullCardDeck = reader.readFile(FULL_CARD_DECK_STR);

    maxFlip = new MaxFlipStrategy();
  }

  @Test
  public void testWithMock() {
    StringBuilder record = new StringBuilder();
    ThreeTriosModel modelMock = new ModelMockMaxFlip(record);

    modelMock.setup(reader.readFile(START + "board_standard_n.txt"), fullCardDeck);

    CardMove mockMax = maxFlip.chooseMove(modelMock, Color.RED);

    assertEquals(new Move(3, 3), mockMax.move);
    List<Card> lucky = modelMock.getPlayerHand(Color.RED);
    assertEquals(lucky.get(0), mockMax.card);

    String transcript = record.toString();
    String expected = String.format("%s%s%s%s%s%s%s",
            "numCardsFlipped 0 1\n", "numCardsFlipped 0 2\n",
            "numCardsFlipped 1 2\n", "numCardsFlipped 2 1\n", "numCardsFlipped 3 1\n",
            "numCardsFlipped 3 2\n", "numCardsFlipped 3 3\n");

    assertEquals(expected, transcript);
  }

  @Test
  public void testMFStategyIsConsistent() {
    model.setup(crossGrid, fullCardDeck);

    List<Card> redHand = model.getPlayerHand(Color.RED);
    model.playCard(redHand.get(1), 1, 2);

    List<Card> blueHand = model.getPlayerHand(Color.BLUE);

    CardMove mFMove = maxFlip.chooseMove(model, Color.BLUE);

    CardMove expected = new CardMove(blueHand.get(2), new Move(1, 1));
    assertEquals(expected.move, mFMove.move);
    assertEquals(expected.card, mFMove.card);

    CardMove mFMove2 = maxFlip.chooseMove(model, Color.BLUE);

    assertEquals(expected.move, mFMove2.move);
    assertEquals(expected.card, mFMove2.card);
  }

  @Test
  public void testMaxFlipMultipleDirectFlips() {
    model.setup(squareGrid, fullCardDeck);
    List<Card> redHand = model.getPlayerHand(Color.RED);
    List<Card> blueHand = model.getPlayerHand(Color.BLUE);

    model.playCard(redHand.get(3), 0, 2);
    model.playCard(blueHand.get(3), 1, 2);
    model.playCard(redHand.get(1), 1, 0);
    model.playCard(blueHand.get(4), 2, 1);

    CardMove mFMove = maxFlip.chooseMove(model, Color.RED);
    assertEquals(new Move(1, 1), mFMove.move);
    assertEquals(redHand.get(0), mFMove.card);
  }

  @Test
  public void testMaxFlipChainReaction() {
    model.setup(squareGrid, fullCardDeck);
    List<Card> redHand = model.getPlayerHand(Color.RED);
    List<Card> blueHand = model.getPlayerHand(Color.BLUE);

    model.playCard(redHand.get(2), 0, 0); // Thunder
    model.playCard(blueHand.get(4), 0, 2); // Waterfall
    model.playCard(redHand.get(0), 1, 0); // Wave
    model.playCard(blueHand.get(0), 1, 2); // Earthquake
    model.playCard(redHand.get(4), 2, 0); // Storm
    model.playCard(blueHand.get(3), 2, 2); // Lightning

    CardMove mFMove = maxFlip.chooseMove(model, Color.RED);
    assertEquals(new Move(2, 1), mFMove.move);
    assertEquals(redHand.get(1), mFMove.card);
  }
}