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
package com.solarsystem.bodies.planets;

import com.solarsystem.bodies.Body;

/**
 *
 * @author declan
 */
public final class Neptune extends Body {

    private static final Body singleton = new Neptune(24622, 1.0243e26);

    private Neptune(double radius, double mass) {
        super(radius, mass);
    }

    public static Body get() {
        return singleton;
    }

    @Override
    public String getName() {
        return "Neptune";
    }
    
    @Override
    public double[] getRGB() {
        return new double[]{0.0, 0.0, 1.0};
    }

}