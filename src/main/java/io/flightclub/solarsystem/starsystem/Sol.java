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

import io.flightclub.solarsystem.bodies.OrbitingObject;
import io.flightclub.solarsystem.bodies.planets.*;
import io.flightclub.solarsystem.bodies.satellites.Satellite;
import io.flightclub.solarsystem.bodies.stars.Sun;
import io.flightclub.solarsystem.utils.Resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

/**
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

		addSatellite(new Satellite("Starman", Sun.get()));
	}

	@Override
	public String getName() {
		return "Sol";
	}

	@Override
	public void init(int year) throws Exception {

		File outputDir = new File("output");
		outputDir.mkdir();

		Arrays.stream(outputDir.listFiles()).forEach(File::delete);

		List<File> files = Arrays.asList(Resources.getResourceFolderFiles("horizons_earth-mean-equator"));

		getBodies().stream().parallel().forEach(body -> parseHorizonsFile(files, body, year));
		getSatellites().stream().parallel().forEach(sat -> parseHorizonsFile(files, sat, year));

	}

	private void parseHorizonsFile(List<File> files, OrbitingObject obj, int year) {

		boolean pastHeader = false, complete = false;

		File bodyFile = files.stream()
				.filter(f -> f.getName().toLowerCase().contains(obj.getName().toLowerCase()))
				.findFirst()
				.orElse(null);

		SimpleDateFormat sdf = new SimpleDateFormat("'A.D.' yyyy-MMM-dd HH:mm:ss.SSSS");

		try (BufferedReader br = new BufferedReader(new FileReader(bodyFile))) {
			for (String line; (line = br.readLine()) != null; ) {

				if ("$$SOE".equals(line.trim())) {
					pastHeader = true;
					continue;
				}
				if ("$$EOE".equals(line.trim())) {
					break;
				}

				if (!pastHeader)
					continue;

				String[] data = line.split(",");
				if (data[1].trim().startsWith("A.D. " + year)) {
					long startTime = sdf.parse(data[1].trim()).getTime() / 1000;
					double[] pos = new double[]{1e3 * Double.parseDouble(data[2].trim()), 1e3 * Double.parseDouble(data[3].trim()), 1e3 * Double.parseDouble(data[4].trim())};
					double[] vel = new double[]{1e3 * Double.parseDouble(data[5].trim()), 1e3 * Double.parseDouble(data[6].trim()), 1e3 * Double.parseDouble(data[7].trim())};

					obj.setStartTime(startTime);
					obj.setPos(pos);
					obj.setVel(vel);
					complete = true;
					break;
				}
			}
			if (!complete) {
				if (obj.getParent() == null) {
					obj.setPos(new double[]{0, 0, 0});
					obj.setVel(new double[]{0, 0, 0});
				} else {
					obj.setAtPerihelion();
				}
			}
		} catch (Exception e) {
			Resources.getStackTrace(e);
		}
	}

}
