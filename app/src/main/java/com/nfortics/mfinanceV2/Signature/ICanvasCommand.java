package com.nfortics.mfinanceV2.Signature;


import android.graphics.Canvas;

/**
 * Created by Bernard Adjei Oppong.
 * Date: 08/04/2012
 * Time: 10:34 AM
 */
public interface ICanvasCommand {
    public void draw(Canvas canvas);
    public void undo();
}
