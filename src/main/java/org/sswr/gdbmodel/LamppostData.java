package org.sswr.gdbmodel;

import java.sql.Timestamp;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.sswr.util.data.DataTools;

@Entity
@Table(name="lamppost_data", schema="dbo")
public class LamppostData
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private Timestamp dtRecv;
	private Timestamp dtData;
	private String lampno;
	private Integer lightStatus;
	private Integer faultStatus;
	private Integer fault1Status;
	private Integer fault2Status;
	private Integer fault3Status;
	private Integer fault4Status;
	private Integer fault5Status;
	private Integer fault6Status;
	private Integer fault7Status;
	private Integer fault8Status;
	private Integer fault9Status;
	private Integer fault10Status;
	private String fault1Descr;
	private String fault2Descr;
	private String fault3Descr;
	private String fault4Descr;
	private String fault5Descr;
	private String fault6Descr;
	private String fault7Descr;
	private String fault8Descr;
	private String fault9Descr;
	private String fault10Descr;
	private Double val1;
	private Double val2;
	private Double val3;
	private Double val4;
	private Double val5;
	private Double val6;
	private Double val7;
	private Double val8;
	private Double val9;
	private Double val10;
	private Integer light1Status;
	private Integer light2Status;
	private Integer light3Status;
	private Integer light4Status;
	private Integer light5Status;
	private Integer light6Status;
	private Integer light7Status;
	private Integer light8Status;
	private Integer light9Status;
	private Integer light10Status;

	public LamppostData() {
	}

	public LamppostData(int id, Timestamp dtRecv, Timestamp dtData, String lampno, Integer lightStatus, Integer faultStatus, Integer fault1Status, Integer fault2Status, Integer fault3Status, Integer fault4Status, Integer fault5Status, Integer fault6Status, Integer fault7Status, Integer fault8Status, Integer fault9Status, Integer fault10Status, String fault1Descr, String fault2Descr, String fault3Descr, String fault4Descr, String fault5Descr, String fault6Descr, String fault7Descr, String fault8Descr, String fault9Descr, String fault10Descr, Double val1, Double val2, Double val3, Double val4, Double val5, Double val6, Double val7, Double val8, Double val9, Double val10, Integer light1Status, Integer light2Status, Integer light3Status, Integer light4Status, Integer light5Status, Integer light6Status, Integer light7Status, Integer light8Status, Integer light9Status, Integer light10Status) {
		this.id = id;
		this.dtRecv = dtRecv;
		this.dtData = dtData;
		this.lampno = lampno;
		this.lightStatus = lightStatus;
		this.faultStatus = faultStatus;
		this.fault1Status = fault1Status;
		this.fault2Status = fault2Status;
		this.fault3Status = fault3Status;
		this.fault4Status = fault4Status;
		this.fault5Status = fault5Status;
		this.fault6Status = fault6Status;
		this.fault7Status = fault7Status;
		this.fault8Status = fault8Status;
		this.fault9Status = fault9Status;
		this.fault10Status = fault10Status;
		this.fault1Descr = fault1Descr;
		this.fault2Descr = fault2Descr;
		this.fault3Descr = fault3Descr;
		this.fault4Descr = fault4Descr;
		this.fault5Descr = fault5Descr;
		this.fault6Descr = fault6Descr;
		this.fault7Descr = fault7Descr;
		this.fault8Descr = fault8Descr;
		this.fault9Descr = fault9Descr;
		this.fault10Descr = fault10Descr;
		this.val1 = val1;
		this.val2 = val2;
		this.val3 = val3;
		this.val4 = val4;
		this.val5 = val5;
		this.val6 = val6;
		this.val7 = val7;
		this.val8 = val8;
		this.val9 = val9;
		this.val10 = val10;
		this.light1Status = light1Status;
		this.light2Status = light2Status;
		this.light3Status = light3Status;
		this.light4Status = light4Status;
		this.light5Status = light5Status;
		this.light6Status = light6Status;
		this.light7Status = light7Status;
		this.light8Status = light8Status;
		this.light9Status = light9Status;
		this.light10Status = light10Status;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getDtRecv() {
		return this.dtRecv;
	}

	public void setDtRecv(Timestamp dtRecv) {
		this.dtRecv = dtRecv;
	}

	public Timestamp getDtData() {
		return this.dtData;
	}

	public void setDtData(Timestamp dtData) {
		this.dtData = dtData;
	}

	public String getLampno() {
		return this.lampno;
	}

	public void setLampno(String lampno) {
		this.lampno = lampno;
	}

	public Integer getLightStatus() {
		return this.lightStatus;
	}

	public void setLightStatus(Integer lightStatus) {
		this.lightStatus = lightStatus;
	}

	public Integer getFaultStatus() {
		return this.faultStatus;
	}

	public void setFaultStatus(Integer faultStatus) {
		this.faultStatus = faultStatus;
	}

	public Integer getFault1Status() {
		return this.fault1Status;
	}

	public void setFault1Status(Integer fault1Status) {
		this.fault1Status = fault1Status;
	}

	public Integer getFault2Status() {
		return this.fault2Status;
	}

	public void setFault2Status(Integer fault2Status) {
		this.fault2Status = fault2Status;
	}

	public Integer getFault3Status() {
		return this.fault3Status;
	}

	public void setFault3Status(Integer fault3Status) {
		this.fault3Status = fault3Status;
	}

	public Integer getFault4Status() {
		return this.fault4Status;
	}

	public void setFault4Status(Integer fault4Status) {
		this.fault4Status = fault4Status;
	}

	public Integer getFault5Status() {
		return this.fault5Status;
	}

	public void setFault5Status(Integer fault5Status) {
		this.fault5Status = fault5Status;
	}

	public Integer getFault6Status() {
		return this.fault6Status;
	}

	public void setFault6Status(Integer fault6Status) {
		this.fault6Status = fault6Status;
	}

	public Integer getFault7Status() {
		return this.fault7Status;
	}

	public void setFault7Status(Integer fault7Status) {
		this.fault7Status = fault7Status;
	}

	public Integer getFault8Status() {
		return this.fault8Status;
	}

	public void setFault8Status(Integer fault8Status) {
		this.fault8Status = fault8Status;
	}

	public Integer getFault9Status() {
		return this.fault9Status;
	}

	public void setFault9Status(Integer fault9Status) {
		this.fault9Status = fault9Status;
	}

	public Integer getFault10Status() {
		return this.fault10Status;
	}

	public void setFault10Status(Integer fault10Status) {
		this.fault10Status = fault10Status;
	}

	public String getFault1Descr() {
		return this.fault1Descr;
	}

	public void setFault1Descr(String fault1Descr) {
		this.fault1Descr = fault1Descr;
	}

	public String getFault2Descr() {
		return this.fault2Descr;
	}

	public void setFault2Descr(String fault2Descr) {
		this.fault2Descr = fault2Descr;
	}

	public String getFault3Descr() {
		return this.fault3Descr;
	}

	public void setFault3Descr(String fault3Descr) {
		this.fault3Descr = fault3Descr;
	}

	public String getFault4Descr() {
		return this.fault4Descr;
	}

	public void setFault4Descr(String fault4Descr) {
		this.fault4Descr = fault4Descr;
	}

	public String getFault5Descr() {
		return this.fault5Descr;
	}

	public void setFault5Descr(String fault5Descr) {
		this.fault5Descr = fault5Descr;
	}

	public String getFault6Descr() {
		return this.fault6Descr;
	}

	public void setFault6Descr(String fault6Descr) {
		this.fault6Descr = fault6Descr;
	}

	public String getFault7Descr() {
		return this.fault7Descr;
	}

	public void setFault7Descr(String fault7Descr) {
		this.fault7Descr = fault7Descr;
	}

	public String getFault8Descr() {
		return this.fault8Descr;
	}

	public void setFault8Descr(String fault8Descr) {
		this.fault8Descr = fault8Descr;
	}

	public String getFault9Descr() {
		return this.fault9Descr;
	}

	public void setFault9Descr(String fault9Descr) {
		this.fault9Descr = fault9Descr;
	}

	public String getFault10Descr() {
		return this.fault10Descr;
	}

	public void setFault10Descr(String fault10Descr) {
		this.fault10Descr = fault10Descr;
	}

	public Double getVal1() {
		return this.val1;
	}

	public void setVal1(Double val1) {
		this.val1 = val1;
	}

	public Double getVal2() {
		return this.val2;
	}

	public void setVal2(Double val2) {
		this.val2 = val2;
	}

	public Double getVal3() {
		return this.val3;
	}

	public void setVal3(Double val3) {
		this.val3 = val3;
	}

	public Double getVal4() {
		return this.val4;
	}

	public void setVal4(Double val4) {
		this.val4 = val4;
	}

	public Double getVal5() {
		return this.val5;
	}

	public void setVal5(Double val5) {
		this.val5 = val5;
	}

	public Double getVal6() {
		return this.val6;
	}

	public void setVal6(Double val6) {
		this.val6 = val6;
	}

	public Double getVal7() {
		return this.val7;
	}

	public void setVal7(Double val7) {
		this.val7 = val7;
	}

	public Double getVal8() {
		return this.val8;
	}

	public void setVal8(Double val8) {
		this.val8 = val8;
	}

	public Double getVal9() {
		return this.val9;
	}

	public void setVal9(Double val9) {
		this.val9 = val9;
	}

	public Double getVal10() {
		return this.val10;
	}

	public void setVal10(Double val10) {
		this.val10 = val10;
	}

	public Integer getLight1Status() {
		return this.light1Status;
	}

	public void setLight1Status(Integer light1Status) {
		this.light1Status = light1Status;
	}

	public Integer getLight2Status() {
		return this.light2Status;
	}

	public void setLight2Status(Integer light2Status) {
		this.light2Status = light2Status;
	}

	public Integer getLight3Status() {
		return this.light3Status;
	}

	public void setLight3Status(Integer light3Status) {
		this.light3Status = light3Status;
	}

	public Integer getLight4Status() {
		return this.light4Status;
	}

	public void setLight4Status(Integer light4Status) {
		this.light4Status = light4Status;
	}

	public Integer getLight5Status() {
		return this.light5Status;
	}

	public void setLight5Status(Integer light5Status) {
		this.light5Status = light5Status;
	}

	public Integer getLight6Status() {
		return this.light6Status;
	}

	public void setLight6Status(Integer light6Status) {
		this.light6Status = light6Status;
	}

	public Integer getLight7Status() {
		return this.light7Status;
	}

	public void setLight7Status(Integer light7Status) {
		this.light7Status = light7Status;
	}

	public Integer getLight8Status() {
		return this.light8Status;
	}

	public void setLight8Status(Integer light8Status) {
		this.light8Status = light8Status;
	}

	public Integer getLight9Status() {
		return this.light9Status;
	}

	public void setLight9Status(Integer light9Status) {
		this.light9Status = light9Status;
	}

	public Integer getLight10Status() {
		return this.light10Status;
	}

	public void setLight10Status(Integer light10Status) {
		this.light10Status = light10Status;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof LamppostData)) {
			return false;
		}
		LamppostData lamppostData = (LamppostData) o;
		return id == lamppostData.id && Objects.equals(dtRecv, lamppostData.dtRecv) && Objects.equals(dtData, lamppostData.dtData) && Objects.equals(lampno, lamppostData.lampno) && Objects.equals(lightStatus, lamppostData.lightStatus) && Objects.equals(faultStatus, lamppostData.faultStatus) && Objects.equals(fault1Status, lamppostData.fault1Status) && Objects.equals(fault2Status, lamppostData.fault2Status) && Objects.equals(fault3Status, lamppostData.fault3Status) && Objects.equals(fault4Status, lamppostData.fault4Status) && Objects.equals(fault5Status, lamppostData.fault5Status) && Objects.equals(fault6Status, lamppostData.fault6Status) && Objects.equals(fault7Status, lamppostData.fault7Status) && Objects.equals(fault8Status, lamppostData.fault8Status) && Objects.equals(fault9Status, lamppostData.fault9Status) && Objects.equals(fault10Status, lamppostData.fault10Status) && Objects.equals(fault1Descr, lamppostData.fault1Descr) && Objects.equals(fault2Descr, lamppostData.fault2Descr) && Objects.equals(fault3Descr, lamppostData.fault3Descr) && Objects.equals(fault4Descr, lamppostData.fault4Descr) && Objects.equals(fault5Descr, lamppostData.fault5Descr) && Objects.equals(fault6Descr, lamppostData.fault6Descr) && Objects.equals(fault7Descr, lamppostData.fault7Descr) && Objects.equals(fault8Descr, lamppostData.fault8Descr) && Objects.equals(fault9Descr, lamppostData.fault9Descr) && Objects.equals(fault10Descr, lamppostData.fault10Descr) && Objects.equals(val1, lamppostData.val1) && Objects.equals(val2, lamppostData.val2) && Objects.equals(val3, lamppostData.val3) && Objects.equals(val4, lamppostData.val4) && Objects.equals(val5, lamppostData.val5) && Objects.equals(val6, lamppostData.val6) && Objects.equals(val7, lamppostData.val7) && Objects.equals(val8, lamppostData.val8) && Objects.equals(val9, lamppostData.val9) && Objects.equals(val10, lamppostData.val10) && Objects.equals(light1Status, lamppostData.light1Status) && Objects.equals(light2Status, lamppostData.light2Status) && Objects.equals(light3Status, lamppostData.light3Status) && Objects.equals(light4Status, lamppostData.light4Status) && Objects.equals(light5Status, lamppostData.light5Status) && Objects.equals(light6Status, lamppostData.light6Status) && Objects.equals(light7Status, lamppostData.light7Status) && Objects.equals(light8Status, lamppostData.light8Status) && Objects.equals(light9Status, lamppostData.light9Status) && Objects.equals(light10Status, lamppostData.light10Status);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, dtRecv, dtData, lampno, lightStatus, faultStatus, fault1Status, fault2Status, fault3Status, fault4Status, fault5Status, fault6Status, fault7Status, fault8Status, fault9Status, fault10Status, fault1Descr, fault2Descr, fault3Descr, fault4Descr, fault5Descr, fault6Descr, fault7Descr, fault8Descr, fault9Descr, fault10Descr, val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, light1Status, light2Status, light3Status, light4Status, light5Status, light6Status, light7Status, light8Status, light9Status, light10Status);
	}

	@Override
	public String toString() {
		return DataTools.toObjectString(this);
	}

	public static String[] getFieldOrder() {
		return new String[] {
		"id",
		"dtRecv",
		"dtData",
		"lampno",
		"lightStatus",
		"faultStatus",
		"fault1Status",
		"fault2Status",
		"fault3Status",
		"fault4Status",
		"fault5Status",
		"fault6Status",
		"fault7Status",
		"fault8Status",
		"fault9Status",
		"fault10Status",
		"fault1Descr",
		"fault2Descr",
		"fault3Descr",
		"fault4Descr",
		"fault5Descr",
		"fault6Descr",
		"fault7Descr",
		"fault8Descr",
		"fault9Descr",
		"fault10Descr",
		"val1",
		"val2",
		"val3",
		"val4",
		"val5",
		"val6",
		"val7",
		"val8",
		"val9",
		"val10",
		"light1Status",
		"light2Status",
		"light3Status",
		"light4Status",
		"light5Status",
		"light6Status",
		"light7Status",
		"light8Status",
		"light9Status",
		"light10Status"
		};
	}
}
