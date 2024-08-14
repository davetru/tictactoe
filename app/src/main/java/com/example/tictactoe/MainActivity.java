package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // Variables to keep track of the game state
    boolean playerOneActive;
    private TextView playerOneScore, playerTwoScore, playerStatus;
    private Button[] buttons = new Button[9];
    private Button reset, playagain;
    int[] gameState = {2,2,2,2,2,2,2,2,2};
    int[][] winningPositions = {{0,1,2}, {3,4,5}, {6,7,8}, {0,3,6},
            {1,4,7}, {2,5,8}, {0,4,8}, {2,4,6}};
    int rounds;
    private int playerOneScoreCount, playerTwoScoreCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing the UI components
        playerOneScore = findViewById(R.id.score_Player1);
        playerTwoScore = findViewById(R.id.score_Player2);
        playerStatus = findViewById(R.id.textStatus);
        reset = findViewById(R.id.btn_reset);
        playagain = findViewById(R.id.btn_play_again);

        // Initializing the buttons for the game
        for(int i = 0; i < buttons.length; i++) {
            String buttonID = "btn" + i;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            buttons[i] = findViewById(resID);
            buttons[i].setOnClickListener(this);
        }

        // Resetting the score and setting the initial player
        playerOneScoreCount = 0;
        playerTwoScoreCount = 0;
        playerOneActive = true;
        rounds = 0;
    }

    @Override
    public void onClick(View view) {
        if(!((Button)view).getText().toString().equals("") || checkWinner()) {
            return;
        }

        // Preventing action if the button already has a value or if a winner is already declared
        if(!((Button)view).getText().toString().equals("") || checkWinner()) {
            return;
        }

        // Determining which button was clicked
        String buttonID  = view.getResources().getResourceEntryName(view.getId());
        int gameStatePointer = Integer.parseInt(buttonID.substring(buttonID.length()-1));

        // Setting the text on the buttons based on the active player
        if(playerOneActive) {
            ((Button)view).setBackgroundResource(R.drawable.x);
            gameState[gameStatePointer] = 0;
        } else {
            ((Button)view).setBackgroundResource(R.drawable.o);
            gameState[gameStatePointer] = 1;
        }

        // Incrementing the round counter
        rounds++;

        // Checking for a winner after each move
        if(checkWinner()) {
            if(playerOneActive) {
                playerOneScoreCount++;
                updatePlayerScore();
                playerStatus.setText("Player 1 wins!");
            } else {
                playerTwoScoreCount++;
                updatePlayerScore();
                playerStatus.setText("Player 2 wins!");
            }
        } else if(rounds == 9) {
            // If all rounds are completed and no winner, declare a draw
            playerStatus.setText("Draw!");
        } else {
            // Switching the active player
            playerOneActive = !playerOneActive;
        }

        // Setting the onClickListener for the reset button
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playAgain();
                playerOneScoreCount = 0;
                playerTwoScoreCount = 0;
                updatePlayerScore();
            }
        });

        // Setting the onClickListener for the play again button
        playagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playAgain();
            }
        });
    }

    // Method to check if there is a winner
    private boolean checkWinner() {
        boolean winnerFound = false;
        for (int[] winningPosition : winningPositions) {
            if(gameState[winningPosition[0]] == gameState[winningPosition[1]] &&
                    gameState[winningPosition[1]] == gameState[winningPosition[2]] &&
                    gameState[winningPosition[0]] != 2) {
                winnerFound = true;
                break;
            }
        }

        if(winnerFound) {
            Animation bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce);
            playerStatus.startAnimation(bounceAnimation);
        }

        return winnerFound;
    }

    // Method to reset the game to its initial state
    private void playAgain() {
        rounds = 0;
        playerOneActive = true;
        for (int i = 0; i < buttons.length; i++) {
            gameState[i] = 2;
            buttons[i].setText("");
        }
        playerStatus.setText("Status");
    }

    // Method to update the score display
    private void updatePlayerScore() {
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        playerOneScore.startAnimation(fadeIn);
        playerTwoScore.startAnimation(fadeIn);
        playerOneScore.setText(Integer.toString(playerOneScoreCount));
        playerTwoScore.setText(Integer.toString(playerTwoScoreCount));
    }
}

