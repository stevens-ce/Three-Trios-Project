package threetrios.controller;

import threetrios.model.Color;

/**
 * interface detailing all the potential events the controller handles.
 */
public interface ThreeTriosController {
  /**
   * checks whether given card exists in player hand, and whether given hand is the player's hand.
   *
   * @param cardIdx int
   * @param hand    Color
   * @return true if valid cardIdx for player's hand, false otherwise
   */
  boolean validCardClick(int cardIdx, Color hand);

  /**
   * trys to call ThreeTriosModel.playCard at given idx to given row and col.
   * If fails, sends message "Can't play there." to view.
   *
   * @param cardIdx int
   * @param row     int
   * @param col     int
   */
  void cardPlayed(int cardIdx, int row, int col);

  /**
   * refreshes the view, sends message "It's your turn." to view if applicable.
   */
  void updateView();
}