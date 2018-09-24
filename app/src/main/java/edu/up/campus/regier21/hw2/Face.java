package edu.up.campus.regier21.hw2;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
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


    //Static constants
    public static final int STYLE_BALD = 0;
    public static final int STYLE_SHORT = 1;
    public static final int STYLE_LONG = 2;
    public static final int[] STYLES = {STYLE_BALD, STYLE_SHORT, STYLE_LONG};
    private int hairStyle = STYLE_BALD;

    public static final int FACE_RADIUS = 50;

    public static final float LONG_HAIR_ARC_ANGLE = 20;
    public static final float LONG_HAIR_RECT_X;
    public static final float LONG_HAIR_RECT_Y;
    public static final int LONG_HAIR_RECT_WIDTH = 20;

    public static final Path HAIR_PATH;

    public static final float EYE_X = 20;
    public static final float EYE_Y = -15;
    public static final int EYE_RADIUS_OUTER = 5;
    public static final int EYE_RADIUS_INNER = 3;
    public static final Paint EYE_PAINT_OUTER;
    public static final int EYE_BORDER_THICKNESS = 1;
    public static final Paint EYE_PAINT_BORDER;
    public static final int EYE_RADIUS_PUPIL = 1;
    public static final Paint EYE_PAINT_PUPIL;

    //TODO: Cite source (23rd, Stack OVerflow tab)
    //Static initializers
    static {
        //Set long hair offsets
        double radians = Math.toRadians(LONG_HAIR_ARC_ANGLE);
        LONG_HAIR_RECT_X = (float) (FACE_RADIUS * Math.cos(radians)) + 10;
        LONG_HAIR_RECT_Y = (float) (-FACE_RADIUS * Math.sin(radians)) - 10;

        HAIR_PATH = new Path();
        HAIR_PATH.addArc(new RectF(-FACE_RADIUS, -FACE_RADIUS, FACE_RADIUS, FACE_RADIUS),
                0, -180);
        HAIR_PATH.addRect(new RectF(-FACE_RADIUS, 0, FACE_RADIUS, FACE_RADIUS), Path.Direction.CW);

        EYE_PAINT_OUTER = new Paint();
        EYE_PAINT_OUTER.setColor(Color.WHITE);
        EYE_PAINT_BORDER = new Paint();
        EYE_PAINT_BORDER.setColor(Color.BLACK);
        EYE_PAINT_PUPIL = new Paint();
        EYE_PAINT_PUPIL.setColor(Color.BLACK);
    }

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
     * @param hairStyle The new hair style for the face
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
        //hairPaint.setAntiAlias(true);
    }

    protected void randomize(){}

    /**
     * Draws a face on {@code canvas} centered at (0, 0).
     *
     * TODO: Add border around face
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

    private void drawHairArc(Canvas canvas, float start, float sweep){
        canvas.drawArc(new RectF(-FACE_RADIUS, -FACE_RADIUS, FACE_RADIUS, FACE_RADIUS),
                start, sweep, false, hairPaint);
    }

    //TODO: Remove constants
    protected void drawHair(Canvas canvas){
        canvas.save();
        canvas.clipPath(HAIR_PATH, Region.Op.INTERSECT); //Prevents hair from leaving top half of head

        switch (hairStyle){
            case STYLE_BALD:
                //Draw nothing
                break;
            case STYLE_SHORT:
                drawHairArc(canvas,-45, -90);
                break;
            case STYLE_LONG:
                //Draw same as short hair
                drawHairArc(canvas, -45, -90);

                //Draw two extra "rotated" arcs
                drawHairArc(canvas, -LONG_HAIR_ARC_ANGLE, -90);
                drawHairArc(canvas, -90 + LONG_HAIR_ARC_ANGLE, -90);

                //Draw rects
                canvas.drawRect(-LONG_HAIR_RECT_X, LONG_HAIR_RECT_Y, -LONG_HAIR_RECT_X + LONG_HAIR_RECT_WIDTH,
                        FACE_RADIUS, hairPaint);
                canvas.drawRect(LONG_HAIR_RECT_X - LONG_HAIR_RECT_WIDTH, LONG_HAIR_RECT_Y, LONG_HAIR_RECT_X,
                        FACE_RADIUS, hairPaint);
                break;
            default:
                //Should be unreachable
                Log.e("Face", "Invalid hair style");
        }

        canvas.restore();
    }

    protected void drawEyes(Canvas canvas){
        drawEye(canvas, EYE_X, EYE_Y);
        drawEye(canvas, -EYE_X, EYE_Y);
    }

    private void drawEye(Canvas canvas, float x, float y){
        canvas.drawCircle(x, y, EYE_RADIUS_OUTER + EYE_BORDER_THICKNESS, EYE_PAINT_BORDER);
        canvas.drawCircle(x, y, EYE_RADIUS_OUTER, EYE_PAINT_OUTER);
        canvas.drawCircle(x, y, EYE_RADIUS_INNER, eyePaint);
        canvas.drawCircle(x, y, EYE_RADIUS_PUPIL, EYE_PAINT_PUPIL);
    }
}
