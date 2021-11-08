package org.sswr.gdbmodel;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.sswr.util.data.DataTools;

@Entity
@Table(name = "LAMPPOST")
public class Lamppost
{
	@Column(name="OBJECTID")
	private int objectId;

//	@Column(name="Shape")
//	private Geometry shape;


	public Lamppost() {
	}

	public Lamppost(int objectId){//}, Geometry shape) {
		this.objectId = objectId;
//		this.shape = shape;
	}

	public int getObjectId() {
		return this.objectId;
	}

	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Lamppost)) {
			return false;
		}
		Lamppost lamppost = (Lamppost) o;
		return objectId == lamppost.objectId;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(objectId);
	}

	@Override
	public String toString()
	{
		return DataTools.toObjectString(this);
	}
}
