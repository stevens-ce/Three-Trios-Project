package threetrios.controller;

import java.util.ArrayList;
import java.util.List;

import java.io.IOException;

import threetrios.model.Card;
import threetrios.model.CardinalValues;
import threetrios.model.CardinalCard;
import threetrios.model.Color;
import threetrios.model.GridCell;
import threetrios.model.ThreeTriosModelMessenger;

/**
 * Simplified mock implementation of ThreeTriosModelMessenger for testing purposes.
 */
public class MockThreeTriosModelMessenger implements ThreeTriosModelMessenger<Card> {
  private final Appendable transcript;
  private List<Receiver> receivers;
  private ArrayList<CardinalCard> list;


  /**
   * Constructor for MockThreeTriosModelMessenger.
   *
   * @param transcript the transcript to append to
   */
  public MockThreeTriosModelMessenger(Appendable transcript) {
    this.transcript = transcript;
    this.receivers = new ArrayList<>();
    list = new ArrayList<>();
    CardinalValues values = new CardinalValues(1, 1, 1, 1);
    list.add(new CardinalCard(values, "token card"));
  }

  @Override
  public void setReceiver(Receiver receiver) {
    addToTranscript("set receiver");
    receivers.add(receiver);
  }

  @Override
  public List<CardinalCard> getPlayerHand(Color player) {
    return list;
  }


  @Override
  public CardinalCard getCardFromHand(int cardIdx, Color player) {
    addToTranscript("getCardFromHand called");
    return list.get(0);
  }

  /**
   * sends a message.
   * @param message String
   */
  public void sendMessageAll(String message) {
    addToTranscript("sending message: " + message);
    for (Receiver receiver : receivers) {
      receiver.messageSent(message);
    }
  }

  @Override
  public void playCard(Card card, int row, int col) {
    addToTranscript(String.format("play card %s %d %d", card.getIdentifier(), row, col));
    if (row == 5 && col == 5) {
      throw new IllegalArgumentException("testing failed play");
    }
  }

  @Override
  public void setup(String[] gridFile, String[] deckFile) {
    return;
  }

  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public Color winner() {
    return null;
  }

  @Override
  public GridCell[][] getGrid() {
    return null;
  }

  @Override
  public Color getCurrentPlayer() {
    return null;
  }

  @Override
  public List<Color> getPlayers() {
    return null;
  }

  @Override
  public int getGridLength() {
    return 0;
  }

  @Override
  public int getGridWidth() {
    return 0;
  }

  @Override
  public GridCell getCell(int row, int col) {
    return null;
  }

  @Override
  public Color getOwnerOfCell(GridCell cell) {
    return null;
  }

  @Override
  public boolean isMoveLegal(int row, int col) {
    return false;
  }

  @Override
  public int numCardsFlipped(Card card, Color player, int row, int col) {
    return 0;
  }

  @Override
  public int currentScore(Color player) {
    return 0;
  }

  private void addToTranscript(String record) {
    try {
      transcript.append(record);
      transcript.append("\n");
    } catch (IOException ex) {
      // who cares
    }
  }
}



