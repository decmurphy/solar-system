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
package io.flightclub.solarsystem.starsystem;

import io.flightclub.solarsystem.bodies.Body;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static io.flightclub.solarsystem.utils.Astrodynamics.G;
import static java.lang.Math.*;

/**
 * @author declan
 */
public abstract class StarSystem {

	private final List<Body> bodies;

	public StarSystem() {
		this.bodies = new ArrayList();
	}

	public final List<Body> getBodies() {
		return bodies;
	}

	public final void addBody(Body b) {
		if (!bodies.contains(b)) {
			bodies.add(b);
		}
	}

	public final void removeBody(Body b) {
		bodies.remove(b);
	}

	public abstract void init(int year) throws Exception;

	public abstract String getName();

	public void leapfrogFirstStep(double dt) {

		updateAcceleration();

		for (Body body : bodies) {
			for (int i = 0; i < 3; i++) {
				body.getVel()[i] += body.getAccel()[i] * dt / 2;
			}
		}
	}

	public void leapfrog(double dt) {

		for (Body body : bodies) {
			for (int i = 0; i < 3; i++) {
				body.getPos()[i] += body.getVel()[i] * dt;
			}
		}

		updateAcceleration();

		for (Body body : bodies) {
			for (int i = 0; i < 3; i++) {
				body.getVel()[i] += body.getAccel()[i] * dt;
			}
		}

	}

	private void updateAcceleration() {

		for (Body body : bodies) {
			// set accels to zero again
			System.arraycopy(new double[3], 0, body.getAccel(), 0, 3);
		}

		Body star = null;
		// The star in the system interacts with all io.flightclub.solarsystem.bodies
		for (Body body : bodies) {

			// Find the star and set accelerations for all io.flightclub.solarsystem.bodies
			if (body.isOrbiting(null)) {
				star = body;
				for (Body child : bodies) {

					if (star.equals(child)) {
						continue;
					}

					double Rx = child.getPos()[0] - star.getPos()[0];
					double Ry = child.getPos()[1] - star.getPos()[1];
					double Rz = child.getPos()[2] - star.getPos()[2];
					double R_sq = (Rx * Rx) + (Ry * Ry) + (Rz * Rz);

					double psi = atan2(Rz, Rx);
					double theta = acos(Ry / sqrt(R_sq));

					double A = G * child.getMass() / R_sq;

					star.getAccel()[0] += A * sin(theta) * cos(psi);
					star.getAccel()[1] += A * cos(theta);
					star.getAccel()[2] += A * sin(theta) * sin(psi);

					A *= star.getMass() / child.getMass();

					child.getAccel()[0] -= A * sin(theta) * cos(psi);
					child.getAccel()[1] -= A * cos(theta);
					child.getAccel()[2] -= A * sin(theta) * sin(psi);
				}
				break;
			}

		}
		// All other io.flightclub.solarsystem.bodies only interact with their parent.
		// If their parent is the star, this has already been done
		for (Body body : bodies) {

			if (!body.equals(star) && !body.isOrbiting(star)) {

				Body otherBody = body.getParent();

				double Rx = otherBody.getPos()[0] - body.getPos()[0];
				double Ry = otherBody.getPos()[1] - body.getPos()[1];
				double Rz = otherBody.getPos()[2] - body.getPos()[2];
				double R_sq = (Rx * Rx) + (Ry * Ry) + (Rz * Rz);

				double psi = atan2(Rz, Rx);
				double theta = acos(Ry / sqrt(R_sq));

				double A = G * otherBody.getMass() / R_sq;

				body.getAccel()[0] += A * sin(theta) * cos(psi);
				body.getAccel()[1] += A * cos(theta);
				body.getAccel()[2] += A * sin(theta) * sin(psi);

				A *= body.getMass() / otherBody.getMass();

				otherBody.getAccel()[0] -= A * sin(theta) * cos(psi);
				otherBody.getAccel()[1] -= A * cos(theta);
				otherBody.getAccel()[2] -= A * sin(theta) * sin(psi);

			}
		}
	}

	// draws positions (or trajectories when continuous)
	public void output(double t) {

		for (Body body : bodies) {

			PrintWriter pw = null;
			try {
				File outputFile = new File("output/" + body.getName() + ".dat");
				pw = new PrintWriter(new FileWriter(outputFile, true));
				pw.printf("%9.3f\t%9.3f\t%9.3f\t%9.3f\n", t, body.getPos()[0], body.getPos()[1], body.getPos()[2]);

			} catch (IOException e) {
				System.err.println(e.getMessage());
			} finally {
				if (pw != null) {
					pw.close();
				}
			}

		}
	}

}
