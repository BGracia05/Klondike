package klondike;


import java.io.InputStreamReader;
import java.util.List;
import klondike.controller.KlondikeController;
import klondike.controller.KlondikeTextualController;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;
import klondike.model.hw04.KlondikeCreator;

/**
 * chooses game type and initializes the game.
 */
public class Klondike {
  private Klondike() {}

  /**
   * runs a Klondike game.
   *
   * @param args the type of game.
   */
  public static void main(String[] args) {
    if (args == null || args.length == 0) {
      throw new IllegalArgumentException("arg must be basic or whitehead");
    }

    KlondikeCreator.GameType type;
    String gameVariant = args[0].toUpperCase();
    if ("BASIC".equals(gameVariant)) {
      type = KlondikeCreator.GameType.BASIC;
    } else if ("WHITEHEAD".equals(gameVariant)) {
      type = KlondikeCreator.GameType.WHITEHEAD;
    } else {
      throw new IllegalArgumentException("Unknown gameVariant: " + args[0]);
    }

    int piles = 7;
    int draws = 3;

    if (args.length >= 2) {
      try {
        int p = Integer.parseInt(args[1]);
        if (p > 0) {
          piles = p;
        }
      } catch (NumberFormatException ignore) {
        // ignore
      }
    }
    if (args.length >= 3) {
      try {
        int d = Integer.parseInt(args[2]);
        if (d > 0) {
          draws = d;
        }
      } catch (NumberFormatException ignore) {
        // ignore
      }
    }

    KlondikeModel<Card> model = KlondikeCreator.create(type);
    List<Card> deck = model.createNewDeck();

    KlondikeController controller =
        new KlondikeTextualController(new InputStreamReader(System.in), System.out);

    try {
      controller.<Card>playGame(model, deck, true, piles, draws);
    } catch (IllegalArgumentException | IllegalStateException e) {
      //ignore
    }

  }
}

