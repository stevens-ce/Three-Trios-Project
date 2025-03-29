package threetrios.view;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

import threetrios.model.ThreeTriosBasicModel;
import threetrios.model.ThreeTriosModel;
import threetrios.model.Card;
import threetrios.model.Direction;

import threetrios.controller.ConfigurationFileReader;

import java.util.List;
import java.util.Arrays;

/**
 * tests TextualViewTTT methods.
 */
public class TextualViewTTMTest {

  private ThreeTriosModel mockModel;
  private StringBuilder appendable;
  private TextualViewTTM view;
  private ConfigurationFileReader reader;

  private static final String BASE_PATH = "hw5" + File.separator + "docs";
  private static final String GRID_TINY = BASE_PATH + File.separator
          + "board_tiny_only_holes.txt";
  private static final String GRID_REACHABLE = BASE_PATH + File.separator
          + "board_reachable.txt";
  private static final String FULL_CARD_DECK = BASE_PATH + File.separator
          + "full_card_deck.txt";
  private static final String SMALL_CARD_DECK = BASE_PATH + File.separator
          + "small_card_deck.txt";

  @Before
  public void setUp() {
    mockModel = new ThreeTriosBasicModel();
    appendable = new StringBuilder();
    view = new TextualViewTTM(mockModel, appendable);
    reader = new ConfigurationFileReader();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nullModelThrowsException() {
    TextualViewTTM viewFail = new TextualViewTTM(null, appendable);
  }

  @Test
  public void properlyDisplaysHoleGrid() {
    mockModel.setup(reader.readFile(GRID_TINY), reader.readFile(SMALL_CARD_DECK));
    view.render();
    String expected = "Player: Red\n"
            + "  \n"
            + "  \n"
            + "nullWins!\n";
    assertEquals(expected, appendable.toString());
  }

  @Test
  public void displaysHand() {
    mockModel.setup(reader.readFile(GRID_REACHABLE), reader.readFile(FULL_CARD_DECK));
    view.render();
    String viewHand = getPartOfRender(5);
    String expectedHand = stringRepOfCurrentHand();
    assertEquals(expectedHand, viewHand);
  }

  private String getPartOfRender(int startIdx) {
    String[] viewRender = appendable.toString().split("\n");
    String[] partialRender = Arrays.copyOfRange(viewRender, startIdx, viewRender.length);
    StringBuilder buildPart = new StringBuilder("");
    for (String line : partialRender) {
      buildPart.append(line + "\n");
    }
    return buildPart.toString();
  }

  @Test
  public void renderUpdatesWithPlayCard() {
    mockModel.setup(reader.readFile(GRID_REACHABLE), reader.readFile(FULL_CARD_DECK));
    view.render();
    String originalRender = appendable.toString();
    String originalExpected = "Player: Red\n"
            + "___\n"
            + "___\n"
            + "___\n"
            + "Hand:\n"
            + stringRepOfCurrentHand();
    assertEquals(originalExpected, originalRender);

    List<Card> redHand = mockModel.getPlayerHand(mockModel.getCurrentPlayer());
    int lengthOfOgRender = 5 + redHand.size();
    Card playedCard = redHand.get(0);
    mockModel.playCard(redHand.get(0), 1, 1);

    view.render();
    String updatedRender = getPartOfRender(lengthOfOgRender);
    String updatedExpected = "Player: Blue\n"
            + "___\n"
            + "_R_\n"
            + "___\n"
            + "Hand:\n"
            + stringRepOfCurrentHand();
    assertEquals(updatedExpected, updatedRender);
  }

  private String stringRepOfCurrentHand() {
    List<Card> currentHand = mockModel.getPlayerHand(mockModel.getCurrentPlayer());
    StringBuilder expectedHand = new StringBuilder("");
    for (Card card : currentHand) {
      String cardFormat = card.getIdentifier() + " "
              + card.getCardinalValue(Direction.NORTH).attackValue + " "
              + card.getCardinalValue(Direction.SOUTH).attackValue + " "
              + card.getCardinalValue(Direction.EAST).attackValue + " "
              + card.getCardinalValue(Direction.WEST).attackValue;
      expectedHand.append(cardFormat + "\n");
    }
    return expectedHand.toString();
  }

}