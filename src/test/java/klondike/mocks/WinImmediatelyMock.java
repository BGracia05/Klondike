package klondike.mocks;

import java.util.ArrayList;
import java.util.List;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;

/**
 * Mock model that simulates an immediate win.
 */
public class WinImmediatelyMock implements KlondikeModel<Card> {
  @Override
  public boolean isGameOver() {
    return true;
  }

  @Override
  public int getScore() {
    return 52;
  }


  @Override
  public void startGame(List<Card> d, boolean s, int p, int n) {
  }

  @Override
  public void movePile(int s, int n, int d) {
  }

  @Override
  public void moveDraw(int d) {
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
