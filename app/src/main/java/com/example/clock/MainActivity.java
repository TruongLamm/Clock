package com.example.clock;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.app.AutomaticZenRule;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;
import android.os.CountDownTimer;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private CountDownTimer countDownTimer;
    private Button startPauseButton;
    private ImageButton iconImageStartPause;
    private Button resetButton;
    private boolean isCountdownPaused = false;
    private long remainingTimeInMillis;
    private ImageView iconImageReset;
    private ImageView iconImageView;
    private TextView titleTextView;
    private TextView timeTextView;
    private long originallySetTimeInMillis;
    private ImageButton exitButton;
    private boolean isCountdownRunning = false;
    private long totalTimeInMillis = 60000 * 3; // Total time in milliseconds (e.g., 60 seconds)

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock); //view

        iconImageView = findViewById(R.id.iconImageView);
        titleTextView = findViewById(R.id.titleTextView);
        exitButton = findViewById(R.id.exitButton);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.circular_progress));
        timeTextView = findViewById(R.id.timeTextView);

        startPauseButton = findViewById(R.id.startPauseButton);
        iconImageStartPause = findViewById(R.id.iconImageStartPause);
        resetButton = findViewById(R.id.resetButton);
        iconImageReset = findViewById(R.id.iconImageReset);

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // finish(); // Exit the activity
                exitApp();
            }
        });

        iconImageStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCountdownRunning) {
                    pauseCountdown();
                    iconImageStartPause.setImageResource(R.drawable.ic_pause);
                } else {
                    if (isCountdownPaused) {
                     // Resume the countdown if it was paused
                        Log.d("Test", "isCountdownResume");
                    } else {
                        startCountdown();
                        Log.d("Test", "isCountdownPaused");
                        iconImageStartPause.setImageResource(R.drawable.ic_start);
                    }
                }
            }
        });

        iconImageReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetCountdown();
            }
        });
    }

    private void exitApp() {
        moveTaskToBack(true); // Minimize the app by moving it to the background
    }

    private void startCountdown() {
        originallySetTimeInMillis = totalTimeInMillis; // Store the originally set time
        countDownTimer = new CountDownTimer(totalTimeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsRemaining = (int) (millisUntilFinished / 1000);

                // Calculate progress based on remaining time
                /*progress tính toán tiến độ (percentage) dựa trên thời gian còn lại
                và thời gian ban đầu. Nó được tính bằng cách chia số giây còn lại cho
                tổng số giây ban đầu và nhân với 100 để đưa ra kết quả là phần trăm.*/
                int progress = (int) ((float) secondsRemaining / (float) (originallySetTimeInMillis / 1000) * 100);

                progressBar.setProgress(progress);
                updateTimeTextView(secondsRemaining);
            }

            @Override
            public void onFinish() {
                // Countdown has finished, handle it here
                isCountdownRunning = false;
                startPauseButton.setText("Start");
            }
        };

        countDownTimer.start();
        isCountdownRunning = true;
        startPauseButton.setText("Pause");
        iconImageReset.setEnabled(false); // Disable the reset button while the countdown is running
    }

    private void updateTimeTextView(int secondsRemaining) {
        int hours = secondsRemaining / 3600;
        int minutes = (secondsRemaining % 3600) / 60;
        int seconds = secondsRemaining % 60;
        String timeText = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        timeTextView.setText(timeText);
    }

    private void pauseCountdown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            isCountdownRunning = false;
            startPauseButton.setText("Resume");
            // isCountdownPaused = true;
            iconImageReset.setEnabled(true); // Enable the reset button when the countdown is paused
        }
    }


    private void resetCountdown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // Restore the originally set time and update the UI
        totalTimeInMillis = originallySetTimeInMillis;
        updateTimeTextView((int) (totalTimeInMillis / 1000));

        progressBar.setProgress(100); // Reset the progress to 100%
        isCountdownRunning = false;
        startPauseButton.setText("Start");
        iconImageReset.setEnabled(false); // Disable the reset button after resetting
    }
}