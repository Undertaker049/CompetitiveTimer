package com.example.competitivetimer;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
{
    private int seconds = 0;
    private int currentCircleResult = 0;
    private boolean isRunning = false;
    private boolean wasRunning = false;
    private TextView textViewTimer;
    private TextView circleResult1;
    private TextView circleResult2;
    private TextView circleResult3;
    private TextView circleResult4;
    private TextView circleResult5;
    private RadioButton radiobutton_continue;
    private EditText editName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewTimer = findViewById(R.id.textViewTimer);
        editName = findViewById(R.id.editName);

        circleResult1 = findViewById(R.id.circleResult1);
        circleResult2 = findViewById(R.id.circleResult2);
        circleResult3 = findViewById(R.id.circleResult3);
        circleResult4 = findViewById(R.id.circleResult4);
        circleResult5 = findViewById(R.id.circleResult5);

        if (savedInstanceState != null)
        {
            seconds = savedInstanceState.getInt("seconds");
            isRunning = savedInstanceState.getBoolean("isRunning");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
            currentCircleResult = savedInstanceState.getInt("currentCircleResult");
        }

        runTimer();
        radiobutton_continue = findViewById(R.id.radiobutton_continue);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putInt("seconds", seconds);
        outState.putBoolean("isRunning", isRunning);
        outState.putBoolean("wasRunning", wasRunning);
        outState.putInt("currentCircleResult", currentCircleResult);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (!radiobutton_continue.isChecked())
        {
            isRunning = wasRunning;
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        wasRunning = isRunning;

        if (!radiobutton_continue.isChecked())
        {
            wasRunning = isRunning;
            isRunning = false;
        }
    }

    public void onClickStartTimer(View view)
    {
        isRunning = true;
    }

    public void onClickStopTimer(View view)
    {
        isRunning = false;
    }

    public void onClickResetTimer(View view)
    {
        isRunning = false;
        seconds = 0;
        currentCircleResult = 0;

        editName.setText("");

        circleResult1.setText("");
        circleResult2.setText("");
        circleResult3.setText("");
        circleResult4.setText("");
        circleResult5.setText("");
    }

    public void onClickCircleResult(View view)
    {
        seconds = 0;
        TextView currentCircleTextView = getNextCircleTextView();

        if (currentCircleTextView != null)
        {
            currentCircleTextView.setText(textViewTimer.getText());
            currentCircleResult = (currentCircleResult + 1) % 5;
        }
    }

    public void onClickCompetitionResult(View view)
    {
        TextView currentCircleTextView = getNextCircleTextView();

        if (currentCircleTextView != null)
        {
            String time = textViewTimer.getText().toString();
            String competitionResultText = editName.getText().toString();

            if (competitionResultText.isEmpty())
            {
                competitionResultText = getString(R.string.сompetition_result);
                currentCircleTextView.setText(competitionResultText + " " + time);
            }

            else
            {
                currentCircleTextView.setText(competitionResultText + ": " + time);
            }

            editName.setText("");
            currentCircleResult = (currentCircleResult + 1) % 5;
        }
    }

    private TextView getNextCircleTextView()
    {
        TextView[] textViews = {circleResult1, circleResult2, circleResult3, circleResult4, circleResult5};
        return textViews[currentCircleResult];
    }

    public void onClickSend(View view)
    {
        String result1 = circleResult1.getText().toString();
        String result2 = circleResult2.getText().toString();
        String result3 = circleResult3.getText().toString();
        String result4 = circleResult4.getText().toString();
        String result5 = circleResult5.getText().toString();

        String competitionResultsText = getString(R.string.сompetition_results);

        String allResults = competitionResultsText + " " +
                "1. " + result1 + "; " +
                "2. " + result2 + "; " +
                "3. " + result3 + "; " +
                "4. " + result4 + "; " +
                "5. " + result5;

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, allResults);
        Intent chosenIntent = Intent.createChooser(intent, getString(R.string.choose));
        startActivity(chosenIntent);
    }

    private void runTimer()
    {
        final Handler handler = new Handler();

        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                String time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);
                textViewTimer.setText(time);

                if (isRunning)
                {
                    seconds++;
                }

                handler.postDelayed(this, 1000);
            }
        });
    }
}