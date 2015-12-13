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
package com.solarsystem;

import static com.solarsystem.Run.G;
import com.solarsystem.bodies.Body;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author declan
 */
public abstract class StarSystem {

    private final List<Body> bodies;

    public StarSystem() {
        this.bodies = new ArrayList();
    }

    public final List<Body> getBodies() {
        return bodies;
    }

    public final void addBody(Body b) {
        if (!bodies.contains(b)) {
            bodies.add(b);
        }
    }

    public final void removeBody(Body b) {
        bodies.remove(b);
    }

    public abstract void init();

    public abstract String getName();

    public void leapfrogFirstStep(double dt) {
        
        updateAcceleration();
        
        for (Body body : bodies) {

            double[] vel = new double[3];
            System.arraycopy(body.getVel(), 0, vel, 0, 3);
            vel[0] += body.getAccel()[0] * dt / 2;
            vel[2] += body.getAccel()[2] * dt / 2;
            body.setVel(vel);
        }
    }

    public void leapfrog(double dt) {

        double[] pos = new double[3];
        for (Body body : bodies) {
            
            System.arraycopy(body.getPos(), 0, pos, 0, 3);
            pos[0] += body.getVel()[0] * dt;
            pos[2] += body.getVel()[2] * dt;
            body.setPos(pos);
            
            // set accels to zero again
            System.arraycopy(new double[3], 0, body.getAccel(), 0, 3);
        }
        
        updateAcceleration();

        double[] vel = new double[3];
        for (Body body : bodies) {
            
            System.arraycopy(body.getVel(), 0, vel, 0, 3);
            vel[0] += body.getAccel()[0] * dt;
            vel[2] += body.getAccel()[2] * dt;
            body.setVel(vel);
        }

    }
    
    private void updateAcceleration() {
                
        double[] accel1 = new double[3];
        for (int i = 0; i < bodies.size(); i++) {

            Body body = bodies.get(i);
            
            System.arraycopy(body.getAccel(), 0, accel1, 0, 3);
            for (int j = i + 1; j < bodies.size(); j++) {

                Body otherBody = bodies.get(j);

                double psi = atan2(otherBody.getPos()[2] - body.getPos()[2], otherBody.getPos()[0] - body.getPos()[0]);
                double R_sq = pow(body.getPos()[0] - otherBody.getPos()[0], 2) + pow(body.getPos()[2] - otherBody.getPos()[2], 2);
                double A = G * otherBody.getMass() / R_sq;

                accel1[0] += A * cos(psi);
                accel1[2] += A * sin(psi);

                A *= body.getMass() / otherBody.getMass();

                double[] accel2 = new double[3];
                System.arraycopy(otherBody.getAccel(), 0, accel2, 0, 3);
                accel2[0] -= A * cos(psi);
                accel2[2] -= A * sin(psi);
                otherBody.setAccel(accel2);

            }
            body.setAccel(accel1);
        }
    }

    public void output() {

        for (Body body : bodies) {

            PrintWriter pw = null;
            try {
                String fileName = "output/" + body.getName() + ".dat";
                File outputFile = new File(fileName);

                pw = new PrintWriter(new FileWriter(outputFile, true));
                pw.printf("%9.3f\t%9.3f\t%9.3f\n", body.getPos()[0], body.getPos()[1], body.getPos()[2]);

            } catch (IOException e) {
                int x = 5;
            } finally {
                if (pw != null) {
                    pw.close();
                }
            }

        }
    }

}
