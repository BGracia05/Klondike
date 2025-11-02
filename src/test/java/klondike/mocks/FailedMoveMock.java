package klondike.mocks;

import java.util.ArrayList;
import java.util.List;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;

/**
 * A mock model that always throws an error on a move.
 */
public class FailedMoveMock implements KlondikeModel<Card> {

  @Override
  public void moveDraw(int destPile) {
    throw new IllegalArgumentException("bad move");
  }

  @Override
  public void startGame(List<Card> d, boolean s, int p, int n) {
  }

  @Override
  public void movePile(int s, int n, int d) {
  }

  @Override
  public void moveToFoundation(int s, int f) {
  }

  @Override
  public void moveDrawToFoundation(int f) {
  }

  @Override
  public void discardDraw() {
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
  public int getPileHeight(int p) {
    return 0;
  }

  @Override
  public Card getCardAt(int p, int c) {
    return null;
  }

  @Override
  public Card getCardAt(int f) {
    return null;
  }

  @Override
  public boolean isCardVisible(int p, int c) {
    return false;
  }

  @Override
  public List<Card> createNewDeck() {
    return new ArrayList<>();
  }
}
