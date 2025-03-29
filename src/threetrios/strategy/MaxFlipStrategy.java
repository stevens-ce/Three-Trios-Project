package threetrios.strategy;

import threetrios.model.ReadOnlyThreeTriosModel;
import threetrios.model.Color;
import threetrios.model.Card;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * strategy where the card in the position that allows it to flip the most cards is the winner.
 * card and move are necessarily decided together.
 * if all possible moves flip zero cards, there are no valid moves; return null.
 * other ties will be resolved internally.
 */
public class MaxFlipStrategy extends TieBreakerStrategy {

  @Override
  public CardMove chooseMove(ReadOnlyThreeTriosModel model, Color player) {
    if (model.isGameOver()) {
      throw new IllegalStateException("no moves when game is over.");
    }

    // The runtime on this is nasty
    Map<Move, List<Card>> bestMoves = getBestMove(model, player);

    if (bestMoves == null) {
      return null;
    } else if (bestMoves.size() == 1) {
      for (Move move : bestMoves.keySet()) {
        List<Card> card = bestMoves.get(move);
        return new CardMove(card.get(0), move);
      }
    }

    return super.tieBreakerMove(model, player, bestMoves);
  }

  private static Map<Move, List<Card>> getBestMove(ReadOnlyThreeTriosModel model, Color player) {
    List<Card> hand = model.getPlayerHand(player);
    int mostFlipped = 0;
    Map<Move, List<Card>> ties = new HashMap<>();

    for (int row = 0; row < model.getGridLength(); row++) { // all possible positions
      for (int col = 0; col < model.getGridWidth(); col++) { // on grid
        if (model.isMoveLegal(row, col)) { // all legal positions
          Move currMove = new Move(row, col);
          for (Card card : hand) { // all cards that could be played

            int flipped = model.numCardsFlipped(card, player, row, col);

            if (flipped >= mostFlipped) { // if there's a new flipping record or tie
              if (flipped > mostFlipped) { // new record
                ties.clear(); // clear ties; no longer relevant data
                mostFlipped = flipped; // updated most flipped
              }
              if (ties.containsKey(currMove)) { // if this move is alr in map, add card to its list
                ties.get(currMove).add(card);
              } else { // else, add currMove mapped to a list containing this card
                List<Card> cardList = new ArrayList<>();
                cardList.add(card);
                ties.put(currMove, cardList);
              }

            }

          }
        }
      }
    }

    return ties;
  }


}
