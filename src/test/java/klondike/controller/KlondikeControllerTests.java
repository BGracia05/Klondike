package klondike.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import klondike.mocks.AlwaysGameOverMock;
import klondike.mocks.FailedMoveMock;
import klondike.mocks.InvalidInputMock;
import klondike.mocks.SimpleModelMock;
import klondike.mocks.StartFailMock;
import klondike.mocks.WinImmediatelyMock;
import klondike.model.hw02.Card;
import org.junit.Test;



/**
 * Tests public behavior of the KlondikeTextualController using mock models.
 */
public class KlondikeControllerTests {

  /**
   * Tests controller rejects null Readable in constructor.
   */
  @Test(expected = IllegalArgumentException.class)
  public void ctorRejectsNullReadable() {
    new KlondikeTextualController(null, new StringBuilder());
  }

  /**
   * Tests controller rejects null Appendable in constructor.
   */
  @Test(expected = IllegalArgumentException.class)
  public void ctorRejectsNullAppendable() {
    new KlondikeTextualController(new StringReader(""), null);
  }

  /**
   * Tests playGame rejects null model.
   */
  @Test(expected = IllegalArgumentException.class)
  public void playGameRejectsNullModel() {
    KlondikeController c = new KlondikeTextualController(new StringReader(""), new StringBuilder());
    c.playGame(null, Collections.<Card>emptyList(), false, 1, 1);
  }

  /**
   * Tests initial board and quit sequence.
   */
  @Test
  public void printsStateThenQuit() {
    StringReader in = new StringReader("q");
    StringBuilder out = new StringBuilder();
    SimpleModelMock m = new SimpleModelMock();

    KlondikeController c = new KlondikeTextualController(in, out);
    c.playGame(m, Collections.<Card>emptyList(), false, 3, 3);

    String s = out.toString();
    assertTrue(s.startsWith("Draw:"));
    assertTrue(s.contains("Foundation:"));
    assertTrue(s.contains("Score: 0\n"));
    assertTrue(s.contains("Game quit!\n"));
    assertTrue(s.contains("State of game when quit:\n"));
    assertTrue(s.trim().endsWith("Score: 0"));
  }

  /**
   * Tests mpp parses and converts 1-based to 0-based indices.
   */
  @Test
  public void mppThreeNumbersConverted() {
    SimpleModelMock m = new SimpleModelMock();
    StringReader in = new StringReader("mpp 3 2 7 q");
    StringBuilder out = new StringBuilder();

    new KlondikeTextualController(in, out)
        .playGame(m, new ArrayList<Card>(), false, 7, 3);

    assertEquals("movePile(2,2,6)", m.lastCall);
  }

  /**
   * Tests md parses one number and converts to 0-based.
   */
  @Test
  public void mdOneNumberConverted() {
    SimpleModelMock m = new SimpleModelMock();
    StringReader in = new StringReader("md 5 q");
    StringBuilder out = new StringBuilder();

    new KlondikeTextualController(in, out)
        .playGame(m, new ArrayList<Card>(), false, 7, 3);

    assertEquals("moveDraw(4)", m.lastCall);
  }

  /**
   * Tests mpf parses two numbers and converts to 0-based.
   */
  @Test
  public void mpfTwoNumbersConverted() {
    SimpleModelMock m = new SimpleModelMock();
    StringReader in = new StringReader("mpf 6 2 q");
    StringBuilder out = new StringBuilder();

    new KlondikeTextualController(in, out)
        .playGame(m, new ArrayList<Card>(), false, 7, 3);

    assertEquals("moveToFoundation(5,1)", m.lastCall);
  }

  /**
   * Tests mdf parses one number and converts to 0-based.
   */
  @Test
  public void mdfOneNumberConverted() {
    SimpleModelMock m = new SimpleModelMock();
    StringReader in = new StringReader("mdf 2 q");
    StringBuilder out = new StringBuilder();

    new KlondikeTextualController(in, out)
        .playGame(m, new ArrayList<Card>(), false, 7, 3);

    assertEquals("moveDrawToFoundation(1)", m.lastCall);
  }

  /**
   * Tests dd maps to discardDraw.
   */
  @Test
  public void ddCallsDiscard() {
    SimpleModelMock m = new SimpleModelMock();
    StringReader in = new StringReader("dd q");
    StringBuilder out = new StringBuilder();

    new KlondikeTextualController(in, out)
        .playGame(m, new ArrayList<Card>(), false, 7, 3);

    assertEquals("discardDraw()", m.lastCall);
  }

  /**
   * Tests bad tokens are skipped until numbers arrive.
   */
  @Test
  public void badTokensAreRetriedUntilNumbers() {
    SimpleModelMock m = new SimpleModelMock();
    StringReader in = new StringReader("mpp X Y 4 1 2 q");
    StringBuilder out = new StringBuilder();

    new KlondikeTextualController(in, out)
        .playGame(m, new ArrayList<Card>(), false, 7, 3);

    assertEquals("movePile(3,1,1)", m.lastCall);
  }

  /**
   * Tests quitting mid-command stops the game.
   */
  @Test
  public void quitMidCommandPrintsQuitBlockAndStops() {
    SimpleModelMock m = new SimpleModelMock();
    StringReader in = new StringReader("mpp 3 q 2 7");
    StringBuilder out = new StringBuilder();

    new KlondikeTextualController(in, out)
        .playGame(m, new ArrayList<Card>(), false, 7, 3);

    String s = out.toString();
    assertTrue(s.contains("Game quit!\n"));
    assertTrue(s.contains("State of game when quit:\n"));
    assertNotEquals("movePile(2,2,6)", m.lastCall);
  }

  /**
   * Tests unknown command prints generic invalid message.
   */
  @Test
  public void unknownCommandPrintsGenericInvalidMove() {
    StringReader in = new StringReader("xyz q");
    StringBuilder out = new StringBuilder();

    new KlondikeTextualController(in, out)
        .playGame(new SimpleModelMock(), new ArrayList<Card>(), false, 7, 3);

    assertTrue(out.toString().contains("Invalid move. Play again.\n"));
  }

  /**
   * Tests model IllegalArgumentException becomes single error line.
   */
  @Test
  public void modelIllegalArgumentPrintedAsErrorLine() {
    FailedMoveMock m = new FailedMoveMock();
    StringReader in = new StringReader("md 2 q");
    StringBuilder out = new StringBuilder();

    new KlondikeTextualController(in, out)
        .playGame(m, new ArrayList<Card>(), false, 7, 3);

    String s = out.toString();
    assertTrue(s.contains("Invalid move. Play again. bad move\n"));
    assertTrue(s.indexOf("Invalid move. Play again. bad move")
        < s.lastIndexOf("Score:"));
  }

  /**
   * Tests invalid input mock throws on movePile.
   */
  @Test
  public void invalidInputMockThrowsFromMovePile() {
    InvalidInputMock m = new InvalidInputMock();
    StringReader in = new StringReader("mpp 1 1 1 q");
    StringBuilder out = new StringBuilder();

    new KlondikeTextualController(in, out)
        .playGame(m, new ArrayList<Card>(), false, 7, 3);

    assertTrue(out.toString().contains("Invalid move. Play again. invalid\n"));
  }

  /**
   * Tests always-game-over ends with Game over message.
   */
  @Test
  public void alwaysGameOverPrintsGameOverMessage() {
    AlwaysGameOverMock m = new AlwaysGameOverMock();
    StringReader in = new StringReader("");
    StringBuilder out = new StringBuilder();

    List<Card> deck = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      deck.add(null);
    }

    new KlondikeTextualController(in, out)
        .playGame(m, deck, false, 7, 3);

    String s = out.toString();
    assertTrue(s.contains("Game over. Score: 0\n"));
  }

  /**
   * Tests win message prints when score equals deck size.
   */
  @Test
  public void winImmediatelyPrintsYouWin() {
    WinImmediatelyMock m = new WinImmediatelyMock();
    StringReader in = new StringReader("");
    StringBuilder out = new StringBuilder();

    List<Card> deck = new ArrayList<>();
    for (int i = 0; i < 52; i++) {
      deck.add(null);
    }

    new KlondikeTextualController(in, out)
        .playGame(m, deck, false, 7, 3);

    assertTrue(out.toString().trim().endsWith("You win!"));
  }

  /**
   * Tests appendable failure maps to IllegalStateException.
   */
  @Test(expected = IllegalStateException.class)
  public void appendableFailureRaisesIllegalState() {
    Reader in = new StringReader("q");
    Appendable exploding = new Appendable() {
      @Override
      public Appendable append(CharSequence csq) throws IOException {
        throw new IOException("nope");
      }

      @Override
      public Appendable append(CharSequence csq, int start, int end) throws IOException {
        throw new IOException("nope");
      }

      @Override
      public Appendable append(char c) throws IOException {
        throw new IOException("nope");
      }
    };
    new KlondikeTextualController(in, exploding)
        .playGame(new SimpleModelMock(), new ArrayList<Card>(), false, 7, 3);
  }

  /**
   * Tests startGame failure maps to IllegalStateException.
   */
  @Test(expected = IllegalStateException.class)
  public void startGameFailureIsIllegalState() {
    StringReader in = new StringReader("q");
    StringBuilder out = new StringBuilder();
    new KlondikeTextualController(in, out)
        .playGame(new StartFailMock(), new ArrayList<Card>(), false, 7, 3);
  }
}
