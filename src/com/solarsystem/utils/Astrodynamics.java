/*
 This file is part of FlightClub.

 Copyright © 2014-2017 Declan Murphy

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
package com.solarsystem.utils;

import static java.lang.Math.sqrt;

/**
 *
 * @author dmurphy
 */
public class Astrodynamics {

    public static double G = 6.67384e-11;

    public static double getVelocityAtDistance(double M, double r, double a) {

        double mu = G * M;
        double v_sq = mu * ((2.0 / r) - (1.0 / a));
        return sqrt(v_sq);

    }

    public static double getSemiMajorAxis(double M, double r, double v) {

        double mu = G * M;
        return mu * r / (2 * mu - r * v * v);

    }
    
    public static long getSeconds(double days, double hours, double minutes, double seconds) {
        return (long) (
                    days*24*60*60 +
                    hours*60*60 +
                    minutes*60 +
                    seconds
                );
    }

}
