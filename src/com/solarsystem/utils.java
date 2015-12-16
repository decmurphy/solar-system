package com.solarsystem;

import static com.solarsystem.Main.G;
import com.solarsystem.bodies.stars.Sun;
import static java.lang.Math.sqrt;

/**
 *
 * @author dmurphy
 */
public class utils {
    
    public static void main(String[] args) {
        
        double vel = getVelocityAtDistance(Sun.get().getMass(), 147.095e9, 149.598023e9);
        System.out.println(vel);
    }
    
    public static double getVelocityAtDistance(double M, double r, double a) {
        
        double mu = G*M;
        double v_sq = mu*((2.0/r) - (1.0/a));
        return sqrt(v_sq);
        
    }
    

}
