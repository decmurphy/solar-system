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
package io.flightclub.solarsystem;

import io.flightclub.solarsystem.starsystem.Sol;
import io.flightclub.solarsystem.starsystem.StarSystem;

import java.util.Calendar;

import static io.flightclub.solarsystem.utils.Astrodynamics.getSeconds;

/**
 * @author declan
 */
public class Main {

	public static void getSystemState(StarSystem system, long millisStart, long millisEnd) throws Exception {
		getSystemState(system, millisStart, millisEnd, 60, 1, 0, 0, 0);
	}

	public static void getSystemState(StarSystem system, long millisStart, long millisEnd,
	                                  double dt, double outputDay, double outputHour, double outputMin, double outputSec) throws Exception {

		StarSystem sol = Sol.get();

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(millisStart);
		int year = c.get(Calendar.YEAR);
		sol.init(year);

		long secondsStart = millisStart / 1000;
		long secondsEnd = millisEnd / 1000;

		long t = secondsStart;
		sol.leapfrogFirstStep(dt);
		do {

			sol.leapfrog(dt);
			if (t > secondsStart && t % (getSeconds(outputDay, outputHour, outputMin, outputSec)) == 0) {
				sol.output(t);
			}

			t += dt;

		} while (t < secondsEnd);

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

	public static void main(String[] args) {

		int year = 1970;
		if (args.length > 0)
			year = Integer.parseInt(args[0]);

		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.set(year, 0, 1, 0, 0, 0);
		end.set(year + 1, 0, 1, 0, 0, 0);

		try {
			getSystemState(Sol.get(), start.getTimeInMillis(), end.getTimeInMillis());
		} catch (Exception e) {
			System.out.println(e);
			System.exit(-1);
		}
	}

}
