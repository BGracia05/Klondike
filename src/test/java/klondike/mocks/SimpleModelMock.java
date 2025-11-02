package klondike.mocks;

import java.util.ArrayList;
import java.util.List;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;

/**
 * A basic mock model used to verify that controller commands.
 */
public class SimpleModelMock implements KlondikeModel<Card> {

  public String lastCall = "";

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw) {
  }

  @Override
  public void movePile(int src, int num, int dest) {
    lastCall = "movePile(" + src + "," + num + "," + dest + ")";
  }

  @Override
  public void moveDraw(int dest) {
    lastCall = "moveDraw(" + dest + ")";
  }

  @Override
  public void moveToFoundation(int src, int f) {
    lastCall = "moveToFoundation(" + src + "," + f + ")";
  }

  @Override
  public void moveDrawToFoundation(int f) {
    lastCall = "moveDrawToFoundation(" + f + ")";
  }

  @Override
  public void discardDraw() {
    lastCall = "discardDraw()";
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
  public int getPileHeight(int pile) {
    return 0;
  }

  @Override
  public Card getCardAt(int pile, int card) {
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
