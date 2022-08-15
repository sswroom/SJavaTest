package org.sswr.model;

import java.sql.Timestamp;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.sswr.util.data.DataTools;

@Entity
@Table(name="test_table", schema="dbo")
public class TestTable
{
	@Id
	private int id;
	private Timestamp time;

	public TestTable() {
	}

	public TestTable(int id, Timestamp time) {
		this.id = id;
		this.time = time;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getTime() {
		return this.time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof TestTable)) {
			return false;
		}
		TestTable testTable = (TestTable) o;
		return id == testTable.id && Objects.equals(time, testTable.time);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, time);
	}

	@Override
	public String toString() {
		return DataTools.toObjectString(this);
	}

	public static String[] getFieldOrder() {
		return new String[] {
		"id",
		"time"
		};
	}
}
