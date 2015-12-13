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

/**
 *
 * @author declan
 */
public class Run {

    static double G = 6.67384e-11;

    public static void main(String[] args) {

        StarSystem sol = Sol.get();
        sol.init();

        int t = 0;
        double dt = 60 * 60;
        sol.leapfrogFirstStep(dt);
        do {

            sol.leapfrog(dt);
            if (t % (60 * 60 * 12) == 0) {
                sol.output();
            }

            t += dt;

        } while (t < 1.1 * 31536000);

    }

}
