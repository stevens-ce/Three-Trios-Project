package threetrios.model;

import threetrios.controller.Receiver;

/**
 * interface with functions giving ability to take in Receivers and send out messages.
 */
public interface Messenger {
  /**
   * adds given Receiver to private list of subscribed receivers.
   * @param receiver Receiver
   */
  void setReceiver(Receiver receiver);

  /**
   * sends message to all subscribed receivers.
   * @param message String
   */
  void sendMessageAll(String message);

}