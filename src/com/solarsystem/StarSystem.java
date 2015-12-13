/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solarsystem;

import static com.solarsystem.Run.G;
import com.solarsystem.bodies.Body;
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
public abstract class StarSystem {
    
    private final List<Body> bodies;
    public StarSystem() {
        this.bodies = new ArrayList();
    }
    
    public final List<Body> getBodies() {
        return bodies;
    }
    
    public final void addBody(Body b) {
        if(!bodies.contains(b))
            bodies.add(b);
    }
    public final void removeBody(Body b) {
        bodies.remove(b);
    }
    
    public abstract void init();
    public abstract String getName();
    
    public void leapfrogFirstStep(double dt) {
        
        for (int i=0;i<bodies.size();i++) {
            
            Body body = bodies.get(i);

            double[] accel1 = new double[3];
            double[] pos = new double[3];

            System.arraycopy(body.getPos(), 0, pos, 0, 3);
            for (int j=i+1;j<bodies.size();j++) {
                
                Body otherBody = bodies.get(j);
                
                double[] accel2 = new double[3];
                System.arraycopy(otherBody.getAccel(), 0, accel2, 0, 3);

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
                accel1[0] += A * sin(theta) * cos(psi);
                accel1[1] += A * cos(theta);
                accel1[2] += A * sin(theta) * sin(psi);

                A *= body.getMass()/otherBody.getMass();
                accel2[0] -= A * sin(theta) * cos(psi);
                accel2[1] -= A * cos(theta);
                accel2[2] -= A * sin(theta) * sin(psi);
                otherBody.setAccel(accel2);

            }

            pos[0] += body.getVel()[0] * dt / 2 + accel1[0] * dt * dt / 4;
            pos[1] += body.getVel()[1] * dt / 2 + accel1[1] * dt * dt / 4;
            pos[2] += body.getVel()[2] * dt / 2 + accel1[2] * dt * dt / 4;

            body.setPos(pos);

        }
        for (Body body : bodies) {
            body.applyChanges();
        }
    }
    public void leapfrog(double dt) {
        
        // set accels to zero again
        for (Body body : bodies) {
            System.arraycopy(new double[3], 0, body.getAccel(), 0, 3);            
        }
        
        for (int i=0;i<bodies.size();i++) {
            
            Body body = bodies.get(i);

            double[] pos = new double[3];
            double[] vel = new double[3];
            double[] accel1 = new double[3];
            System.arraycopy(body.getPos(), 0, pos, 0, 3);
            System.arraycopy(body.getVel(), 0, vel, 0, 3);
            System.arraycopy(body.getAccel(), 0, accel1, 0, 3);

            for (int j=i+1;j<bodies.size();j++) {
                
                Body otherBody = bodies.get(j);
                
                double[] accel2 = new double[3];
                System.arraycopy(otherBody.getAccel(), 0, accel2, 0, 3);

                // get angles between bodies
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

                accel1[0] += A * sin(theta) * cos(psi);
                accel1[1] += A * cos(theta);
                accel1[2] += A * sin(theta) * sin(psi);

                A *= body.getMass()/otherBody.getMass();
                accel2[0] -= A * sin(theta) * cos(psi);
                accel2[1] -= A * cos(theta);
                accel2[2] -= A * sin(theta) * sin(psi);
                otherBody.setAccel(accel2);
            }
            
            vel[0] += accel1[0] * dt;
            vel[1] += accel1[1] * dt;
            vel[2] += accel1[2] * dt;

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
