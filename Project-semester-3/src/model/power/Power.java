/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.power;

import model.Character;

/**
 *
 * @author Maxime
 */
public interface Power {
    
    public void start(Character c);
    public boolean isTheEnd();
}