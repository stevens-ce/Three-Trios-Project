package threetrios.model;

/**
 * This interface allows the model to implement the Messenger interface and the ThreeTriosModel
 * interface.
 */
public interface ThreeTriosModelMessenger<C extends Card> extends Messenger, ThreeTriosModel<C> {

  // empty

}
