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
package io.flightclub.solarsystem.bodies.planets;

import io.flightclub.solarsystem.bodies.Body;
import io.flightclub.solarsystem.bodies.stars.Sun;

import static io.flightclub.solarsystem.utils.Astrodynamics.getSeconds;
import static java.lang.Math.*;

/**
 * @author declan
 */
public final class Earth extends Body {

	private static final Body SINGLETON = new Earth(6378137, 5.9722e24, getSeconds(0, 23, 56, 4));

	private Earth(double radius, double mass, long period) {
		super(radius, mass, period);
		setOrbiting(Sun.get());
		setOrbit(147.095e9, 152.1e9);
	}

	public static Body get() {
		return SINGLETON;
	}

	@Override
	public String getName() {
		return "Earth";
	}

	@Override
	public double getGravitationalConstant() {
		return 9.80665;
	}

	@Override
	public double[] getRGB() {
		return new double[]{0.0, 0.0, 1.0};
	}

	@Override
	public double densityAtAltitude(double altitude) {

		// Source: http://www.pdas.com/atmos.html
		altitude = max(altitude * 1e-3, 0.0);
		return altitude < 14 ? 0.0027557251 * altitude * altitude - 0.1084028109 * altitude + 1.2146329255
				: altitude < 20 ? 2.0860888159 * exp(-0.1579673909 * altitude)
				: altitude < 86 ? 1.3944570704 * exp(-0.1410631501 * altitude)
				: 0.0;
	}

	@Override
	public double pressureAtAltitude(double altitude) {

		// Source: http://www.pdas.com/atmos.html
		altitude = max(altitude * 1e-3, 0.0);
		return altitude < 86 ? 104333.757504423 * exp(-0.1434747262 * altitude) : 0.0;

	}

	@Override
	public double speedOfSoundAtAltitude(double altitude) {

		// Source: http://www.pdas.com/atmos.html
		altitude = max(altitude * 1e-3, 0.0);
		return altitude < 11 ? -4.0873043478 * altitude + 340.6833478261
				: altitude < 20 ? 295.1
				: altitude < 50 ? -0.0018474968 * pow(altitude, 3) + 0.215258168 * altitude * altitude - 6.7011556984 * altitude + 359.0525813969
				: altitude < 86 ? -1.6203508772 * altitude + 411.8680701754
				: 275;
	}

}
