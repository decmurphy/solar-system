/*
 This file is part of FlightClub.

 Copyright © 2014-2015 Declan Murphy

 FlightClub is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 FlightClub is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with FlightClub.  If not, see <http://www.gnu.org/licenses/>.
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

    public Body(double radius, double mass) {
        this.pos = new double[3];
        this.vel = new double[3];
        this.accel = new double[3];
        this.radius = radius;
        this.mass = mass;
    }

    public final void setPos(double[] pos) {
        System.arraycopy(pos, 0, this.pos, 0, pos.length);
    }

    public final void setVel(double[] vel) {
        System.arraycopy(vel, 0, this.vel, 0, vel.length);
    }

    public final void setAccel(double[] accel) {
        System.arraycopy(accel, 0, this.accel, 0, accel.length);
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

    public final void draw() throws IOException {
        File outputFile = new File("/" + getName() + ".output.txt");
        PrintWriter pw = new PrintWriter(new FileWriter(outputFile, false));
        double x, y, z;
        double theta, psi, dt = PI / 36, dp = PI / 36;

        for (theta = 0; theta < PI; theta += dt) {
            z = radius * cos(theta);
            for (psi = 0; psi < 2 * PI; psi += dp) {
                x = radius * sin(theta) * cos(psi);
                y = radius * sin(theta) * sin(psi);

                pw.print((pos[0] + x) * 1e-3 + "\t" + (pos[1] + y) * 1e-3 + "\t" + (pos[2] + z) * 1e-3 + "\n");
            }
            pw.print("\n");
        }
    }
}
