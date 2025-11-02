package klondike.controller;

import java.util.List;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;

/**
 * Initializes controller.
 */
public interface KlondikeController {
  /**
   * Starts and plays a game of Klondike Solitaire using the given model and deck.
   * Game ends continues until game is over or user quits.
   *
   * @param model    the game model to use.
   * @param deck     the deck of cards to start the game
   * @param shuffle  whether to shuffle the deck before starting
   * @param numPiles number of cascade piles to use
   * @param numDraw  number of draw cards visible at a time
   * @param <C>      the type of card used by the model
   * @throws IllegalArgumentException if the model is null
   * @throws IllegalStateException    if input or output fails, or the game cannot start
   */
  <C extends Card> void playGame(KlondikeModel<C> model,
                                 List<C> deck,
                                 boolean shuffle,
                                 int numPiles,
                                 int numDraw);
}
