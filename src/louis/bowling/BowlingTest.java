package louis.bowling;


import org.junit.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.rules.ExpectedException;
import stev.bowling.BowlingException;
import stev.bowling.Game;
import stev.bowling.LastFrame;
import stev.bowling.NormalFrame;

/**
 * Created by Louis Dumont.
 * <p>
 * Class which purpose is to test the well behaviour
 * of the library bowling-score.jar
 */
public class BowlingTest {
    private Game game;
    private Game demo;
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void setup() {
        setupGame();
        setupDemoGame();
    }

    private void setupDemoGame() {
        demo = new Game();
        demo.addFrame(new NormalFrame(0).setPinsDown(1, 3).setPinsDown(2, 6));
        demo.addFrame(new NormalFrame(1).setPinsDown(1, 10));
        demo.addFrame(new NormalFrame(2).setPinsDown(1, 5).setPinsDown(2, 0));
        demo.addFrame(new NormalFrame(3).setPinsDown(1, 1).setPinsDown(2, 9));
        demo.addFrame(new NormalFrame(4).setPinsDown(1, 10));
        demo.addFrame(new NormalFrame(5).setPinsDown(1, 0).setPinsDown(2, 0));
        demo.addFrame(new NormalFrame(6).setPinsDown(1, 0).setPinsDown(2, 6));
        demo.addFrame(new NormalFrame(7).setPinsDown(1, 10));
        demo.addFrame(new NormalFrame(8).setPinsDown(1, 2).setPinsDown(2, 8));
        demo.addFrame(new LastFrame(9).setPinsDown(1, 1).setPinsDown(2, 9).setPinsDown(3, 3));
    }

    public void resetGameFrames() {
        for (int i = 1; i <= 9; i++) {
            game.getFrame(i).reset();
        }
    }

    /**
     * Ce test a pour but de vérifier que l'on ne
     * peut pas lancer plus de 2 fois sur une Normal Frame
     */
    @Test
    public void checkThatThePlayerCantRollMoreThan2TimesOnNormalFrame() {
        exceptionRule.expect(BowlingException.class);
        exceptionRule.expectMessage("There is no such roll 3");
        game.getFrame(1).setPinsDown(1, 2).setPinsDown(2, 2).setPinsDown(3, 2);
    }

    /**
     * Ce test a pour but de vérifier que l'on ne peut pas
     * lancer plus de deux fois sur une LastFrame quand pas d'abat ou de réserve
     */
    @Test
    public void checkThatThePlayerCantRollMoreThan2TimesOnLastFrameWhenNoStrikeOrSpare() {
        exceptionRule.expect(BowlingException.class);
        exceptionRule.expectMessage("No third roll is allowed");
        game.getFrame(9).setPinsDown(1, 2).setPinsDown(2, 2).setPinsDown(3, 2);
    }

    /**
     * Ce test a pour but de vérifier que
     * l'on ne peut pas relancer après un strike sur une NormalFrame
     */
    @Test
    public void checkStrikeRollDoesntAllowAnotherRoll() {
        exceptionRule.expectMessage("The total score exceeds 10");
        game.getFrame(2).setPinsDown(1, 10).setPinsDown(2, 1);
    }

    /**
     * Ce test a pour but de vérifier le contenu toString d'une NormalFrame après Strike
     */
    @Test
    public void checkStrikeCloseTheFrame() {
        Assert.assertTrue(game.getFrame(3).setPinsDown(1, 10).toString().contains("X"));
    }

    /**
     * Ce test a pour but de vérifier le contenu d'une NormalFrame après Spare
     */
    @Test
    public void checkSpare() {
        Assert.assertTrue(game.getFrame(4).setPinsDown(1, 5).setPinsDown(2, 5).toString().contains("5/"));
    }

    /**
     * Ce test a pour but de vérifier que l'on ne peut pas entrer de lancer 0
     */
    @Test
    public void checkSetPinsDownRollNumberWithZero() {
        exceptionRule.expectMessage("There is no such roll 0");
        game.getFrame(5).setPinsDown(0, 2);
    }

    /**
     * Ce test a pour but de vérifier que l'on put lancer 3 fois sur une LastFrame après spare
     */
    @Test
    public void checkFrameRollsWithSpare() {
        LastFrame frame = (LastFrame) game.getFrame(9).reset();
        frame.setPinsDown(1, 5).setPinsDown(2, 5).setPinsDown(3, 5);
    }

    /**
     * Ce test a pour but de vérifier le score total de la game de démo disponible sur Moodle
     */
    @Test
    public void checkScoreWithDemoGame() {
        Assert.assertEquals(demo.getCumulativeScore(10), 109);
    }

    /**
     * Erreur: Ce test a pour but de vérifier que l'on peut lancer 3 fois sur une LastFrame après un strike
     */
    @Test
    public void checkLastFrameRollsWithStrike() {
        LastFrame frame = (LastFrame) game.getFrame(9).reset();
        frame.setPinsDown(1, 10).setPinsDown(2, 2).setPinsDown(3, 5);
    }

    /**
     * Erreur: Ce test a pour but de vérifier le contenu toString de la game de démo disponible sur Moodle
     */
    @Test
    public void checkToStringMethodWithRegularGame() {
        Assert.assertEquals("|#1  |#2  |#3  |#4  |#5  |#6  |#7  |#8  |#9  |#10 |\n" +
                "+----+----+----+----+----+----+----+----+----+----|\n" +
                "|  36|  X |  5-|  1/|  X |  --|  -6|  X |  2/| 1/3|\n" +
                "|9   |24  |29  |49  |59  |59  |65  |85  |96  |109 |", demo.toString());
    }

    /**
     * Ce test a pour but de vérifier que la méthod reset réinitialise le nombre de lancer
     */
    @Test
    public void checkResetMethodResetCountRolls() {
        resetGameFrames();
        game.getFrame(0).setPinsDown(1, 5).setPinsDown(2, 4);
        game.getFrame(0).reset();
        Assert.assertEquals(game.getFrame(0).countRolls(), 0);
    }

    /**
     * Ce test a pour but de vérifier que la méthode reset réinitialise le nombre de quille
     */
    @Test
    public void checkResetMethodResetCountPins() {
        resetGameFrames();
        game.getFrame(0).setPinsDown(1, 5).setPinsDown(2, 4);
        game.getFrame(0).reset();
        Assert.assertEquals(game.getFrame(0).countPinsDown(), 0);
    }

    /**
     * Ce test a pour but de vérifier la méthod getPindDown
     */
    @Test
    public void checkCountPinsDownMethodWithParameter() {
        resetGameFrames();
        game.getFrame(0).setPinsDown(1, 5).setPinsDown(2, 4);
        Assert.assertEquals(5, game.getFrame(0).getPinsDown(1));
    }

    /**
     * Ce test a pour but de vérifier la method getPinsDown après avoir utiliser la méthod reset
     */
    @Test
    public void checkCountPinsDownMethodWithParameterAfterReset() {
        resetGameFrames();
        game.getFrame(0).setPinsDown(1, 5).setPinsDown(2, 4);
        game.getFrame(0).reset();
        Assert.assertEquals(game.getFrame(0).getPinsDown(1), -1);
    }

    /**
     * Ce test a pour but de vérifier la longueur toString de chacun des NormalFrame
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8})
    public void checkToStringMethodWithCharacterCountForNormalFrame(int frameNumber) {
        setupGame();
        Assert.assertEquals(2, game.getFrame(frameNumber).toString().length());
    }

    /**
     * Ce test a pour but de vérifier la longeur de la méthod toString sur une LastFrame
     */
    @Test
    public void checkToStringMethodWithCharacterCountForLastFrame() {
        Assert.assertEquals(3, game.getFrame(9).toString().length());
    }

    /**
     * Ce test a pour but de vérifier que la méthod reset influe sur le score total
     */
    @Test
    public void checkThatResetInfluenceCumulativeScore() {
        demo.getFrame(4).reset();
        Assert.assertNotEquals(65, demo.getCumulativeScore(6));
    }

    /**
     * Erreur: Ce test a pour but de vérifier le nombre de lancer totaux lorsque l'on en fait qu'un seul sur une LastFrame
     */
    @Test
    public void checkCountRollsWhenOnlyOneIsDoneOnLastFrame() {
        game.getFrame(9).reset();
        game.getFrame(9).setPinsDown(1, 5);
        Assert.assertEquals(1, game.getFrame(9).countRolls());
    }

    /**
     * Erreur: Ce test a pour but de vérifier le nombre de quilles quand il n'y a pas tout les lancer de faits sur une NormalFrame
     */
    @Test
    public void checkCountPinsDownsWhenNotAllRollsDoneOnNormalFrame() {
        game.getFrame(1).reset();
        game.getFrame(1).setPinsDown(1, 5);
        Assert.assertEquals(5, game.getFrame(1).countPinsDown());
    }

    /**
     * Erreur: Ce test a pour but de vérifier le nombre de quilles quand il n'y a pas tout les lancer de faits sur une LastFrames
     */
    @Test
    public void checkCountPinsDownsWhenNotAllRollsDoneOnLastlFrame() {
        game.getFrame(9).reset();
        game.getFrame(9).setPinsDown(1, 5);
        Assert.assertEquals(5, game.getFrame(9).countPinsDown());
    }

    /**
     * Erreur: Ce test a pour but de vérifier qu'un lancer 2 sans avoir fait le 1er,
     * ne compte pas les points si l'exception est thrown
     */
    @Test
    public void checkThatWhenFirstRollDidntHappenedAndSecondRollIsRegisteredTheScoreDoesntIncrement() {
        game.getFrame(1).reset();
        try {
            game.getFrame(1).setPinsDown(2, 5);
        } catch (BowlingException exception) {
            Assert.assertEquals(0, game.getFrame(1).getPinsDown(2));
        }
    }

    /**
     * Erreur: Ce test a pour but de vérifier le nombre de lancer lorsque le premier est un dalot
     */
    @Test
    public void checkCountRollsWhenFirstRollIsDalot() {
        game.getFrame(1).reset();
        game.getFrame(1).setPinsDown(1, 0).setPinsDown(2, 5);
        Assert.assertEquals(2, game.getFrame(1).countRolls());
    }

    /**
     * Ce test a pour but de vérifier le nombre de quille tombée quand le 1er lancé est un dalot
     */
    @Test
    public void checkPinsDownWhenFirstRollIsDalot() {
        game.getFrame(1).reset();
        game.getFrame(1).setPinsDown(1, 0).setPinsDown(2, 5);
        Assert.assertEquals(5, game.getFrame(1).countPinsDown());
    }

    /**
     * Erreur: Ce test a pour but de vérifier l'existance d'une exception lorsque le score d'un lancer est négatif
     */
    @Test(expected = BowlingException.class)
    public void checkCannotEnterNegativeNumberWhenRolling() {
        game.getFrame(1).reset();
        game.getFrame(1).setPinsDown(1, -1);
    }

    /**
     * Erreur: Ce test a pour but de vérifier le score si tout les lancer sont des strikes sur les NormalFrame
     */
    @Test
    public void checkScoreIfAllStrike() {
        resetGameFrames();
        game.getFrame(0).setPinsDown(1, 10);
        game.getFrame(1).setPinsDown(1, 10);
        game.getFrame(2).setPinsDown(1, 10);
        game.getFrame(3).setPinsDown(1, 10);
        game.getFrame(4).setPinsDown(1, 10);
        game.getFrame(5).setPinsDown(1, 10);
        game.getFrame(6).setPinsDown(1, 10);
        game.getFrame(7).setPinsDown(1, 10);
        game.getFrame(8).setPinsDown(1, 10);
        game.getFrame(9).setPinsDown(1, 3).setPinsDown(2, 5);
        Assert.assertEquals(188, game.getCumulativeScore(10));
    }

    private void setupGame() {
        game = new Game();
        game.addFrame(new NormalFrame(0));
        game.addFrame(new NormalFrame(1));
        game.addFrame(new NormalFrame(2));
        game.addFrame(new NormalFrame(3));
        game.addFrame(new NormalFrame(4));
        game.addFrame(new NormalFrame(5));
        game.addFrame(new NormalFrame(6));
        game.addFrame(new NormalFrame(7));
        game.addFrame(new NormalFrame(8));
        game.addFrame(new LastFrame(9));
    }
}
