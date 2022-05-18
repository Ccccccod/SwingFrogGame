/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author MyPC
 */
class Key implements KeyListener{
    Control control;

    public Key(Control control) {
        this.control = control;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP && !control.pause){
            control.speed = -4;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
