package threetrios.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.BasicStroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;


import threetrios.model.CardinalCard;
import threetrios.model.Color;
import threetrios.model.ReadOnlyThreeTriosModel;

/**
 * This represents the panel for a player's hand in the ThreeTrios game. It extends the JPanel, and
 * it renders a player's hand vertically, manages the card selection, highlighting, and
 * separates the cards.
 */
public class HandPanel extends JHandPanel implements MouseListener {
  private static final int CARD_OUTLINE = 3;
  private int selectedCardIndex = -1;
  private final List<CardinalCard> playerHand;
  private final List<CardPanel> cardPanels;
  private final Color playerColor;
  private final ThreeTriosView parentView;

  /**
   * This is the constructor for a specific player's hand panel. It initializes the player's hand,
   * setting up the background color, layout, and adds each card as a CardPanel.
   *
   * @param playerColor the color of the player whose hand is being displayed.
   * @param model       the read-only model of the game.
   */
  public HandPanel(Color playerColor, ReadOnlyThreeTriosModel model, ThreeTriosView parentView) {
    this.cardPanels = new ArrayList<>();
    this.parentView = parentView;
    this.playerColor = playerColor;
    this.playerHand = model.getPlayerHand(playerColor);
    // Vertical layout for cards with spacing
    setLayout(new GridLayout(playerHand.size(), 1, 0, 5));
    setPreferredSize(new Dimension(100, playerHand.size() * 100));
    setBackground(playerColor == Color.RED ? new java.awt.Color(239, 88, 88) :
            new java.awt.Color(58, 138, 253));


    // Add each card to the panel as a CardPanel
    for (CardinalCard card : playerHand) {
      CardPanel cardPanel = new CardPanel(card, playerColor);
      cardPanels.add(cardPanel);
      add(cardPanel); // adds the card panel to the hand panel (the actual jpanel itself)
    }
    addMouseListener(this);
  }

  /**
   * This paints the panel, and separates the cards with a line.
   *
   * @param g the <code>Graphics</code> object to protect the render of the panel.
   */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    if (!playerHand.isEmpty()) {
      int cardHeight = getHeight() / playerHand.size();

      for (int i = 0; i < playerHand.size(); i++) {
        // Y-position of the current card
        int y = i * cardHeight;

        // Draw the separation line between cards
        if (i < playerHand.size() - 1) {
          g2d.setColor(java.awt.Color.BLACK);
          g2d.setStroke(new BasicStroke(CARD_OUTLINE));
          g2d.drawLine(1, y + cardHeight, getWidth() - 1, y + cardHeight);
        }
      }
    } else {
      g2d.setColor(java.awt.Color.BLACK);
      g2d.setStroke(new BasicStroke(CARD_OUTLINE));
      g2d.drawLine(1, getHeight(), getWidth() - 1, getHeight());
    }
  }


  /**
   * Handles the mouse click event.
   *
   * @param e the MouseEvent triggered on click.
   */
  @Override
  public void mouseClicked(MouseEvent e) {
    int cardHeight = getHeight() / playerHand.size();
    int cardIndex = e.getY() / cardHeight;

    //    if (cardIndex == selectedCardIndex) {
    //      selectedCardIndex = -1; // Deselect if the same card is clicked again
    //    } else {
    //      selectedCardIndex = cardIndex;
    //    }

    // tell view to tell feature card has been selected
    parentView.clickToFeature("Hand " + cardIndex + " " + playerColor);
  }

  @Override
  public void highlightSelected(int selected) {
    if (selected < 0 || selected >= cardPanels.size()) {
      throw new IllegalArgumentException("not a valid selection.");
    }
    if (selectedCardIndex != -1) {
      cardPanels.get(selectedCardIndex).toggleHighlight(false);
    }
    boolean highlight = true;
    if (selectedCardIndex == selected) {
      selectedCardIndex = -1;
      highlight = false;
    } else {
      selectedCardIndex = selected;
    }
    cardPanels.get(selected).toggleHighlight(highlight);
    //    for (int i = 0; i < cardPanels.size(); i++) {
    //      cardPanels.get(i).setSelected(i == selectedCardIndex);
    //    }
  }

  @Override
  public void mousePressed(MouseEvent e) {
    return;
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    return;
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    return;
  }

  @Override
  public void mouseExited(MouseEvent e) {
    return;
  }


}
