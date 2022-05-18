/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import View.HappyFrog;
import java.awt.Rectangle;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author MyPC
 */
public class Control{
    HappyFrog main;
    int point, width, height;
    double speed;
    boolean stop = false, pause;
    JPanel game;
    JLabel frog;
    JButton[] pipe = new JButton[6];
    Rectangle[] saver = new Rectangle[4];
    Key key;
    Random RANDOM = new Random();
    Thread move = new Thread(){
        @Override
        public void run() {
            while (!stop) {
                try {
                    sleep(15);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
                }
                for (int i = 0; i < 3; i++) {
                    if(pipe[i].getX()+50 < 0){
                        pipe[i].setBounds(width, 0, 50, RANDOM.nextInt(height-100));
                        pipe[i+3].setBounds(width, pipe[i].getHeight()+100, 50, height-100-pipe[i].getHeight());
                    } else{
                        pipe[i].setLocation(pipe[i].getX() - 1, pipe[i].getY());
                        pipe[i+3].setLocation(pipe[i+3].getX() - 1, pipe[i+3].getY());
                    }
                    if(pipe[i].getX() == frog.getX()){
                        main.getLabelPoint().setText("Points: " + ++point);
                    }
                }
                speed += 0.2;
                frog.setLocation(frog.getX(), (int) (frog.getY()+speed));
                for (int i = 0; i < 6; i++){
                    if (frog.getBounds().intersects(pipe[i].getBounds())){
                        endGame();
                    }
                }
                if (frog.getY() < 0 || frog.getY() + 50 > height){
                    endGame();
                }
            }
            stop = false;
        }
    };

    //Constructor
    public Control(HappyFrog Main) {
        key = new Key(this);
        main = Main;
        game = Main.getjPanelGame();
        width = game.getWidth();
        height = game.getHeight();
        try {
            frog = new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("frog.jpg"))));
        } catch (IOException ex) {
            Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
        }
        main.getPauseBtn().addKeyListener(key);
        main.getSaveBtn().addKeyListener(key);
        for (int i = 0; i < 6; i++){
            pipe[i] = new JButton();
        }
        if (new File("data.bin").exists() && JOptionPane.showConfirmDialog(null, "Do you want to continue?", "New game", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
            load();
        } else{
            newGame();
        }
    }
    
    //Create new game
    void newGame(){
        main.getPauseBtn().setText("Pause");
        pause = false;
        point = 0;
        speed = 0;
        main.getLabelPoint().setText("Points: " + point);
        frog.setBounds(width/3-20, height/2-20, 40, 40);
        game.add(frog);
        for (int i = 0; i < 3; i++){
            pipe[i].setBounds(width+i*(width+50)/3, 0, 50, RANDOM.nextInt(height-100));
            pipe[i+3].setBounds(width+i*(width+50)/3, pipe[i].getHeight()+100, 50, height-100-pipe[i].getHeight());
            game.add(pipe[i]);
            game.add(pipe[i+3]);
            pipe[i].setFocusable(false);
            pipe[i+3].setFocusable(false);
        }
        move = new Thread(){
        @Override
        public void run() {
            while (!stop) {
                try {
                    sleep(15);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
                }
                for (int i = 0; i < 3; i++) {
                    if(pipe[i].getX()+50 < 0){
                        pipe[i].setBounds(width, 0, 50, RANDOM.nextInt(height-100));
                        pipe[i+3].setBounds(width, pipe[i].getHeight()+100, 50, height-100-pipe[i].getHeight());
                    } else{
                        pipe[i].setLocation(pipe[i].getX() - 1, pipe[i].getY());
                        pipe[i+3].setLocation(pipe[i+3].getX() - 1, pipe[i+3].getY());
                    }
                    if(pipe[i].getX() == frog.getX()){
                        main.getLabelPoint().setText("Points: " + ++point);
                    }
                }
                speed += 0.2;
                frog.setLocation(frog.getX(), (int) (frog.getY()+speed));
                for (int i = 0; i < 6; i++){
                    if (frog.getBounds().intersects(pipe[i].getBounds())){
                        endGame();
                    }
                }
                if (frog.getY() < 0 || frog.getY() + 50 > height){
                    endGame();
                }
            }
            stop = false;
        }
    };
        move.start();
    }
    
    void endGame(){
        stop = true;
        String medal;
        if (point <= 10) {
            medal = "no";
        } else if (point < 20) {
            medal = "Bronze";
        } else if (point < 30){
            medal = "Sliver";
        } else if (point < 40){
            medal = "Goldd";
        } else{
            medal = "Platinum";
        }
        int option;
        if (!new File("data.bin").exists()){
            option = JOptionPane.showOptionDialog(main, "You god " + medal + " medal. Do you want to continue?", "Game Over"
                    , JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"New game", "Exit"}, "New game");
        } else{
            option = JOptionPane.showOptionDialog(main, "You god " + medal + " medal. Do you want to continue?", "Game Over"
                    , JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"New game", "Exit", "Continue"}, "New game");
        }
        switch (option) {
            case JOptionPane.YES_OPTION:
                newGame();
                break;
            case JOptionPane.CANCEL_OPTION:
                load();
                break;
            default:
                System.exit(0);
                break;
        }
    }
    
    public void pause(){
        if (!pause){
            move.suspend();
            pause = true;
            main.getPauseBtn().setText("Continue");
        } else{
            move.resume();
            pause = false;
            main.getPauseBtn().setText("Pause");
        }
    }
    
    public void save(){
        try {
            FileOutputStream fos = new FileOutputStream("data.bin");
            DataOutputStream dos = new DataOutputStream(fos);
            fos = new FileOutputStream("datapipe.bin");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            dos.writeInt(point);
            dos.writeInt(frog.getY());
            dos.writeDouble(speed);
            oos.writeObject(pipe);
            fos.close();
            dos.close();
            oos.close();
        } catch (Exception e) {
        }
    }
    
    void load(){
        try {
            main.getPauseBtn().setText("Pause");
            pause = false;
            game.removeAll();
            game.repaint();
            FileInputStream fis = new FileInputStream("data.bin");
            DataInputStream dis = new DataInputStream(fis);
            fis = new FileInputStream("datapipe.bin");
            ObjectInputStream ois = new ObjectInputStream(fis);
            main.getLabelPoint().setText("Points: " + (point = dis.readInt()));
            frog.setBounds(width / 3 - 20, dis.readInt(), 40, 40);
            speed = dis.readDouble();
            pipe = (JButton[]) ois.readObject();
            dis.close();
            ois.close();
            game.add(frog);
            for (int i = 0; i < 6; i++) {
                game.add(pipe[i]);
                pipe[i].setFocusable(false);
            }
            move = new Thread() {
                @Override
                public void run() {
                    while (!stop) {
                        try {
                            sleep(15);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        for (int i = 0; i < 3; i++) {
                            if (pipe[i].getX() + 50 < 0) {
                                pipe[i].setBounds(width, 0, 50, RANDOM.nextInt(height - 100));
                                pipe[i + 3].setBounds(width, pipe[i].getHeight() + 100, 50, height - 100 - pipe[i].getHeight());
                            } else {
                                pipe[i].setLocation(pipe[i].getX() - 1, pipe[i].getY());
                                pipe[i + 3].setLocation(pipe[i + 3].getX() - 1, pipe[i + 3].getY());
                            }
                            if (pipe[i].getX() == frog.getX()) {
                                main.getLabelPoint().setText("Points: " + ++point);
                            }
                        }
                        speed += 0.2;
                        frog.setLocation(frog.getX(), (int) (frog.getY() + speed));
                        for (int i = 0; i < 6; i++) {
                            if (frog.getBounds().intersects(pipe[i].getBounds())) {
                                endGame();
                            }
                        }
                        if (frog.getY() < 0 || frog.getY() + 50 > height) {
                            endGame();
                        }
                    }
                    stop = false;
                }
            };
            move.start();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Load file error");
            System.exit(0);
        }
    }
    
    public void exit(){
        move.suspend();
        pause = true;
        main.getPauseBtn().setText("Continue");
        if (JOptionPane.showConfirmDialog(main, "Do you want to exit?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
            System.exit(0);
        }
    }
}