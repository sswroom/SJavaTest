package org.sswr;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.LegendPosition;
import org.apache.poi.xddf.usermodel.chart.XDDFCategoryDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory;
import org.apache.poi.xddf.usermodel.chart.XDDFLineChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.drawingml.x2006.chart.STDispBlanksAs;
import org.sswr.util.data.DataTools;
import org.sswr.util.data.DateTimeUtil;
import org.sswr.util.io.FileUtil;
import org.sswr.util.math.unit.Distance.DistanceUnit;
import org.sswr.util.office.XlsxUtil;
import org.sswr.util.office.XlsxValidator;
import org.sswr.util.office.XlsxUtil.AxisType;

public class XlsxTest
{
	private static void test0() throws IOException
	{
		String fileName = FileUtil.getRealPath("~/Progs/Temp/Test.xlsx");
		XlsxValidator xlsx = new XlsxValidator(new FileInputStream(fileName), new String[]{"ColA", "ColB", "ColC", "ColD", "ColE"});
		while (xlsx.nextRow())
		{
			System.out.println(DataTools.toObjectString(xlsx.getRowAsMap()));
		}
		xlsx.close();
	}

	private static void test1() throws IOException
	{
		int testRowCnt = 2;
		String fileName = FileUtil.getRealPath("~/Progs/Temp/TestChart.xlsx");
		Workbook wb;
		wb = new XSSFWorkbook();
		Font font10 = XlsxUtil.createFont(wb, "Arial", 10, false);
		CellStyle dateStyle = XlsxUtil.createCellStyle(wb, font10, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "yyyy-MM-dd");
		CellStyle numStyle = XlsxUtil.createCellStyle(wb, font10, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "0.###");
		Sheet graphSheet = wb.createSheet();
		Sheet dataSheet = wb.createSheet();
		XSSFChart chart = XlsxUtil.createChart(graphSheet, DistanceUnit.Inch, 0.64, 1.61, 13.10, 5.53, "\nSETTLEMENT VS CHAINAGE");
		XDDFLineChartData lineChartData = XlsxUtil.lineChart(chart, "ACCUMULATED SETTLEMENT", "CHAINAGE", AxisType.CATEGORY);
		XlsxUtil.setDisplayBlankAs(chart.getCTChart(), STDispBlanksAs.GAP);
		if (testRowCnt > 1)
		{
			XlsxUtil.chartAddLegend(chart, LegendPosition.BOTTOM);
		}

		int rowNum;
		Timestamp ts;
		Row row = XlsxUtil.getRow(dataSheet, 0);
		XlsxUtil.setCell(row, 0, dateStyle, "Date");
		int i = 0;
		int j = 20;
		while (i < j)
		{
			XlsxUtil.setCell(row, i + 1, numStyle, 112 + i * 0.1);
			i++;
		}
		if (testRowCnt > 0)
		{
			XDDFCategoryDataSource chainageSource = XDDFDataSourcesFactory.fromStringCellRange((XSSFSheet)dataSheet, new CellRangeAddress(0, 0, 1, j));

			SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");
			rowNum = 0;
			while (rowNum < testRowCnt)
			{
				ts = DateTimeUtil.addDay(new Timestamp(System.currentTimeMillis()), rowNum - testRowCnt);
				rowNum++;
				row = XlsxUtil.getRow(dataSheet, rowNum);
				XlsxUtil.setCell(row, 0, dateStyle, ts);
				i = 0;
				while (i < j)
				{
					if (i != 5)
					{
						XlsxUtil.setCell(row, i + 1, numStyle, -1 + i * 0.1);
					}
					i++;
				}
				XDDFNumericalDataSource<Double> valSource = XDDFDataSourcesFactory.fromNumericCellRange((XSSFSheet)dataSheet, new CellRangeAddress(rowNum, rowNum, 1, j));
				XlsxUtil.addLineChartSeries(lineChartData, chainageSource, valSource, dateFmt.format(ts), testRowCnt > 1);
			}
			chart.setPlotOnlyVisibleCells(true);
			chart.plot(lineChartData);
		}

		FileOutputStream fos = new FileOutputStream(fileName, false);
		wb.write(fos);
		fos.close();
	}

	public static void main(String []args) throws IOException
	{
		int testId = 1;
		switch (testId)
		{
		case 0:
			test0();
			break;
		case 1:
			test1();
			break;
		}
	}	
}
