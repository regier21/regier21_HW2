package edu.up.campus.regier21.hw2;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.util.Log;

/**
 * @author Ryan Regier
 * @version 1.0
 *
 * Stores the state of the Face.
 * Can draw the face using its current state.
 * Note: This is not a view for the face.
 * @see FaceView
 */
public class Face{

    private @ColorInt int skinColor = 0xFF000000;
    private @ColorInt int eyeColor = 0xFF000000;
    private @ColorInt int hairColor = 0xFF000000;

    private Paint skinPaint = new Paint();
    private Paint eyePaint = new Paint();
    private Paint hairPaint = new Paint();

    public static final int STYLE_BALD = 0;
    public static final int STYLE_SHORT = 1;
    public static final int STYLE_LONG = 2;
    public static final int[] STYLES = {STYLE_BALD, STYLE_SHORT, STYLE_LONG};
    private int hairStyle = STYLE_BALD;

    public static final int FACE_RADIUS = 50;

    public void setSkinColor(@ColorInt int skinColor) {
        this.skinColor = skinColor;
        skinPaint.setColor(skinColor);
    }

    public void setEyeColor(@ColorInt int eyeColor) {
        this.eyeColor = eyeColor;
        eyePaint.setColor(eyeColor);
    }

    public void setHairColor(@ColorInt int hairColor) {
        this.hairColor = hairColor;
        hairPaint.setColor(hairColor);
    }

    /**
     * Will fail if {@code hairStyle} is not in {@code STYLES}
     * @param hairStyle
     */
    public void setHairStyle(int hairStyle) {
        for (int style : STYLES) {
            if (hairStyle == style) {
                this.hairStyle = hairStyle;
                return;
            }
        }
    }

    public @ColorInt int getSkinColor() {
        return skinColor;
    }

    public @ColorInt int getEyeColor() {
        return eyeColor;
    }

    public @ColorInt int getHairColor() {
        return hairColor;
    }

    /**
     * Will always return value in {@code STYLES}
     * @return The face's hair style
     */
    public int getHairStyle() {
        return hairStyle;
    }

    public Face(){
        eyePaint.setColor(eyeColor);
        hairPaint.setColor(hairColor);
        skinPaint.setColor(skinColor);
        hairPaint.setAntiAlias(true);

    }

    protected void randomize(){}

    /**
     * Draws a face on {@code canvas} centered at (0, 0).
     *
     * @param canvas The canvas to draw on. Center should be at (0, 0). Zoom should be where \
     *               edges are at -50 and 50 (FACE_RADIUS)
     */
    public void onDraw(Canvas canvas){
        /**
         * External Citation
         * Date: 23 Sept. 2018
         * Problem: Needed to know how to draw on canvas
         * Resource:
         *  https://developer.android.com/reference/android/graphics/Canvas
         * Solution: Using appropriate method calls
         */
        canvas.drawCircle(0, 0, FACE_RADIUS, skinPaint); //Draw the face
        //Log.d("Face", "Face drawn");

        drawEyes(canvas);
        drawHair(canvas);
    }

    protected void drawHair(Canvas canvas){
        switch (hairStyle){
            case STYLE_BALD:
                //Draw nothing
                break;
            case STYLE_SHORT:
                canvas.drawArc(new RectF(-FACE_RADIUS, -FACE_RADIUS, FACE_RADIUS, FACE_RADIUS),
                        -45, -90, false, hairPaint);
                break;
            case STYLE_LONG:
                break;
            default:
                //Should be unreachable
                Log.e("Face", "Invalid hair style");
        }
    }

    protected void drawEyes(Canvas canvas){}
}
