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
package io.flightclub.solarsystem.bodies;

import lombok.Data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import static io.flightclub.solarsystem.utils.Astrodynamics.G;
import static io.flightclub.solarsystem.utils.Astrodynamics.getVelocityAtDistance;
import static java.lang.Math.*;

/**
 * @author declan
 */
@Data
public abstract class Body extends OrbitingObject {

	private final double radius;
	private final double mass;

	private double energy;
	private double angVel;

	public Body(String name, double radius, double mass, long period) {
		super(name);
		this.radius = radius;
		this.mass = mass;
		this.angVel = 2 * PI / period;
	}

	public double getGravitationalConstant() {
		return G * mass / (radius * radius);
	}

	public abstract double[] getRGB();

	// draws the actual sphere of the planet, not any kind of trajectory
	public final void draw() throws IOException {

		PrintWriter pw = null;
		try {
			File outputFile = new File("output/" + getName() + ".draw.dat");

			pw = new PrintWriter(new FileWriter(outputFile, true));

			double x, y, z;
			double theta, psi, dt = PI / 36, dp = PI / 36;

			for (theta = 0; theta < PI; theta += dt) {
				z = radius * cos(theta);
				for (psi = 0; psi < 2 * PI; psi += dp) {
					x = radius * sin(theta) * cos(psi);
					y = radius * sin(theta) * sin(psi);

					pw.print((getPos()[0] + x) + ";" + (getPos()[1] + y) + ";" + (getPos()[2] + z) + "\n");
				}
				pw.print("\n");
			}
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	}

	public double gravityAtAltitude(double alt) {
		return G * mass / pow(alt + radius, 2);
	}

	public double gravityAtRadius(double r) {
		r = max(radius, r);
		return G * mass / pow(r, 2);
	}

	public abstract double densityAtAltitude(double altitude);

	public abstract double pressureAtAltitude(double altitude);

	public abstract double speedOfSoundAtAltitude(double altitude);
}
