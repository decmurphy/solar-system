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
package com.solarsystem.bodies.planets;

import com.solarsystem.bodies.Body;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

/**
 *
 * @author declan
 */
public final class Earth extends Body {
    
    public static final Body singleton = new Earth(6378137, 5.972e24); 
    private Earth(double radius, double mass) {
        super(radius, mass);
    }
    
    public static Body get() {
        return singleton;
    }
    
    @Override
    public String getName() {
        return "Earth";
    }

    public void drawHazardMap(File inputFile, File outputFile) throws IOException 
    {
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

}
