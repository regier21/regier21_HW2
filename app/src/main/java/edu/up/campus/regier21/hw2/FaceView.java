package edu.up.campus.regier21.hw2;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;

public class FaceView extends SurfaceView {

    MainActivity activity;

    public FaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setActivity(MainActivity activity){
        this.activity = activity;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        /**
         * External citation (canvas transform methods, 23 Sept. 2018)
         */
        if (activity != null){
            float width = canvas.getWidth();
            float height = canvas.getHeight();

            //The distance from the center to the edge of the smallest dimension
            float minDist = Math.min(width, height)/2;

            canvas.save();

            //Translates, so 0, 0 is the center of the canvas
            canvas.translate(width/2, height/2);

            //Makes the canvas a square constrained by its smaller dimension
            canvas.clipRect(-minDist, -minDist, minDist, minDist);

            //Scales the canvas so that the distance so that its size is 50 x 50
            //By these scaling factors, will scale to take up as much of the screen as possible
            canvas.scale(minDist/Face.FACE_RADIUS, minDist/Face.FACE_RADIUS);

            activity.drawFace(canvas);
            canvas.restore();
            //Log.d("FaceView", "Face Drawn");
        } else {
            Log.w("FaceView", "Activity not defined for FaceView");
        }
    }
}
