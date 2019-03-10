package io.flightclub.solarsystem.bodies;

import lombok.Data;
import lombok.NoArgsConstructor;

import static io.flightclub.solarsystem.utils.Astrodynamics.getVelocityAtDistance;

@Data
@NoArgsConstructor
public abstract class OrbitingObject {

	private String name;
	private double[] pos;
	private double[] vel;
	private double[] accel;
	private boolean started;
	private long startTime;
	protected Body parent;

	private double r_a;
	private double r_p;
	private double a;
	private double e;

	public OrbitingObject(String name) {
		this.name = name;
		this.started = false;
		this.pos = new double[3];
		this.vel = new double[3];
		this.accel = new double[3];
	}

	public final void setOrbiting(Body parent) {
		this.parent = parent;
	}

	public final boolean isOrbiting(Body body) {
		if (parent == null) {
			return body == null;
		}
		return parent.equals(body);
	}

	public Body getParent() {
		return parent;
	}
	public final void setOrbit(double perihelion, double aphelion) {
		this.r_a = aphelion;
		this.r_p = perihelion;
		this.a = (r_a + r_p) / 2.0;
		this.e = (r_a - r_p) / (2 * a);
	}

	public final void setAtPerihelion() throws Exception {
		if (parent == null) {
			throw new Exception(getName() + "'s parent is null. Can't set orbit.");
		}

		setPos(new double[]{parent.getPos()[0] + r_p, parent.getPos()[1], parent.getPos()[2]});
		setVel(new double[]{parent.getVel()[0], parent.getVel()[1], parent.getVel()[2] - getVelocityAtDistance(parent.getMass(), r_p, a)});
	}

	public final void setAtAphelion() throws Exception {
		if (parent == null) {
			throw new Exception(getName() + "'s parent is null. Can't set orbit.");
		}

		setPos(new double[]{parent.getPos()[0] + r_a, parent.getPos()[1], parent.getPos()[2]});
		setVel(new double[]{parent.getVel()[0], parent.getVel()[1], parent.getVel()[2] - getVelocityAtDistance(parent.getMass(), r_a, a)});
	}

	public final void setPos(double[] pos) {
		System.arraycopy(pos, 0, this.getPos(), 0, pos.length);
	}

	public final void setVel(double[] vel) {
		System.arraycopy(vel, 0, this.getVel(), 0, vel.length);
	}

	public final void setAccel(double[] accel) {
		System.arraycopy(accel, 0, this.getAccel(), 0, accel.length);
	}


}
