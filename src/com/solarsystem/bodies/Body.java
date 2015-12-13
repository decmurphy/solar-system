/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solarsystem.bodies;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 *
 * @author declan
 */
public abstract class Body {
    
    private final double[] pos;
    private final double[] vel;
    private final double[] accel;
    private final double radius;
    private final double mass;
    private double energy;
    
    private final double[] tempPos;
    private final double[] tempVel;
    
    public Body(double radius, double mass) {
        this.pos = new double[3];
        this.vel = new double[3];
        this.accel = new double[3];
        this.radius = radius;
        this.mass = mass;
        this.tempPos = new double[3];
        this.tempVel = new double[3];
    }

    public final void setPos(double[] pos) {
        System.arraycopy(pos, 0, this.tempPos, 0, pos.length);
    }

    public final void setVel(double[] vel) {
        System.arraycopy(vel, 0, this.tempVel, 0, vel.length);
    }

    public final void setAccel(double[] accel) {
        System.arraycopy(accel, 0, this.accel, 0, accel.length);
    }
    
    public final void applyChanges() {
        System.arraycopy(tempPos, 0, pos, 0, pos.length);
        System.arraycopy(tempVel, 0, vel, 0, vel.length);        
    }
    
    public final void setEnergy(double e) {
        this.energy = e;
    }

    public final double[] getPos() {
        return pos;
    }
    public final double[] getVel() {
        return vel;
    }
    public final double[] getAccel() {
        return accel;
    }
    public final double getRadius() {
        return radius;
    }
    public final double getMass() {
        return mass;
    }
    public final double getEnergy() {
        return energy;
    }
    
    public abstract String getName();
    
    public final void draw() throws IOException
    {
        File outputFile = new File("/" + getName() + ".output.txt");        
        PrintWriter pw = new PrintWriter(new FileWriter(outputFile, false));
        double x, y, z;
        double theta, psi, dt = PI / 36, dp = PI / 36;

        for (theta = 0; theta < PI; theta += dt) {
            z = radius * cos(theta);
            for (psi = 0; psi < 2 * PI; psi += dp) {
                x = radius * sin(theta) * cos(psi);
                y = radius * sin(theta) * sin(psi);

                pw.print((pos[0]+x)*1e-3 + "\t" + (pos[1]+y)*1e-3 + "\t" + (pos[2]+z)*1e-3 + "\n");
            }
            pw.print("\n");
        }
    }
}
