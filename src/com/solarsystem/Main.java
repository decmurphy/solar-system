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

import com.solarsystem.starsystem.Sol;
import com.solarsystem.starsystem.StarSystem;
import static com.solarsystem.utils.Astrodynamics.getSeconds;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author declan
 */
public class Main {

    public static void main(String[] args) {

        int year = 1970;
        if(args.length>0)
            year = Integer.parseInt(args[0]);
        
        StarSystem sol = Sol.get();
        try {
            sol.init(year);
        } catch (Exception e) {
            System.out.println(e);
            System.exit(-1);
        }

        int t = 0;
        double dt = 60;
        sol.leapfrogFirstStep(dt);
        do {

            sol.leapfrog(dt);
            if (t % (getSeconds(0, 0, 10, 0)) == 0) {
                sol.output();
            }

            t += dt;

        } while (t < getSeconds(365, 0, 0, 0));

        /*
        for (Body body : sol.getBodies()) {
            try {
                body.draw();
            } catch (IOException e) {
                System.err.println("Couldn't draw " + body.getName());
                System.err.println(e.getMessage());
            }
        }
        */

    }

}
