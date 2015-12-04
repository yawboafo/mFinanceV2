package com.nfortics.mfinanceV2.Signature;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by Bernard Adjei Oppong.
 * Date: 08/04/2012
 * Time: 10:34 AM
 */
public class DoodlingPath implements ICanvasCommand{
    public Path path;
    public Paint paint;

    public void draw(Canvas canvas) {
        canvas.drawPath( path, paint );
    }

    public void undo() {
        //Todo this would be changed later
    }
}
