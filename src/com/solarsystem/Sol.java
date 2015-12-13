/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solarsystem;

import com.solarsystem.bodies.Body;
import com.solarsystem.bodies.planets.Earth;
import com.solarsystem.bodies.planets.Mars;
import com.solarsystem.bodies.planets.Moon;
import com.solarsystem.bodies.stars.Sun;
import java.io.File;

/**
 *
 * @author declan
 */
public class Sol extends StarSystem {
    
    private static final StarSystem singleton = new Sol();
    public static StarSystem get() {
        return singleton;
    }
    
    private Sol() {
        addBody(Sun.get());
        addBody(Earth.get());
        addBody(Moon.get());
        addBody(Mars.get());
    }
    
    @Override
    public String getName() {
        return "Sol";
    }
    
    @Override
    public void init() {

        for (Body body : getBodies()) {
            
            // delete existing data files
            new File(body.getName() + ".dat").delete();

            if (body instanceof Sun) {

                body.setPos(new double[]{0, 0, 0});
                body.setVel(new double[]{0, 0, 0});

            } else if (body instanceof Earth) {

                body.setPos(new double[]{149.598023e9, 0, 0});
                body.setVel(new double[]{0, 0, -29.78e3});

            } else if (body instanceof Moon) {

                body.setPos(new double[]{Earth.get().getPos()[0] + 384399e3, 0, 0});
                body.setVel(new double[]{0, 0, Earth.get().getVel()[2] - 1.022e3});

            } else if (body instanceof Mars) {

                body.setPos(new double[]{227.939200e9, 0, 0});
                body.setVel(new double[]{0, 0, -24.077e3});

            }

            body.applyChanges();

        }

    }
    
    
}
