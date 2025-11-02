package klondike.mocks;

import java.util.ArrayList;
import java.util.List;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;

/**
 * Mock model whose startGame always fails.
 * Used to verify controller throws IllegalStateException when game cannot start.
 */
public class StartFailMock implements KlondikeModel<Card> {

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw) {
    throw new IllegalArgumentException("boom");
  }

  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public int getScore() {
    return 0;
  }

  @Override
  public int getNumPiles() {
    return 0;
  }

  @Override
  public int getNumRows() {
    return 0;
  }

  @Override
  public int getNumDraw() {
    return 0;
  }

  @Override
  public int getNumFoundations() {
    return 1;
  }

  @Override
  public List<Card> getDrawCards() {
    return new ArrayList<>();
  }

  @Override
  public int getPileHeight(int pileNum) {
    return 0;
  }

  @Override
  public Card getCardAt(int pileNum, int card) {
    return null;
  }

  @Override
  public Card getCardAt(int foundationPile) {
    return null;
  }

  @Override
  public boolean isCardVisible(int pileNum, int card) {
    return false;
  }

  @Override
  public List<Card> createNewDeck() {
    return new ArrayList<>();
  }

  @Override
  public void movePile(int srcPile, int numCards, int destPile) {
  }

  @Override
  public void moveDraw(int destPile) {
  }

  @Override
  public void moveToFoundation(int srcPile, int foundationPile) {
  }

  @Override
  public void moveDrawToFoundation(int foundationPile) {
  }

  @Override
  public void discardDraw() {
  }
}
