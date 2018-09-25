package edu.up.campus.regier21.hw2;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.util.Log;

import java.util.Random;

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

    //Stores the color for the skin, eyes, and hair respectively
    private @ColorInt int skinColor = 0xFF000000;
    private @ColorInt int eyeColor = 0xFF000000;
    private @ColorInt int hairColor = 0xFF000000;

    //Paints used to draw these elements
    //Color set to value of corresponding variable
    private Paint skinPaint = new Paint();
    private Paint eyePaint = new Paint();
    private Paint hairPaint = new Paint();

    //Used to randomize the face
    private Random random = new Random();

    //Static constants

    //Radius of the face
    public static final int FACE_RADIUS = 50;

    //Hair styles
    //@IntDef was used to simulate an enum
    public static final int STYLE_BALD = 0;
    public static final int STYLE_SHORT = 1;
    public static final int STYLE_LONG = 2;

    /**
     * External citation
     *  Date: 24 Sept. 2018
     *  Problem: I wanted a way to ensure that hairStyle was always valid, which meant acting
     *      like an enum. However, the spec requires an int and (from the resource) enums are less
     *      efficient
     *  Resource: https://developer.android.com/reference/android/support/annotation/IntDef
     *  Solution: Using @IntDef, the linter can ensure hairStyle is not set to an invalid value
     *      when using compile-time constants.
     */
    @IntDef({STYLE_BALD, STYLE_SHORT, STYLE_LONG})
    public @interface style {}

    //List of styles. Used to validate if parameters are styles
    public static final @style int[] STYLES = {STYLE_BALD, STYLE_SHORT, STYLE_LONG};

    //Current hair style
    private @style int hairStyle = STYLE_BALD;

    //Hair variables

    //The clip path for drawing hair. Prevents hair from leaving face.
    //Helpful to cut off anti-aliasing and smooth corner of long hair rect
    public static final Path HAIR_PATH;
    //The y coordinate of the bottom of the arcs used to draw short and long hair
    public static final float HAIR_RECT_BOTTOM;

    //Long hair variables
    //The angle to start the additional arcs for the long hair
    public static final float LONG_HAIR_ARC_ANGLE = 20;
    //The width of the long hair rectangle
    public static final int LONG_HAIR_RECT_WIDTH = 20;
    //The y coordinate of where the long hair rectangle stops
    public static final int LONG_HAIR_RECT_BOTTOM = FACE_RADIUS - 5;

    //Eye variables

    //The coordinates for the center of the eye
    public static final float EYE_X = 20;
    public static final float EYE_Y = -15;
    //The radius of the white of the eye
    public static final int EYE_RADIUS_OUTER = 5;
    //The paint used to draw the white of the eye
    public static final Paint EYE_PAINT_OUTER;
    //The radius of the color of the eye
    public static final int EYE_RADIUS_INNER = 3;
    //The thickness of the outer border of the eye
    public static final int EYE_BORDER_THICKNESS = 1;
    //The paint used for the eye border
    public static final Paint EYE_PAINT_BORDER;
    //The radius of the pupil
    public static final int EYE_RADIUS_PUPIL = 1;
    //The paint used to draw the pupil
    public static final Paint EYE_PAINT_PUPIL;

    /**
     * External citation
     *  Date: 23 Sept. 2018
     *  Problem: Some of my static final values required complex or multiline calculations. I needed
     *      a way to set them, but they cannot be set in a constructor
     *  Resource: https://stackoverflow.com/questions/1642347/static-variable-initialization-java
     *  Solution: I am initializing these variables in a static initializer block
     */
    //Static initializer block
    static {

        //Create the path for clipping the canvas
        //Consists of a semicircle at the top half of the head
        //And a rectangle covering the bottom half of the canvas
        HAIR_PATH = new Path();
        HAIR_PATH.addArc(new RectF(-FACE_RADIUS, -FACE_RADIUS, FACE_RADIUS, FACE_RADIUS),
                0, -180);
        HAIR_PATH.addRect(new RectF(-FACE_RADIUS, 0, FACE_RADIUS, FACE_RADIUS), Path.Direction.CW);

        HAIR_RECT_BOTTOM = (float) (FACE_RADIUS*Math.sin(Math.toRadians(45)));

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
    public void setHairStyle(@style int hairStyle) {
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
    public @style int getHairStyle() {
        return hairStyle;
    }

    /**
     * Main constructor.
     *
     * Updates all the paint to the initial colors specified
     */
    public Face(){
        eyePaint.setColor(eyeColor);
        hairPaint.setColor(hairColor);
        skinPaint.setColor(skinColor);
        //hairPaint.setAntiAlias(true);
    }

    /**
     * Randomizes all the attributes of the face. This includes:
     * <ul>
     *     <li>Hair style</li>
     *     <li>Hair color</li>
     *     <li>Skin color</li>
     *     <li>Eye color</li>
     * </ul>
     */
    protected void randomize(){
        setHairColor(randomColor());
        setEyeColor(randomColor());
        setSkinColor(randomColor());
        setHairStyle(random.nextInt(STYLES.length));
    }

    /**
     * Generates a random color.
     *
     * @return A random color with full alpha
     */
    protected @ColorInt int randomColor(){
        int red = random.nextInt(256);
        int blue = random.nextInt(256);
        int green = random.nextInt(256);
        return Color.rgb(red, green, blue);
    }

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

    /**
     * Draws a rectangle on the upper portion of the canvas.
     * Will go from left to right edge of canvas
     * Goes from the top of the canvas (when rotated by {@code angle}) to {@code HAIR_RECT_BOTTOM}.
     *
     * Usual canvas restrictions apply (see onDraw).
     *
     * @param canvas The canvas to draw on
     * @param angle The angle the rectangle should be offset from the vertical, in degrees clockwise
     */
    private void drawHairRect(Canvas canvas, float angle){
        if (angle != 0){
            //Rotate the canvas by angle, then draw the rectangle
            canvas.save();
            canvas.rotate(angle);
            //Called recursively to make it clear there are no cases where restore is not called
            drawHairRect(canvas, 0);
            canvas.restore();
            return;
        }

        canvas.drawRect(new RectF(-FACE_RADIUS, -FACE_RADIUS, FACE_RADIUS, -HAIR_RECT_BOTTOM), hairPaint);
    }

    /**
     * Draws hair based on the set style and color.
     *
     * Rectangles are used to draw the arcs instead of canvas.drawArc.
     * drawArc pixelates when scale is restored, but circles and rectangles do not. (reason unknown)
     * As such, drawing rectangles on a clipped canvas is used to produce sharp edges.
     *
     * Usual canvas restrictions apply (see onDraw)
     *
     * @param canvas The canvas to draw on
     */
    protected void drawHair(Canvas canvas){
        //Allows us to restore the canvas after done clipping
        canvas.save();

        /**
         * External Citation
         *  Problem: The old method for clipping from a path was deprecated in favor of a new one.
         *      The old one was used in the android version corresponding to my minSdk, but the new
         *      method is used in my target version. Rather than call a deprecated method (which may
         *      fail in later versions), I wanted to call the appropriate method based on version.
         *  Resource:
         *      https://stackoverflow.com/questions/15502935/how-to-only-execute-code-on-certain-api-level#15503357
         *  Solution: I am using Build.VERSION.SDK_INT to select and run the appropriate method
         */

        //Prevents hair from leaving top half of head
        if(Build.VERSION.SDK_INT >= 26){
            /*@RequiresApi(26)*/ canvas.clipOutPath(HAIR_PATH); //This method does not exist in earlier versions
        } else {
            canvas.clipPath(HAIR_PATH, Region.Op.INTERSECT); //This method is deprecated in later versions
        }

        switch (hairStyle){
            case STYLE_BALD:
                //Draw nothing
                break;
            case STYLE_SHORT:
                drawHairRect(canvas, 0);
                break;
            case STYLE_LONG:
                //Draw same as short hair
                drawHairRect(canvas, 0);

                //Draw two extra "rotated" arcs
                drawHairRect(canvas, -LONG_HAIR_ARC_ANGLE);
                drawHairRect(canvas, LONG_HAIR_ARC_ANGLE);

                //Draw rects on both sides
                canvas.drawRect(-FACE_RADIUS, -FACE_RADIUS, -FACE_RADIUS + LONG_HAIR_RECT_WIDTH,
                        LONG_HAIR_RECT_BOTTOM, hairPaint);
                canvas.drawRect(FACE_RADIUS - LONG_HAIR_RECT_WIDTH, -FACE_RADIUS, FACE_RADIUS,
                        LONG_HAIR_RECT_BOTTOM, hairPaint);
                break;
            default:
                //Should be unreachable
                Log.e("Face", "Invalid hair style");
        }

        canvas.restore();
    }

    /**
     * Draws the eyes for the face.
     * Usual canvas restrictions apply (see onDraw)
     *
     * @param canvas The canvas to draw on
     */
    protected void drawEyes(Canvas canvas){
        drawEye(canvas, EYE_X, EYE_Y);
        drawEye(canvas, -EYE_X, EYE_Y);
    }

    /**
     * Draws an eye at the specified coordinates.
     * Usual canvas restrictions apply (see onDraw)
     *
     * @param canvas The canvas to draw on
     * @param x The x coordinate of the eye's center
     * @param y The y coordinate of the eye's center
     */
    private void drawEye(Canvas canvas, float x, float y){
        canvas.drawCircle(x, y, EYE_RADIUS_OUTER + EYE_BORDER_THICKNESS, EYE_PAINT_BORDER);
        canvas.drawCircle(x, y, EYE_RADIUS_OUTER, EYE_PAINT_OUTER);
        canvas.drawCircle(x, y, EYE_RADIUS_INNER, eyePaint);
        canvas.drawCircle(x, y, EYE_RADIUS_PUPIL, EYE_PAINT_PUPIL);
    }
}
