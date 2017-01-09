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
package com.solarsystem.starsystem;

import com.solarsystem.bodies.Body;
import com.solarsystem.bodies.planets.Earth;
import com.solarsystem.bodies.planets.Jupiter;
import com.solarsystem.bodies.planets.Mars;
import com.solarsystem.bodies.planets.Mercury;
import com.solarsystem.bodies.planets.Luna;
import com.solarsystem.bodies.planets.Neptune;
import com.solarsystem.bodies.planets.Pluto;
import com.solarsystem.bodies.planets.Saturn;
import com.solarsystem.bodies.planets.Uranus;
import com.solarsystem.bodies.planets.Venus;
import com.solarsystem.bodies.stars.Sun;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 *
 * @author declan
 */
public class Sol extends StarSystem {

    private static final StarSystem SINGLETON = new Sol();

    public static StarSystem get() {
        return SINGLETON;
    }

    private Sol() {
        addBody(Sun.get());
        addBody(Mercury.get());
        addBody(Venus.get());
        addBody(Earth.get());
        addBody(Luna.get());
        addBody(Mars.get());
        addBody(Jupiter.get());
        addBody(Saturn.get());
        addBody(Uranus.get());
        addBody(Neptune.get());
        addBody(Pluto.get());
    }

    @Override
    public String getName() {
        return "Sol";
    }

    @Override
    public void init(int year) throws Exception {

        File outputDir = new File("output");
        outputDir.mkdir();
        
        for(File f: outputDir.listFiles())
            f.delete();

        for (Body body : getBodies()) {
            
            boolean pastHeader = false, complete = false;
            try (BufferedReader br = new BufferedReader(new FileReader(new File("horizons/"+body.getName().toLowerCase()+".txt")))) {
                for (String line; (line = br.readLine()) != null;) {
                    
                    if("$$SOE".equals(line.trim())) {
                        pastHeader = true;
                        continue;
                    }
                    if("$$EOE".equals(line.trim())) {
                        break;
                    }
                                        
                    if(!pastHeader)
                        continue;
                    
                    String[] data = line.split(",");
                    if(data[1].trim().startsWith("A.D. "+year)) {
                        double[] pos = new double[]{1e3*Double.parseDouble(data[2].trim()), 1e3*Double.parseDouble(data[3].trim()), 1e3*Double.parseDouble(data[4].trim())};
                        double[] vel = new double[]{1e3*Double.parseDouble(data[5].trim()), 1e3*Double.parseDouble(data[6].trim()), 1e3*Double.parseDouble(data[7].trim())};
                        
                        body.setPos(pos);
                        body.setVel(vel);
                        complete = true;
                        break;
                    }
                }
                if(!complete) {
                    if (body.getParent() == null) {
                        body.setPos(new double[]{0, 0, 0});
                        body.setVel(new double[]{0, 0, 0});
                    } else {
                        body.setAtPerihelion();
                    }
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

        }

    }

}
