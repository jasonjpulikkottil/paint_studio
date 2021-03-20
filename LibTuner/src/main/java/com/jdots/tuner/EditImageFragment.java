package com.jdots.tuner;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditImageFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private Bitmap bitmap;

    private PictureThread thread;

    @BindView(R2.id.seekbar_brightness)
    SeekBar seekBarBrightness;

    @BindView(R2.id.seekbar_contrast)
    SeekBar seekBarContrast;

    @BindView(R2.id.seekbar_saturation)
    SeekBar seekBarSaturation;



    public EditImageFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_image, container, false);

        ButterKnife.bind(this, view);

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.paint_1);
        ImageView mLogo = (ImageView)getActivity().findViewById(R.id.image_preview);
        mLogo.setImageBitmap(bitmap);

        thread = new PictureThread(mLogo,bitmap);
        thread.start();



        ButterKnife.bind(this, view);
        seekBarBrightness.setMax(200);
        seekBarBrightness.setProgress(100);

        seekBarContrast.setMax(100);
        seekBarContrast.setProgress(0);

        seekBarSaturation.setMax(510);
        seekBarSaturation.setProgress(255);

        seekBarBrightness.setOnSeekBarChangeListener(this);
        seekBarContrast.setOnSeekBarChangeListener(this);
        seekBarSaturation.setOnSeekBarChangeListener(this);


        seekBarBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){

                thread.adjustBrightness(seekBar.getProgress()-100);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }


        });



        seekBarContrast.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                thread.adjustContrast(seekBar.getProgress());

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }


        });


        seekBarSaturation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                thread.adjustSaturation(seekBar.getProgress()-255);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }


        });

        return view;


    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public interface EditImageFragmentListener {


        void onEditStarted();

        void onEditCompleted();
    }
}
