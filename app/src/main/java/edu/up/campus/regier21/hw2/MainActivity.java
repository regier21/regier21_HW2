package edu.up.campus.regier21.hw2;

import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

    /**
     * Called to start program
     * @param savedInstanceState ignored
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        face = new Face();
        face.setSkinColor(Color.BLUE);
        faceView = findViewById(R.id.viewFace);
        faceView.setActivity(this);

        initializeSpinner();

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

    private class SpinnerListener implements AdapterView.OnItemSelectedListener{
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


}
