package neural; /**
 * Created by ethanm on 1/25/17.
 */

import checkers.CheckersGame;
import org.encog.neural.neat.NEATNetwork;

public class NeuralPlayer {
    private NEATNetwork network;
    private NEATNetwork[] opponents;
    private int wins = 0;
    private int losses = 0;
    private int ties = 0;
    private int wins2 = 0;
    private int losses2 = 0;
    private int ties2 = 0;


    public NeuralPlayer(NEATNetwork network, NEATNetwork[] opponents) {
        this.opponents = opponents;
        this.network = network;
    }

    public int scorePlayer() {
        int n = 0;
        for (int i = 0; i < this.opponents.length; i++) {
            n += doIterationA();
            n -= doIterationB();
        }
        //System.out.println("W1:" + wins + " T1:" + ties + " L1:" + losses );
        //System.out.println("W2:" + wins2+ " T2:" + ties2+ " L2:" + losses2);
        //System.out.print(n/2+"\n\n");
        return n/2;
    }

    public int doIterationA() {

        TicTacToeGame game = new TicTacToeGame();
        game.initializeGame();
        for(int i = 0; i<9;i++)
            if(game.winner ==-2)
                game.turnR(this.network);
        //Test stuff
        if (game.winner == 1) wins++;
        if (game.winner == 0) ties++;
        if (game.winner == -1)losses++;

        return game.winner;
    }

    public int doIterationB() {

        TicTacToeGame game = new TicTacToeGame();
        game.initializeGame();
        for(int i = 0; i<9;i++)
            if(game.winner ==-2)
                game.turnR2(this.network);
        //Test stuff
        if (game.winner == -1)wins2++;
        if (game.winner == 0) ties2++;
        if (game.winner == 1) losses2++;
        return game.winner;
    }

    private int wl (int gameResult, int player) {
        if (gameResult == player*-1)
            return gameResult*5;
        else
            return gameResult;
    }

    private int playGame(NEATNetwork network1, NEATNetwork network2) {
        CheckersGame game = new CheckersGame();
        game.initializeGame();
        game.getBluePlayer().setNetwork(network1);
        game.getRedPlayer().setNetwork(network2);

        if (MainTrain.AM_DEBUGGING) {
            game.playGUI();

            while (game.getWinner() == -2);

        } else {

            //game.playGUI();
            while (game.getWinner() == -2) {
                game.turn();
            }

        }

        int winStatus = game.getWinner();
        game = null;


        // UNCOMMENT IN TIMES OF STRESS <-lol
        //System.gc();
        return winStatus;
    }
}

