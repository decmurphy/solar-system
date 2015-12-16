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
public final class Moon extends Body {

    private static final Body singleton = new Moon(1737100, 7.342e22);

    private Moon(double radius, double mass) {
        super(radius, mass);
    }

    public static Body get() {
        return singleton;
    }

    @Override
    public String getName() {
        return "Moon";
    }

}
