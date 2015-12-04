package com.nfortics.mfinanceV2.Widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.nfortics.mfinanceV2.Signature.CommandManager;
import com.nfortics.mfinanceV2.Signature.DoodlingPath;

/**
 * Created by bigfire on 11/9/2015.
 */
public class DoodlingSurface extends SurfaceView implements
        SurfaceHolder.Callback {
    private Boolean _run;
    protected DrawThread thread;
    private Bitmap mBitmap;

    private CommandManager commandManager;

    public DoodlingSurface(Context context, AttributeSet attrs) {
        super(context, attrs);

        getHolder().addCallback(this);

        commandManager = new CommandManager();
        thread = new DrawThread(getHolder());
    }

    class DrawThread extends Thread {
        private SurfaceHolder mSurfaceHolder;

        public DrawThread(SurfaceHolder surfaceHolder) {
            mSurfaceHolder = surfaceHolder;

        }

        public void setRunning(boolean run) {
            _run = run;
        }

        @Override
        public void run() {
            Canvas canvas = null;
            // canvas = mSurfaceHolder.lockCanvas(null);
            while (_run) {
                canvas = mSurfaceHolder.lockCanvas(null);
                try {
                    // canvas = mSurfaceHolder.lockCanvas(null);
                    if (mBitmap == null) {
                        mBitmap = Bitmap.createBitmap(1, 1,
                                Bitmap.Config.ARGB_8888);
                    }

                    final Canvas c = new Canvas(mBitmap);
                    c.drawColor(0, PorterDuff.Mode.CLEAR);
                    commandManager.executeAll(c);
                    canvas.drawBitmap(mBitmap, 0, 0, null);
                    // mSurfaceHolder.unlockCanvasAndPost(canvas);
                } catch (Exception e) {
                    // mSurfaceHolder.unlockCanvasAndPost(canvas);
                    e.printStackTrace();
                } finally {
                    try {
                        mSurfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        // mSurfaceHolder.unlockCanvasAndPost(canvas);
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    public void addDrawingPath(DoodlingPath drawingPath) {
        commandManager.addCommand(drawingPath);
    }

    public boolean hasMoreRedo() {
        return commandManager.hasMoreRedo();
    }

    public void redo() {
        commandManager.redo();
    }

    public void undo() {
        commandManager.undo();
    }

    public boolean hasMoreUndo() {
        return commandManager.hasMoreRedo();
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        ;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        thread.setRunning(true);
        thread.start();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                // we will try it again and again...
            }
        }
    }
}
