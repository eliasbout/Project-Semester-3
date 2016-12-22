/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.power;

import java.awt.Rectangle;
import java.io.Serializable;
import model.Character;

/**
 *
 * @author Tobias
 */
public class FireRateBoost implements Power, Serializable {

    private model.Character c;
    private int extraFireRate;
    private long startTime;
    private int duration;
    private boolean pickedUp;
    private int posX;
    private int posY;
    private String name = "Fire rate boost";

    public FireRateBoost(int posX, int posY) {
        extraFireRate = 25;
        this.posX = posX;
        this.posY = posY;
        this.duration = 20000;
    }

    @Override
    public void start(model.Character c) {
        this.pickedUp = true;
        startTime = System.currentTimeMillis();
        this.c = c;
        this.c.increaseFirerate(extraFireRate);
    }

    private void end() {

        c.decreaseFireRate(extraFireRate);
    }

    @Override
    public boolean isTheEnd() {
        if (pickedUp) {
            if ((startTime + duration) <= System.currentTimeMillis()) {
                end();
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isPickedUp() {
        return this.pickedUp;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(posX - (20 / 2), posY - (20 / 2), 20, 20);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getTimeLeft() {
        return (int) (startTime + duration - System.currentTimeMillis()) / 1000;
    }
}