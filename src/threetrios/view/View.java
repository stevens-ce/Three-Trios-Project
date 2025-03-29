package threetrios.view;


import threetrios.controller.Feature;

/**
 * This defines methods needed for setting up and displaying a GUI for the ThreeTrios game.
 */
public interface View {

  /**
   * This method refreshes the view of the game.
   */
  void refresh();


  /**
   * This method adds a feature to the view.
   *
   * @param feature to be added.
   */
  void addFeature(Feature feature);


  /**
   * Displays a message.
   *
   * @param s message to be displayed.
   */
  void displayMessage(String s);

  /**
   * based on command, calls different Feature method.
   *
   * @param command String.
   */
  void clickToFeature(String command);

  /**
   * highlights indicated card in the hand of given player.
   *
   * @param player String representing the color of a player
   * @param idx    of card.
   */
  void highlightCard(String player, int idx);
}
