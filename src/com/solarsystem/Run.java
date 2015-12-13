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
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Math.PI;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author declan
 */
public class Run {

    static double G = 6.67384e-11;
    public static void main(String[] args) {

        StarSystem sol = Sol.get();
        sol.init();

        int t = 0;
        double dt = 60*60;
        sol.leapfrogFirstStep(dt);
        do {

            sol.leapfrog(dt);
            if(t%(60*60*12)==0)
                sol.output();

            t += dt;

        } while (t < 1.1 * 31536000);

    }

}
