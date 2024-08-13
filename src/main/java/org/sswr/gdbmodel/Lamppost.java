package org.sswr.gdbmodel;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import org.locationtech.jts.geom.Geometry;
import org.sswr.util.data.DataTools;

@Entity
@Table(name = "LAMPPOST")
public class Lamppost
{
	@Column(name="OBJECTID")
	private int objectId;

	@Column(name="Shape")
	private Geometry shape;

	public Lamppost() {
	}

	public Lamppost(int objectId, Geometry shape) {
		this.objectId = objectId;
		this.shape = shape;
	}

	public int getObjectId() {
		return this.objectId;
	}

	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}

	public Geometry getShape() {
		return this.shape;
	}

	public void setShape(Geometry shape) {
		this.shape = shape;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Lamppost)) {
			return false;
		}
		Lamppost lamppost = (Lamppost) o;
		return objectId == lamppost.objectId && Objects.equals(shape, lamppost.shape);
	}

	@Override
	public int hashCode() {
		return Objects.hash(objectId, shape);
	}

	@Override
	public String toString()
	{
		return DataTools.toObjectString(this);
	}
}
