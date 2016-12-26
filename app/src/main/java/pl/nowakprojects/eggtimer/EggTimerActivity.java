package pl.nowakprojects.eggtimer;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class EggTimerActivity extends AppCompatActivity implements EggTimerView {

    private EggTimerPresenter presenter;
    private SeekBar timerSlider;
    private TextView timerTextView;
    private FloatingActionButton timerControlButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_egg_timer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initPresenter();

        timerSlider = (SeekBar) findViewById(R.id.timerSeekBar);
        timerTextView = (TextView) findViewById(R.id.timerTextView);
        timerControlButton = (FloatingActionButton) findViewById(R.id.timerControlButton);

        presenter.initViewUserInterface();
    }


    @Override
    public void initPresenter() {
        presenter = new EggTimerPresenter(this);
    }


    @Override
    public void setTimerStateTextView(int minutes, int seconds) {
        String secondsString = String.valueOf(seconds);
        if(seconds<10)
            secondsString = "0" + secondsString;

        timerTextView.setText(getString(R.string.timer_minutes_seconds,minutes,secondsString));
    }

    @Override
    public void setTimerControlButton() {
        timerControlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerControlButton.setEnabled(false);
                new CountDownTimer(presenter.getTimerCurrentTimeInSeconds() * 1000,1000){

                    @Override
                    public void onTick(long l) {
                        presenter.setTimerCurrentTime((int) l/1000);
                        presenter.updateTimerStateTextView();
                        Log.i("TimeState:",String.valueOf(l));
                    }

                    @Override
                    public void onFinish() {
                        onTick(1000);
                        presenter.updateTimerStateTextView();
                        timerControlButton.setEnabled(true);
                    }
                }.start();
            }
        });
    }


    @Override
    public void setupTimerSlider() {
        timerSlider.setMax(presenter.getTimerMaxSeconds());
        timerSlider.setProgress(presenter.getTimerCurrentTimeInSeconds());

        timerSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                presenter.setTimerCurrentTime(i);
                presenter.updateTimerStateTextView();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}