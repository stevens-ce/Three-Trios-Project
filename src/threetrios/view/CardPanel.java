package threetrios.view;

import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Color;

import threetrios.model.CardinalCard;
import threetrios.model.Direction;

import java.awt.BasicStroke;

/**
 * This represents the card of the ThreeTrios game. It extends the JPanel and it renders the card
 * with the attack values in each direction.
 */
public class CardPanel extends JPanel {
  public static final int CARD_OUTLINE = 6;
  private final CardinalCard card;
  private boolean selected = false;

  private static final int HIGHLIGHT_WIDTH = 8;

  /**
   * This is a constructor for the card panel. It initializes the card, and the player's color.
   *
   * @param card        the card in a user's hand.
   * @param playerColor the color of the player to paint the card.
   */
  public CardPanel(CardinalCard card, threetrios.model.Color playerColor) {
    this.card = card;

    if (playerColor == threetrios.model.Color.RED) {
      setBackground(new java.awt.Color(239, 88, 88));
    } else if (playerColor == threetrios.model.Color.BLUE) {
      setBackground(new java.awt.Color(58, 138, 253));
    }

    setPreferredSize(new java.awt.Dimension(30, 80));
  }

  /**
   * This sets the status of the card to selected if highlight == true. It then repaints the card to
   * show an inner highlight.
   *
   * @param highlight a boolean value that represents whether a card is to be highlighted.
   */
  public void toggleHighlight(boolean highlight) {
    this.selected = highlight;
    repaint();
  }

  /**
   * This paints the card, and the attacks values for each of the directions. It also highlights
   * the card if it is selected.
   *
   * @param g the <code>Graphics</code> object to protect the rendering of card.
   */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;

    if (this.selected) {
      g2d.setColor(java.awt.Color.YELLOW);
      g2d.setStroke(new BasicStroke(HIGHLIGHT_WIDTH));
      g2d.drawRect(0, 0, getWidth(), getHeight());
    }

    // font and color for text
    g2d.setColor(Color.BLACK);
    g2d.setStroke(new BasicStroke(CARD_OUTLINE));
    g2d.setFont(new Font("Arial", Font.BOLD, 20));

    // Getting cardinal values
    String northValue = card.getCardinalValue(Direction.NORTH).attackValue.toString();
    String southValue = card.getCardinalValue(Direction.SOUTH).attackValue.toString();
    String eastValue = card.getCardinalValue(Direction.EAST).attackValue.toString();
    String westValue = card.getCardinalValue(Direction.WEST).attackValue.toString();

    // Draw each value in the order of NSEW
    g2d.drawString(northValue, getWidth() / 2 - 5, 20);
    g2d.drawString(southValue, getWidth() / 2 - 5, getHeight() - 10);
    g2d.drawString(eastValue, getWidth() - 20, getHeight() / 2);
    g2d.drawString(westValue, 10, getHeight() / 2);
  }
}
