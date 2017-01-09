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
package com.solarsystem.bodies.planets;

import com.solarsystem.bodies.Body;
import com.solarsystem.bodies.stars.Sun;
import static com.solarsystem.utils.Astrodynamics.getSeconds;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Math.cos;
import static java.lang.Math.exp;
import static java.lang.Math.max;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

/**
 *
 * @author declan
 */
public final class Earth extends Body {

    private static final Body singleton = new Earth(6378137, 5.972e24, getSeconds(0, 23, 56, 4));

    private Earth(double radius, double mass, long period) {
        super(radius, mass, period);
        setOrbiting(Sun.get());
        setOrbit(147.095e9, 152.1e9);
    }

    public static Body get() {
        return singleton;
    }

    @Override
    public String getName() {
        return "Earth";
    }

    @Override
    public double[] getRGB() {
        return new double[]{0.0, 0.0, 1.0};
    }

    public void drawHazardMap(File inputFile, File outputFile) throws IOException {
        PrintWriter pw = null;
        BufferedReader br;

        double theta, psi;
        double R = getRadius() * 1e-3;

        try {
            String line;
            pw = new PrintWriter(new FileWriter(outputFile, false));
            br = new BufferedReader(new FileReader(inputFile));

            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    String[] parts = line.split("\\s+");

                    psi = toRadians(Double.parseDouble(parts[0]));
                    theta = toRadians(90 - Double.parseDouble(parts[1]));
                    pw.print(R * sin(theta) * cos(psi) + "\t" + R * sin(theta) * sin(psi) + "\t" + R * cos(theta) + "\n");
                } else {
                    pw.print("\n");
                }
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    @Override
    public double densityAtAltitude(double altitude) {

		// Source: http://www.pdas.com/atmos.html
        altitude = max(altitude*1e-3, 0.0);
        return altitude < 14 ? 0.0027557251*altitude*altitude - 0.1084028109*altitude + 1.2146329255
             : altitude < 20 ? 2.0860888159*exp(-0.1579673909*altitude)
             : altitude < 86 ? 1.3944570704*exp(-0.1410631501*altitude) 
             : 0.0;
    }

    @Override
    public double pressureAtAltitude(double altitude) {

		// Source: http://www.pdas.com/atmos.html
        altitude = max(altitude*1e-3, 0.0);
        return altitude < 86 ? 104333.757504423*exp(-0.1434747262*altitude) : 0.0;

    }

    @Override
    public double speedOfSoundAtAltitude(double altitude) {

		// Source: http://www.pdas.com/atmos.html
        altitude = max(altitude*1e-3, 0.0);
        return altitude < 11 ? -4.0873043478*altitude + 340.6833478261
             : altitude < 20 ? 295.1
             : altitude < 50 ? -0.0018474968*pow(altitude, 3) + 0.215258168*altitude*altitude - 6.7011556984*altitude + 359.0525813969
             : altitude < 86 ? -1.6203508772*altitude + 411.8680701754
             : 275;
    }

}
