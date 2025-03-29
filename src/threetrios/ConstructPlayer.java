package threetrios;

import threetrios.model.Color;
import threetrios.model.ReadOnlyThreeTriosModel;

import threetrios.player.ActionPlayer;
import threetrios.player.HumanActionPlayer;
import threetrios.player.MachineActionPlayer;

import threetrios.strategy.TTStrategy;
import threetrios.strategy.TTStrategyInfallibleStack;
import threetrios.strategy.MaxFlipStrategy;
import threetrios.strategy.FourCornerStrategy;

import threetrios.view.View;


/**
 * used to build ActionPlayers.
 */
public class ConstructPlayer {

  /**
   * creates a player based on given command terms.
   *
   * @param model needed to construct machine player
   * @param view needed to construct human player
   * @param color needed to construct player
   * @return some type of ActionPlayer
   */
  public ActionPlayer createPlayer(String playerType, ReadOnlyThreeTriosModel model,
                                   View view, Color color) {
    if (playerType.equals("human")) {
      return new HumanActionPlayer(view);
    } else {
      TTStrategy strat;
      switch (playerType) {
        case "strategy1":
          strat = new TTStrategyInfallibleStack(new MaxFlipStrategy());
          break;
        case "strategy2":
          strat = new TTStrategyInfallibleStack(new FourCornerStrategy());
          break;
        default:
          throw new IllegalArgumentException("invalid configuration terms for player.");
      }
      return new MachineActionPlayer(model, strat, color);
    }
  }

}