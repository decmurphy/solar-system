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
package com.solarsystem;

import com.solarsystem.bodies.Body;
import com.solarsystem.bodies.planets.Earth;
import com.solarsystem.bodies.planets.Jupiter;
import com.solarsystem.bodies.planets.Mars;
import com.solarsystem.bodies.planets.Mercury;
import com.solarsystem.bodies.planets.Moon;
import com.solarsystem.bodies.planets.Neptune;
import com.solarsystem.bodies.planets.Pluto;
import com.solarsystem.bodies.planets.Saturn;
import com.solarsystem.bodies.planets.Uranus;
import com.solarsystem.bodies.planets.Venus;
import com.solarsystem.bodies.stars.Sun;
import java.io.File;

/**
 *
 * @author declan
 */
public class Sol extends StarSystem {

    private static final StarSystem SINGLETON = new Sol();

    public static StarSystem get() {
        return SINGLETON;
    }

    private Sol() {
        addBody(Sun.get());
        addBody(Mercury.get());
        addBody(Venus.get());
        addBody(Earth.get());
        addBody(Moon.get());
        addBody(Mars.get());
        addBody(Jupiter.get());
        addBody(Saturn.get());
        addBody(Uranus.get());
        addBody(Neptune.get());
        addBody(Pluto.get());
    }

    @Override
    public String getName() {
        return "Sol";
    }

    @Override
    public void init() throws Exception {

        for (Body body : getBodies()) {

            // delete any existing data files
            if (!new File("output").mkdir()) {
                new File("output/" + body.getName() + ".dat").delete();
                new File("output/" + body.getName() + ".output.txt").delete();
            }

            if (body.getParent() == null) {
                body.setPos(new double[]{0, 0, 0});
                body.setVel(new double[]{0, 0, 0});
            } else {
                body.setAtPerihelion();
            }

        }

    }

}
