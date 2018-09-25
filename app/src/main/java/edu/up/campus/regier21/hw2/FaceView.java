package edu.up.campus.regier21.hw2;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;

/**
 * The surface view on which a face is drawn.
 *
 * @author Ryan Regier
 * @version 1.0
 */
public class FaceView extends SurfaceView {

    MainActivity activity; //The activity to call to draw the face

    public FaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Needed so that the view can ask the activity to redraw the face
     * @param activity
     */
    public void setActivity(MainActivity activity){
        this.activity = activity;
    }

    /**
     * Called whenever the view needs to be redrawn.
     * To maintain seperation between model and view, this method transforms the canvas such that
     * it is centered at (0, 0) and goes from negative to positive 50 in both the x and y directions.
     * It then asks {@code activity} to draw the face on this modified canvas.
     * Finally, it undoes the transformations, fitting the canvas to the view.
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        /**
         * External citation (canvas transform methods, 23 Sept. 2018)
         *  Date: 23 Sept. 2018
         *  Problem: Wanted draw methods in face to use absolute coordinates and not be concerned
         *      about size of view. (This is a view concern, not a model concern)
         *  Resource: https://developer.android.com/reference/android/graphics/Canvas
         *  Solution: Used the canvas save, restore, clip, and transform methods to return canvas of
         *      uniform size and dimensions.
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
            //By these scaling factors, will scale to take up as much of the view as possible
            canvas.scale(minDist/Face.FACE_RADIUS, minDist/Face.FACE_RADIUS);

            //Actually draws the face
            activity.drawFace(canvas);

            //Returns the canvas to its original state
            canvas.restore();
        } else {
            Log.w("FaceView", "Activity not defined for FaceView");
        }
    }
}
