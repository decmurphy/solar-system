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
package com.solarsystem.bodies.stars;

import com.solarsystem.bodies.Body;
import static com.solarsystem.utils.Astrodynamics.getSeconds;

/**
 *
 * @author declan
 */
public class Sun extends Body {

    private static final Body singleton = new Sun(696342e3, 1.98855e30, getSeconds(24.47, 0, 0, 0));

    private Sun(double radius, double mass, long period) {
        super(radius, mass, period);
    }

    public static Body get() {
        return singleton;
    }

    @Override
    public String getName() {
        return "Sun";
    }
    
    @Override
    public double[] getRGB() {
        return new double[]{1.0, 1.0, 1.0};
    }

    @Override
    public double densityAtAltitude(double altitude) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double pressureAtAltitude(double altitude) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double speedOfSoundAtAltitude(double altitude) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
