package threetrios.player;

import threetrios.controller.Feature;

/**
 * Representation of a player that can make a move.
 */
public interface ActionPlayer {
  /**
   * adds a Feature that contains all commands ActionPlayer needs.
   *
   * @param feature Feature
   */
  void addFeature(Feature feature);

  /**
   * In working ActionPlayer, will send move information to Feature (position, card).
   * If Feature is not instantiated, quit game because it's completely broken.
   *
   * @throws IllegalStateException if feature has not been intialized
   */
  void makeMove();

  /**
   * If non-auto player, sends message to view. Else, does nothing.
   *
   * @param message for view.
   */
  void playerMessenger(String message);
}