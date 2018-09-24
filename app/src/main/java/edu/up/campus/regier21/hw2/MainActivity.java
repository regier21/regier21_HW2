package edu.up.campus.regier21.hw2;

import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;


/**
 * @author Ryan Regier
 * @version 1.0
 *
 * This class is the Activity that is the sole entry point for the program.
 * It serves as the controller for the application.
 */
public class MainActivity extends AppCompatActivity {

    private Face face;
    private FaceView faceView;

    private SeekBar redSeek;
    private SeekBar greenSeek;
    private SeekBar blueSeek;

    /**
     * Called to start program
     * @param savedInstanceState ignored
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: Remove debug code when complete
        face = new Face();
        face.setSkinColor(Color.BLUE);
        face.setEyeColor(Color.GREEN);

        faceView = findViewById(R.id.viewFace);
        faceView.setActivity(this);

        initializeSpinner();

        ColorListener listener = new ColorListener();

        redSeek = findViewById(R.id.seekBarRed);
        redSeek.setOnSeekBarChangeListener(listener);
        blueSeek = findViewById(R.id.seekBarBlue);
        blueSeek.setOnSeekBarChangeListener(listener);
        greenSeek = findViewById(R.id.seekBarGreen);
        greenSeek.setOnSeekBarChangeListener(listener);

        RadioGroup group = findViewById(R.id.radioGroup);
        group.setOnCheckedChangeListener(listener);
        group.check(R.id.radioButtonHair); //Also initializes seek bars in event listeners


    }

    /**
     * Populates spinnerHairStyle and sets event listener.
     */
    private void initializeSpinner(){
        /**
        External Citation
         Date: 23 Sept. 2018
         Problem: Needed to set the contents of spinnerHairStyle
         Resource:
             https://developer.android.com/guide/topics/ui/controls/spinner
         Solution: Using ArrayAdapter to create spinner
         */
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.hair_styles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = findViewById(R.id.spinnerHairStyle);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new SpinnerListener());
    }

    public void drawFace(Canvas canvas){
        face.onDraw(canvas);
        //Log.d("MainActivity", "Drawn Face");
    }

    protected class SpinnerListener implements AdapterView.OnItemSelectedListener{
        /**
         * Called when a hair style is selected
         * @param parent Ignored
         * @param view Ignored
         * @param position The hair style selected
         * @param id Ignored
         */
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            face.setHairStyle(position);
            faceView.invalidate();
            //Log.d("SpinnerListener", String.format("Hair style: %d", position));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            //Do nothing
        }
    }

    protected class ColorListener implements RadioGroup.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener{

        public static final @IdRes int HAIR_ID = R.id.radioButtonHair;
        public static final @IdRes int EYES_ID = R.id.radioButtonEyes;
        public static final @IdRes int SKIN_ID = R.id.radioButtonSkin;

        /**
         * Which radio button is selected.
         * 0: Hair
         * 1: Eyes
         * 2: Skin
         */
        private int state = 0;

        /**
         * TODO: External citation for event listener (9/24)
         * @param group
         * @param checkedId
         */
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            @ColorInt int color;
            switch(checkedId) {
                case HAIR_ID:
                    color = face.getHairColor();
                    state = 0;
                    break;
                case EYES_ID:
                    color = face.getEyeColor();
                    state = 1;
                    break;
                case SKIN_ID:
                    color = face.getSkinColor();
                    state = 2;
                    break;
                default:
                    //Unreachable
                    Log.e("MainActivity", "Radio button selection ignored");
                    return;
            }

            redSeek.setProgress(Color.red(color));
            greenSeek.setProgress(Color.green(color));
            blueSeek.setProgress(Color.blue(color));
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            @ColorInt int color = Color.rgb(redSeek.getProgress(), greenSeek.getProgress(), blueSeek.getProgress());
            switch(state){
                case 0: //Hair selected
                    face.setHairColor(color);
                    break;
                case 1: //Eyes selected
                    face.setEyeColor(color);
                    break;
                case 2: //Skin selected
                    face.setSkinColor(color);
                    break;
                default:
                    //Unreachable
                    Log.e("MainActivity", "Reached unsupported state for color editing");
                    return; //Prevents invalidation
            }

            faceView.invalidate();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }
}
