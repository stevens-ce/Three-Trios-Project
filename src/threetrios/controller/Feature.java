package threetrios.controller;

import threetrios.model.Color;

/**
 * outlines all commands a player needs i.e. player actions;
 * allows for view -> controller communication.
 * feature stores color.
 */

public interface Feature {

  /**
   * if card valid for feature's player (not given player), saves card.
   * if exact card already saved, unsaves it.
   */
  void cardSelected(int cardIdx, Color player);

  /**
   * if a card is current saved, attempts to play card to given (row, col) in model.
   * if no card or placement fails, do nothing.
   *
   * @param row pos
   * @param col pos
   */
  void cellSelected(int row, int col);


  /**
   * Gets the color from the associated player.
   *
   * @return color of player.
   */
  Color getAssociatedColor();
}