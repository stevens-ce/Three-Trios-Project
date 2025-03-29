package threetrios.controller;

/**
 * interface with functions giving ability to recieve messages from a Messenger.
 */
public interface Receiver {
  /**
   * receives a message and chooses what to do with its contents, based on impl.
   * @param message from Messenger
   */
  void messageSent(String message);
}