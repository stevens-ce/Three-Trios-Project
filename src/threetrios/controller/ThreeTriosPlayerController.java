package threetrios.controller;

import threetrios.player.ActionPlayer;

import threetrios.view.View;

import threetrios.model.Card;
import threetrios.model.ThreeTriosModelMessenger;
import threetrios.model.Color;


/**
 * Controller for a player in the ThreeTrios game.
 * Responsible for handling player input and updating the view.
 */
public class ThreeTriosPlayerController implements ThreeTriosController, Receiver {
  private final View view;
  private final ThreeTriosModelMessenger model;
  private final ActionPlayer player;

  private final Color color;
  private Color turn;

  /**
   * Constructs a new ThreeTriosPlayerController.
   *
   * @param player the player to control.
   * @param view   the view to update.
   * @param model  the model to update.
   * @param color  the color of the player.
   */
  public ThreeTriosPlayerController(ActionPlayer player, View view,
                                    ThreeTriosModelMessenger model, Color color) {
    model.setReceiver(this);

    Feature feature = new ViewPlayerFeature(this, color);
    view.addFeature(feature);
    player.addFeature(feature);

    this.color = color;
    this.view = view;
    this.player = player;
    this.model = model;
  }

  @Override
  public void messageSent(String message) {
    if (message.contains("it's your turn.")) {
      for (Color hue : Color.values()) {
        if (message.contains(hue.toString())) {
          this.turn = hue;
          break;
        }
      }
    } else if (message.contains("Make next move.")) {
      if (this.turn == this.color) {
        thisPlayerTurn();
      }
    } else if (message.contains("Update grid state.")) {
      updateView();
    } else if (message.contains("Game is Over!")) {
      view.displayMessage(message);
    } else {
      throw new IllegalArgumentException("controller doesn't recognize this command.");
    }
  }

  @Override
  public boolean validCardClick(int cardIdx, Color hand) {
    if (hand == this.color) {
      if (hand != turn) {
        player.playerMessenger("You can't select a card right now.");
      } else if (cardIdx >= 0 && cardIdx < model.getPlayerHand(color).size()) {
        player.playerMessenger(color + " highlight " + cardIdx);
        return true;
      } else {
        player.playerMessenger("I didn't think this was possible? NO.");
      }
    } else {
      player.playerMessenger("You can't select other player's card.");
    }
    return false;
  }

  @Override
  public void cardPlayed(int cardIdx, int row, int col) {
    try {
      Card card = model.getCardFromHand(cardIdx, color);
      model.playCard(card, row, col);
    } catch (IllegalArgumentException | IllegalStateException ex) {
      player.playerMessenger("Can't play there.");
      player.playerMessenger(color + " highlight " + cardIdx);
      player.makeMove();
    }
  }

  @Override
  public void updateView() {
    view.refresh();
  }

  private void thisPlayerTurn() {
    player.playerMessenger("It's your turn.");
    player.makeMove();
  }
}