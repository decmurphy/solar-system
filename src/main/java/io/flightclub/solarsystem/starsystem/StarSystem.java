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
import io.flightclub.solarsystem.bodies.OrbitingObject;
import io.flightclub.solarsystem.bodies.satellites.Satellite;
import io.flightclub.solarsystem.utils.Resources;
import lombok.Data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.flightclub.solarsystem.utils.Astrodynamics.G;
import static java.lang.Math.*;

/**
 * @author declan
 */
@Data
public abstract class StarSystem {

	private final List<Body> bodies;
	private final List<Satellite> satellites;

	StarSystem() {

		this.bodies = new ArrayList<>();
		this.satellites = new ArrayList<>();
	}

	final void addBody(Body b) {
		if (!bodies.contains(b)) {
			bodies.add(b);
		}
	}

	final void addSatellite(Satellite sat) {
		if (!satellites.contains(sat)) {
			satellites.add(sat);
		}
	}

	public final void removeBody(Body b) {
		bodies.remove(b);
	}

	public final void removeSatellite(Satellite sat) {
		satellites.remove(sat);
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
		for (Satellite sat : satellites) {
			for (int i = 0; i < 3; i++) {
				sat.getVel()[i] += sat.getAccel()[i] * dt / 2;
			}
		}
	}

	public void leapfrog(double t, double dt) {

		for (Body body : bodies) {
			if(!body.isStarted()) {
				if(t > body.getStartTime()) {
					body.setStarted(true);
					leapfrogFirstStep(dt);
				}
			} else {
				for (int i = 0; i < 3; i++) {
					body.getPos()[i] += body.getVel()[i] * dt;
				}
			}
		}
		for (Satellite sat : satellites) {
			if(!sat.isStarted()) {
				if(t > sat.getStartTime()) {
					sat.setStarted(true);
					leapfrogFirstStep(dt);
				}
			} else {
				for (int i = 0; i < 3; i++) {
					sat.getPos()[i] += sat.getVel()[i] * dt;
				}
			}
		}

		updateAcceleration();

		for (Body body : bodies) {
			if(!body.isStarted()) {
				continue;
			}
			for (int i = 0; i < 3; i++) {
				body.getVel()[i] += body.getAccel()[i] * dt;
			}
		}
		for (Satellite sat : satellites) {
			if(!sat.isStarted()) {
				continue;
			}
			for (int i = 0; i < 3; i++) {
				sat.getVel()[i] += sat.getAccel()[i] * dt;
			}
		}

	}

	private void updateAcceleration() {

		for (Body body : bodies) {
			// set accels to zero again
			System.arraycopy(new double[3], 0, body.getAccel(), 0, 3);
		}
		for (Satellite sat : satellites) {
			// set accels to zero again
			System.arraycopy(new double[3], 0, sat.getAccel(), 0, 3);
		}

		// The star in the system interacts with all io.flightclub.solarsystem.bodies
		// Find the star and set accelerations for all io.flightclub.solarsystem.bodies
		Body star = bodies.stream().filter(body -> body.isOrbiting(null)).findFirst().get();

		bodies.stream().filter(child -> !child.equals(star)).forEach(child -> {

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

			// All other io.flightclub.solarsystem.bodies only interact with their parent.
			// If their parent is the star, this has already been done
			if(!child.isOrbiting(star)) {

				Body parent = child.getParent();

				Rx = parent.getPos()[0] - child.getPos()[0];
				Ry = parent.getPos()[1] - child.getPos()[1];
				Rz = parent.getPos()[2] - child.getPos()[2];
				R_sq = (Rx * Rx) + (Ry * Ry) + (Rz * Rz);

				psi = atan2(Rz, Rx);
				theta = acos(Ry / sqrt(R_sq));

				A = G * parent.getMass() / R_sq;

				child.getAccel()[0] += A * sin(theta) * cos(psi);
				child.getAccel()[1] += A * cos(theta);
				child.getAccel()[2] += A * sin(theta) * sin(psi);

				A *= child.getMass() / parent.getMass();

				parent.getAccel()[0] -= A * sin(theta) * cos(psi);
				parent.getAccel()[1] -= A * cos(theta);
				parent.getAccel()[2] -= A * sin(theta) * sin(psi);
			}

		});

		// Satellites don't affect the star
		satellites.forEach(sat -> {

			if(!sat.isStarted()) {
				return;
			}

			double Rx = sat.getPos()[0] - star.getPos()[0];
			double Ry = sat.getPos()[1] - star.getPos()[1];
			double Rz = sat.getPos()[2] - star.getPos()[2];
			double R_sq = (Rx * Rx) + (Ry * Ry) + (Rz * Rz);

			double psi = atan2(Rz, Rx);
			double theta = acos(Ry / sqrt(R_sq));

			double A = G * star.getMass() / R_sq;

			sat.getAccel()[0] -= A * sin(theta) * cos(psi);
			sat.getAccel()[1] -= A * cos(theta);
			sat.getAccel()[2] -= A * sin(theta) * sin(psi);

			if(!sat.isOrbiting(star)) {
				Body parent = sat.getParent();

				Rx = parent.getPos()[0] - sat.getPos()[0];
				Ry = parent.getPos()[1] - sat.getPos()[1];
				Rz = parent.getPos()[2] - sat.getPos()[2];
				R_sq = (Rx * Rx) + (Ry * Ry) + (Rz * Rz);

				psi = atan2(Rz, Rx);
				theta = acos(Ry / sqrt(R_sq));

				A = G * parent.getMass() / R_sq;

				sat.getAccel()[0] += A * sin(theta) * cos(psi);
				sat.getAccel()[1] += A * cos(theta);
				sat.getAccel()[2] += A * sin(theta) * sin(psi);
			}

		});

	}

	// draws positions (or trajectories when continuous)
	public void output(double t) {
		bodies.forEach(body -> outputToFile(body, t));
		satellites.forEach(sat -> outputToFile(sat, t));
	}

	private void outputToFile(OrbitingObject obj, double t) {

		if(!obj.isStarted()) {
			return;
		}

		File outputFile = new File("output/" + obj.getName() + ".csv");

		try (PrintWriter pw = new PrintWriter(new FileWriter(outputFile, true))) {
			pw.printf("%.0f,%.0f,%.0f,%.0f\n", t, obj.getPos()[0], obj.getPos()[1], obj.getPos()[2]);
		} catch (IOException e) {
			System.err.println(Resources.getStackTrace(e));
		}

	}

}
