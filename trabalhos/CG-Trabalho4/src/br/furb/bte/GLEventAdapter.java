package br.furb.bte;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

public interface GLEventAdapter extends GLEventListener {

    @Override
    default void display(GLAutoDrawable arg) {
    }

    @Override
    default void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
    }

    @Override
    default void init(GLAutoDrawable arg0) {
    }

    @Override
    default void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
    }
}
