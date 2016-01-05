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
public class SettingsFragment extends Fragment {
    public static Integer count = new Integer(10);


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(
                R.layout.fragment_settings, container, false);
        Bundle args = getArguments();

        SeekBar seekBarCount = (SeekBar) rootView.findViewById(R.id.seekBarCount);
        final TextView fieldCount = (TextView) rootView.findViewById(R.id.textView2);
        seekBarCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                fieldCount.setText("Количество: " + progress);
                synchronized (count) {
                    count = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Button deleteAll = (Button) rootView.findViewById(R.id.button2);
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainThread.handleDeleteButton();
            }
        });

        Button addN = (Button) rootView.findViewById(R.id.button);
        addN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainThread.handleAddButton();
            }
        });

        Button exit = (Button) rootView.findViewById(R.id.button3);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                System.exit(0);
            }
        });
        return rootView;
    }
}