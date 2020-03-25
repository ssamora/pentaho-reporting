package org.pentaho.plugin.jfreereport.reportcharts;

import org.junit.Before;
import org.junit.Test;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class PieChartExpressionTest {

  List<BigDecimal> values = new ArrayList<>(  );

  @Before
  public void setup() {
    values.add(BigDecimal.valueOf( 4091419.8699999979 ));
    values.add(BigDecimal.valueOf( 1274125.19000000056 ));
    values.add(BigDecimal.valueOf( 1076757.11999999948 ));
    values.add(BigDecimal.valueOf( 748670.8500000001 ));
    values.add(BigDecimal.valueOf( 234469.189999999974 ));
    values.add(BigDecimal.valueOf( 1154280.56999999992 ));
    values.add(BigDecimal.valueOf( 2066226.3899999995 ));
  }

  @Test
  public void test() {

    createItemArray();
  }

  protected void createItemArray() {


    final DecimalFormat percentFormat =
      new DecimalFormat( "0.0000%", new DecimalFormatSymbols( Locale.getDefault() ) );
    percentFormat.setRoundingMode( RoundingMode.HALF_UP);
    double total = calculatePieDatasetTotal();
    double totalPercent = 0.0;
    double totalFormatedPercent = 0.0;
    System.out.println( "TOTAL: " + total );

    for(BigDecimal value : values) {

      double percent = 0.0D;
      if (value != null) {
        double v = value.doubleValue();
        if (v > 0.0D) {
          percent = v / total;
        }
      }

      System.out.println( percent );
      String formatedPercent = percentFormat.format( percent );
      System.out.println( formatedPercent );

      totalPercent += percent;
      totalFormatedPercent += Double.valueOf( formatedPercent.substring( 0, formatedPercent.length() - 1 ) );
    }

    System.out.println( "TOTAL PERCENT: " + totalPercent);
    System.out.println( "TOTAL FORMATTED PERCENT: " + totalFormatedPercent);

  }

  public double calculatePieDatasetTotal() {


    double totalValue = 0.0D;
    Iterator iterator = values.iterator();

    while(iterator.hasNext()) {

      BigDecimal value = (BigDecimal) iterator.next();
        double v = 0.0D;
        if (value != null) {
          v = value.doubleValue();
        }

        if (v > 0.0D) {
          totalValue += v;
        }

    }

    return totalValue;
  }
}
