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

import com.solarsystem.bodies.Body;
import com.solarsystem.bodies.planets.Earth;
import com.solarsystem.bodies.planets.Mars;
import com.solarsystem.bodies.planets.Moon;
import com.solarsystem.bodies.stars.Sun;
import java.io.File;

/**
 *
 * @author declan
 */
public class Sol extends StarSystem {

    private static final StarSystem singleton = new Sol();

    public static StarSystem get() {
        return singleton;
    }

    private Sol() {
        addBody(Sun.get());
        addBody(Earth.get());
        addBody(Moon.get());
        addBody(Mars.get());
    }

    @Override
    public String getName() {
        return "Sol";
    }

    @Override
    public void init() throws Exception {

        for (Body body : getBodies()) {

            // delete any existing data files
            if(!new File("output").mkdir()) {
                new File("output/" + body.getName() + ".dat").delete();
                new File("output/" + body.getName() + ".output.txt").delete();
            }

            if (body instanceof Sun) {

                body.setOrbiting(null);
                body.setPos(new double[]{0, 0, 0});
                body.setVel(new double[]{0, 0, 0});

            } else if (body instanceof Earth) {

                body.setOrbiting(Sun.get());
                body.setOrbit(147.095e9, 152.1e9);
                body.setAtPerihelion();

            } else if (body instanceof Moon) {

                body.setOrbiting(Earth.get());
                body.setOrbit(362.6e6, 405.4e6);
                body.setAtPerihelion();

            } else if (body instanceof Mars) {

                body.setOrbiting(Sun.get());
                body.setOrbit(206.7e9, 249.2e9);
                body.setAtPerihelion();

            }

        }

    }

}
