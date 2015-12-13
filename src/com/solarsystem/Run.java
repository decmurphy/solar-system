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
import com.solarsystem.bodies.stars.Sol;
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
    static double dt = 60;

    public static void main(String[] args) {

        List<Body> bodies = new ArrayList();
        bodies.add(Sol.get());
        bodies.add(Earth.get());
        bodies.add(Moon.get());
        bodies.add(Mars.get());

        init(bodies);

        int t = 0;
        leapfrogFirstStep(bodies);
        do {

            leapfrog(bodies);
            if(t%(60*60*12)==0)
                output(bodies);

            t += dt;

        } while (t < 1.1 * 31536000);

    }

    public static void init(List<Body> bodies) {

        for (Body body : bodies) {
            
            // delete existing data files
            new File(body.getName() + ".dat").delete();

            if (body instanceof Sol) {

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

    public static void leapfrogFirstStep(List<Body> bodies) {

        for (Body body : bodies) {

            double[] accel = new double[3];
            double[] pos = new double[3];

            System.arraycopy(body.getPos(), 0, pos, 0, 3);
            for (Body otherBody : bodies) {

                if (otherBody.equals(body)) {
                    continue;
                }

                // get angles betwee bodies
                double theta = PI/2.0;
                double psi = atan2(
                        otherBody.getPos()[2] - body.getPos()[2],
                        otherBody.getPos()[0] - body.getPos()[0]
                );

                // get magnitude of accel
                double X = sqrt(pow(pos[0] - otherBody.getPos()[0], 2)
                        + pow(pos[1] - otherBody.getPos()[1], 2)
                        + pow(pos[2] - otherBody.getPos()[2], 2)
                );
                double A = G * otherBody.getMass() / pow(X, 2);

                // break down accel
                accel[0] += A * sin(theta) * cos(psi);
                accel[1] += A * cos(theta);
                accel[2] += A * sin(theta) * sin(psi);

            }

            pos[0] += body.getVel()[0] * dt / 2 + accel[0] * dt * dt / 4;
            pos[1] += body.getVel()[1] * dt / 2 + accel[1] * dt * dt / 4;
            pos[2] += body.getVel()[2] * dt / 2 + accel[2] * dt * dt / 4;

            body.setPos(pos);

        }
        for (Body body : bodies) {
            body.applyChanges();
        }
    }

    public static void leapfrog(List<Body> bodies) {

        for (Body body : bodies) {

            double[] pos = new double[3];
            double[] vel = new double[3];
            double[] accel = new double[3];
            System.arraycopy(body.getPos(), 0, pos, 0, 3);
            System.arraycopy(body.getVel(), 0, vel, 0, 3);

            for (Body otherBody : bodies) {

                if (otherBody.equals(body)) {
                    continue;
                }

                // get angles betwee bodies
                double theta = PI/2.0;
                double psi = atan2(
                        otherBody.getPos()[2] - body.getPos()[2],
                        otherBody.getPos()[0] - body.getPos()[0]
                );

                // get magnitude of accel
                double X = sqrt(pow(pos[0] - otherBody.getPos()[0], 2)
                        + pow(pos[1] - otherBody.getPos()[1], 2)
                        + pow(pos[2] - otherBody.getPos()[2], 2)
                );
                double A = G * otherBody.getMass() / pow(X, 2);

                accel[0] += A * sin(theta) * cos(psi);
                accel[1] += A * cos(theta);
                accel[2] += A * sin(theta) * sin(psi);

            }

            vel[0] += accel[0] * dt;
            vel[1] += accel[1] * dt;
            vel[2] += accel[2] * dt;

            pos[0] += vel[0] * dt;
            pos[1] += vel[1] * dt;
            pos[2] += vel[2] * dt;

            body.setPos(pos);
            body.setVel(vel);
        }
        for (Body body : bodies) {
            body.applyChanges();
        }

    }

    public static void output(List<Body> bodies) {

        for (Body body : bodies) {

            PrintWriter pw = null;
            try {
                String fileName = body.getName() + ".dat";
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
