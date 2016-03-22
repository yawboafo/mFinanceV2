package com.nfortics.mfinanceV2.Signature;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Bernard Adjei Oppong.
 * Date: 08/04/2012
 * Time: 10:34 AM
 */
public class CommandManager {
    private List<DoodlingPath> currentStack;
    private List<DoodlingPath> redoStack;

    public  CommandManager(){
        currentStack = Collections.synchronizedList(new ArrayList<DoodlingPath>());
        redoStack = Collections.synchronizedList(new ArrayList<DoodlingPath>());
    }

    public void addCommand(DoodlingPath command){
        redoStack.clear();
        currentStack.add(command);
    }

    public void undo (){
        final int length = currentStackLength();
        if ( length > 0) {
            final DoodlingPath undoCommand = currentStack.get(  length - 1  );
            currentStack.remove( length - 1 );
            undoCommand.undo();
            redoStack.add( undoCommand );
        }
    }

    public int currentStackLength(){
        final int length = currentStack.toArray().length;
        return length;
    }


    public void executeAll( Canvas canvas){
        if( currentStack != null ){
            synchronized( currentStack ) {
                final Iterator i = currentStack.iterator();
                while ( i.hasNext() ){
                    final DoodlingPath drawingPath = (DoodlingPath) i.next();
                    drawingPath.draw( canvas );
                }
            }
        }
    }



    public boolean hasMoreRedo(){
        return  redoStack.toArray().length > 0;
    }

    public boolean hasMoreUndo(){
        return  currentStack.toArray().length > 0;
    }

    public void redo(){
        final int length = redoStack.toArray().length;
        if ( length > 0) {
            final DoodlingPath redoCommand = redoStack.get(  length - 1  );
            redoStack.remove( length - 1 );
            currentStack.add( redoCommand );
        }
    }
}
