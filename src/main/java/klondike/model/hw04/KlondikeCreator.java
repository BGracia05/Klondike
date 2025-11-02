package klondike.model.hw04;

import klondike.model.hw02.BasicKlondike;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;


/**
 * Creates a Klondike game, either basic or whitehead.
 */

public class KlondikeCreator {
  /**
   * representation of either BasicKlondike or WhiteheadKlondike.
   */
  public enum GameType {
    BASIC, WHITEHEAD
  }

  private KlondikeCreator() {

  }

  /**
   * Picks what game is going to be initialized.
   *
   * @param type type of game that is chosen.
   * @return a new Klondike game of either Basic or Whitehead.
   */
  public static KlondikeModel<Card> create(GameType type) {
    if (type == null) {
      throw new IllegalArgumentException("Type is null");
    }
    return switch (type) {
      case BASIC -> new BasicKlondike();
      case WHITEHEAD -> new WhiteheadKlondike();
    };
  }

}
