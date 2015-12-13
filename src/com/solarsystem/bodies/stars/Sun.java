/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solarsystem.bodies.stars;

import com.solarsystem.bodies.Body;

/**
 *
 * @author declan
 */
public class Sun extends Body {
    
    private static final Sun singleton = new Sun(696342e3, 1.98855e30);
    private Sun(double radius, double mass) {
        super(radius, mass);
    }
    
    public static Body get() {
        return singleton;
    }
    
    @Override
    public String getName() {
        return "Sun";
    }
    
}
