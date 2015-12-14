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

import static com.solarsystem.Main.G;
import com.solarsystem.bodies.Body;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
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
            body.getVel()[0] += body.getAccel()[0] * dt / 2;
            body.getVel()[2] += body.getAccel()[2] * dt / 2;
        }
    }

    public void leapfrog(double dt) {

        for (Body body : bodies) {

            body.getPos()[0] += body.getVel()[0] * dt;
            body.getPos()[2] += body.getVel()[2] * dt;

            // set accels to zero again
            System.arraycopy(new double[3], 0, body.getAccel(), 0, 3);
        }

        updateAcceleration();

        for (Body body : bodies) {
            body.getVel()[0] += body.getAccel()[0] * dt;
            body.getVel()[2] += body.getAccel()[2] * dt;
        }

    }

    private void updateAcceleration() {

        Body star = null;
        // The star in the system interacts with all bodies
        for (Body body : bodies) {

            // Find the star and set accelerations for all bodies
            if (body.isOrbiting(null)) {
                star = body;
                for (Body otherBody : bodies) {

                    if (body.equals(otherBody)) {
                        continue;
                    }

                    double Rx = otherBody.getPos()[0] - body.getPos()[0];
                    double Rz = otherBody.getPos()[2] - body.getPos()[2];
                    double psi = atan2(Rz, Rx);
                    double R_sq = Rx * Rx + Rz * Rz;

                    double A = G * otherBody.getMass() / R_sq;

                    body.getAccel()[0] += A * cos(psi);
                    body.getAccel()[2] += A * sin(psi);

                    A *= body.getMass() / otherBody.getMass();

                    otherBody.getAccel()[0] -= A * cos(psi);
                    otherBody.getAccel()[2] -= A * sin(psi);
                }
                break;
            }

        }
        // All other bodies only interact with their parent.
        // If their parent is the star, this has already been done
        for (Body body : bodies) {

            if (!body.equals(star) && !body.isOrbiting(star)) {

                Body otherBody = body.getParent();

                double Rx = otherBody.getPos()[0] - body.getPos()[0];
                double Rz = otherBody.getPos()[2] - body.getPos()[2];
                double psi = atan2(Rz, Rx);
                double R_sq = Rx * Rx + Rz * Rz;

                double A = G * otherBody.getMass() / R_sq;

                body.getAccel()[0] += A * cos(psi);
                body.getAccel()[2] += A * sin(psi);

                A *= body.getMass() / otherBody.getMass();

                otherBody.getAccel()[0] -= A * cos(psi);
                otherBody.getAccel()[2] -= A * sin(psi);

            }
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
