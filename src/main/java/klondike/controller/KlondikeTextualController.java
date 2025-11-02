package klondike.controller;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;
import klondike.view.KlondikeTextualView;

/**
 * Text-based controller for Klondike.
 */
public class KlondikeTextualController implements KlondikeController {
  private final Readable read;
  private final Appendable app;

  /**
   * constructs the textual controller.
   *
   * @param read the input.
   * @param app  the output.
   */
  public KlondikeTextualController(Readable read, Appendable app) {
    if (read == null || app == null) {
      throw new IllegalArgumentException("null");
    }
    this.read = read;
    this.app = app;
  }

  @Override
  public <C extends Card> void playGame(KlondikeModel<C> model,
                                        List<C> deck,
                                        boolean shuffle,
                                        int numPiles,
                                        int numDraw) {
    if (model == null) {
      throw new IllegalArgumentException("null");
    }
    final Scanner sc = new Scanner(read);
    final int totalCards = (deck == null) ? 0 : deck.size();

    try {
      model.startGame(deck, shuffle, numPiles, numDraw);
    } catch (RuntimeException e) {
      throw new IllegalStateException("Could not start game", e);
    }

    gameState(model);

    while (true) {
      if (!sc.hasNext()) {
        throw new IllegalStateException("Input ended");
      }

      String cmd = sc.next();
      if (isQuit(cmd)) {
        quit(model);
        return;
      }

      try {
        switch (cmd) {
          case "mpp":
            {
            Integer a = readOrQuit(sc, model); if (a == null) {
              return;
            }
            Integer b = readOrQuit(sc, model); if (b == null) {
              return;
            }
            Integer c = readOrQuit(sc, model); if (c == null) {
              return;
            }
            model.movePile(a - 1, b, c - 1);   // success → render
            gameState(model);
            break;
            }
          case "md":
            {
            Integer d = readOrQuit(sc, model); if (d == null) {
              return;
            }
            model.moveDraw(d - 1);
            gameState(model);
            break;
            }
          case "mpf":
            {
            Integer s = readOrQuit(sc, model); if (s == null) {
              return;
            }
            Integer f = readOrQuit(sc, model); if (f == null) {
              return;
            }
            model.moveToFoundation(s - 1, f - 1);
            gameState(model);
            break;
            }
          case "mdf":
            {
            Integer f = readOrQuit(sc, model); if (f == null) {
              return;
            }
            model.moveDrawToFoundation(f - 1);
            gameState(model);
            break;
            }
          case "dd":
            {
            model.discardDraw();
            gameState(model);
            break;
            }
          default:
            writeln("Invalid move. Play again.");
        }
        if (model.isGameOver()) {
          writeln(model.getScore() == totalCards ? "You win!"
              : "Game over. Score: " + model.getScore());
          return;
        }
      } catch (RuntimeException ex) {
        writeln("Invalid move. Play again. " + ex.getMessage());
      }
    }
  }

  private void gameState(KlondikeModel<?> model) {
    writeln(new KlondikeTextualView(model, new StringBuilder()).toString());
    writeln("Score: " + model.getScore());
  }

  private void quit(KlondikeModel<?> model) {
    writeln("Game quit!");
    writeln("State of game when quit:");
    writeln(new KlondikeTextualView(model, new StringBuilder()).toString());
    writeln("Score: " + model.getScore());
  }

  private Integer readOrQuit(Scanner sc, KlondikeModel<?> model) {
    while (true) {
      if (!sc.hasNext()) {
        throw new IllegalStateException("Input ended");
      }
      String tok = sc.next();
      if (isQuit(tok)) {
        quit(model);
        return null;
      }
      try {
        return Integer.parseInt(tok);
      } catch (NumberFormatException ignore) {
        // ignore and continue scanning.
      }
    }
  }


  private static boolean isQuit(String s) {
    return s.equalsIgnoreCase("q");
  }

  private void writeln(String s) {
    try {
      app.append(s).append('\n');
    } catch (IOException e) {
      throw new IllegalStateException("Transmission failed", e);
    }
  }
}

