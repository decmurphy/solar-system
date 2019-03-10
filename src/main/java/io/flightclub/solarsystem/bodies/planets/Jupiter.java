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

/**
 * @author declan
 */
public final class Jupiter extends Body {

	private static final Body SINGLETON = new Jupiter(69911000, 1.8986e27, getSeconds(0, 9, 55, 30));

	private Jupiter(double radius, double mass, long period) {
		super("Jupiter", radius, mass, period);
		setOrbiting(Sun.get());
		setOrbit(740.55e9, 816.04e9);
	}

	public static Body get() {
		return SINGLETON;
	}

	@Override
	public double[] getRGB() {
		return new double[]{1.0, 0.5, 0.5};
	}

	@Override
	public double densityAtAltitude(double altitude) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public double pressureAtAltitude(double altitude) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public double speedOfSoundAtAltitude(double altitude) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
