package klondike.view;

import java.io.IOException;

/**
 * A marker interface for all text-based views, to be used in the Klondike game.
 */
public interface TextualView {
  /**
   * Output current state of game to the appendable.
   *
   * @throws IOException if the output fails.
   */
  void render() throws IOException;
}