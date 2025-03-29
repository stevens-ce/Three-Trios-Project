package threetrios.strategy;

import threetrios.model.Color;
import threetrios.model.ReadOnlyThreeTriosModel;
import threetrios.model.Direction;
import threetrios.model.Card;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Strategy that selects the most defensible of four corners of grid, then card least likely to be
 * flipped in that position.
 * "Defensible" means the one with the least amount of empty adjacent cells.
 * The card least likely to be flipped is determined to be the one with the highest average
 * attackvalues FOR THE VALUES FACING EMPTY ADJ CELLS.
 * (NOT corners dictated by shape of grid's card cells, but corners by dimension)
 */
public class FourCornerStrategy extends TieBreakerStrategy {

  @Override
  public CardMove chooseMove(ReadOnlyThreeTriosModel model, Color player) {
    if (model.isGameOver()) {
      throw new IllegalStateException("no moves when game is over.");
    }
    Move[] moves = new Move[4];
    moves[0] = new Move(0, 0);
    moves[1] = new Move(0, model.getGridWidth() - 1);
    moves[2] = new Move(model.getGridLength() - 1, 0);
    moves[3] = new Move(model.getGridLength() - 1, model.getGridWidth() - 1);

    int numOpen = 2;
    Move defensibleMove = null;
    for (Move move : moves) {
      if (model.isMoveLegal(move.row, move.col)) {
        int rowFactor = move.row == 0 ? 1 : -1;
        int colFactor = move.col == 0 ? 1 : -1;
        int openSides = getNumOpenSides(model, move, rowFactor, colFactor);

        defensibleMove = numOpen > openSides ? move :
                (defensibleMove == null ? move : defensibleMove);
        numOpen = Math.min(numOpen, openSides);
      }
    }

    return getCardMove(model, player, defensibleMove, numOpen);
  }

  private CardMove getCardMove(ReadOnlyThreeTriosModel model, Color player,
                               Move move, int numOpen) {
    if (move == null) {
      return null;
    } else {
      int rowFactor = move.row == 0 ? 1 : -1;
      int colFactor = move.col == 0 ? 1 : -1;
      List<Card> cards = bestCardForMove(model, player, move, rowFactor, colFactor, numOpen);
      if (cards.size() == 1) {
        return new CardMove(cards.get(0), move);
      } else {
        Map<Move, List<Card>> cardsForDefMove = new HashMap<>();
        cardsForDefMove.put(move, cards);
        return super.tieBreakerMove(model, player, cardsForDefMove);
      }
    }
  }

  private List<Card> bestCardForMove(ReadOnlyThreeTriosModel model, Color player, Move move,
                                     int rowFactor, int colFactor, int numOpen) {
    List<Card> hand = model.getPlayerHand(player);
    if (numOpen == 0) {
      return hand;
    } else {
      Direction[] relevantDir = new Direction[numOpen];
      if (model.isMoveLegal(move.row, move.col + colFactor)) {
        relevantDir[0] = colFactor < 0 ? Direction.SOUTH : Direction.NORTH;
      }
      if (model.isMoveLegal(move.row + rowFactor, move.col)) {
        relevantDir[numOpen - 1] = colFactor < 0 ? Direction.WEST : Direction.EAST;
      }

      return getCardOptions(hand, relevantDir);
    }
  }

  private static List<Card> getCardOptions(List<Card> hand, Direction[] relevantDir) {
    int atkVal = 0;
    List<Card> options = new ArrayList<>();

    for (Card card : hand) {
      int sum = 0;
      for (Direction dir : relevantDir) {
        sum += card.getCardinalValue(dir).attackValue.getValue();
      }
      int avg = (int) (sum / relevantDir.length);
      if (atkVal == avg) {
        options.add(card);
      } else if (atkVal < avg) {
        options.clear();
        options.add(card);
        atkVal = avg;
      }
    }

    return options;
  }

  private int getNumOpenSides(ReadOnlyThreeTriosModel model, Move move,
                              int rowFactor, int colFactor) {
    int exposedSides = 0;
    if (model.isMoveLegal(move.row + rowFactor, move.col)) {
      exposedSides++;
    }
    if (model.isMoveLegal(move.row, move.col + colFactor)) {
      exposedSides++;
    }
    return exposedSides;
  }


}
