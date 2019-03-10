package io.flightclub.solarsystem.bodies.satellites;

import io.flightclub.solarsystem.bodies.Body;
import io.flightclub.solarsystem.bodies.OrbitingObject;

public class Satellite extends OrbitingObject {

	public Satellite(String name, Body parent) {
		super(name);
		setOrbiting(parent);
	}
}
