package com.shashov.particles;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import com.shashov.particles.model.MainThread;

/**
 * Created by kirill on 29.12.2015.
 */
public class MainFragment extends Fragment {
    public static Integer weight = new Integer(10);
    private boolean pause = false;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        final View rootView = inflater.inflate(
                R.layout.fragment_app, container, false);
        Bundle args = getArguments();
        /*((TextView) rootView.findViewById(android.R.id.text1)).setText(
                Integer.toString(args.getInt(ARG_OBJECT)));*/
        SeekBar seekBarWeight = (SeekBar) rootView.findViewById(R.id.seekBarWeight);
        final TextView fieldWeight = (TextView) rootView.findViewById(R.id.textView);
        seekBarWeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                fieldWeight.setText("Масса: " + progress);
                synchronized (weight) {
                    weight = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        final Button buttonPlay = (Button) rootView.findViewById(R.id.buttonPlay);
        buttonPlay.setText("||");
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pause == false) {
                    buttonPlay.setText(">");
                    pause = true;

                } else {

                    buttonPlay.setText("||");
                    pause = false;
                }

                MainThread.handlePlayButton();
            }
        });
        return rootView;
    }
}