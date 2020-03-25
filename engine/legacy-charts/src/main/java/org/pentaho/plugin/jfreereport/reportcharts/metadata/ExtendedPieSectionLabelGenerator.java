/*!
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 * or from the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright (c) 2002-2020 Hitachi Vantara..  All rights reserved.
 */
package org.pentaho.plugin.jfreereport.reportcharts.metadata;

import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.PieDataset;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExtendedPieSectionLabelGenerator extends StandardPieSectionLabelGenerator {

  public ExtendedPieSectionLabelGenerator( String labelFormat, NumberFormat numberFormat, NumberFormat percentFormat) {
    super( labelFormat, numberFormat, percentFormat );
  }

  protected Object[] createItemArray( PieDataset dataset, Comparable key ) {
    if ( getNumberFormat().getRoundingMode() != RoundingMode.FLOOR ) {
      return super.createItemArray( dataset, key );
    }
    Object[] result = new Object[4];
    double total = DatasetUtilities.calculatePieDatasetTotal( dataset );
    result[0] = key.toString();
    Number value = dataset.getValue( key );
    if ( value != null ) {
      result[1] = getNumberFormat().format( value );
    } else {
      result[1] = "null";
    }
    double percent = 0.0D;
    if ( value != null ) {
      double v = value.doubleValue();
      if ( v > 0.0D ) {
        percent = v / total;
      }
    }

    //Applying "Largest remainder method"
    //1 - Rounding all values down to the nearest integer value;
    //2 - Determining the difference between the sum of the rounded values and total value;
    //3 - Distributing the difference between the rounded values in decreasing order of their decimal parts.
    percent = getLargestRemainderMethodPercent( dataset, key, total, percent );

    result[2] = getPercentFormat().format( percent );
    result[3] = getNumberFormat().format( total );
    return result;
  }

  private double getLargestRemainderMethodPercent( PieDataset dataset, Comparable key, double total, double percent ) {
    Map<Comparable, Double> values = new HashMap<>();
    Map<Comparable, Integer> roundedValues = new HashMap<>();
    final int totalPercentage = 100;
    final int correctFactor = 1;

    //1st: Round all elements down
    initValuesRoundedDown( dataset, total, values, roundedValues );

    //2nd: Now get the difference between the sum of the rounded values and total value (100%)
    int diff = totalPercentage - roundedValues.values().stream().reduce( 0, (subtotal, element) -> subtotal + element );

    //3rd: Distribute the difference across our rounded percentages. (part 1)
    // This distribution should be done in decreasing order of the values’ decimal part, so let’s first sort them by their decimal part
    Map<Comparable, Double> sortedValues = values.entrySet().stream()
      .sorted( (element1, element2) -> ( Double.valueOf( Math.abs( Math.floor( element2.getValue() ) - element2.getValue() ) )
                                        .compareTo( Math.abs( Math.floor( element1.getValue() ) - element1.getValue() ) ) ) )
      .collect( Collectors
        .toMap( Map.Entry::getKey, Map.Entry::getValue, ( element1, element2 ) -> element1, LinkedHashMap::new ) );


    //4th: Distribute the difference across our rounded percentages. (part 2)
    //     Finally distribute the remaining diff. If the current value we are now calculating is in range of the calculated
    //     difference and the total, then its value is corrected
    boolean correctPercentage = shouldValueCorrectPercentage( key, diff, sortedValues );
    if ( correctPercentage ) {
      percent = ( percent * totalPercentage + correctFactor ) / totalPercentage;
    }
    return percent;
  }

  /*
   * Checks if the percent value should be corrected according to the "Largest remainder method"
   * @param key the key of the value being checked
   * @param diff the amount of difference between 100% and the sum of all percentages rounded down
   * @param sortedValues all percentages sorted from highest decimal part to lowest decimal part
   */
  private boolean shouldValueCorrectPercentage( Comparable key, int diff, Map<Comparable, Double> sortedValues ) {
    boolean correctPercentage = false;
    Iterator<Map.Entry<Comparable, Double>> iter = sortedValues.entrySet().iterator();
    while ( iter.hasNext() && diff > 0 ) {
      if ( iter.next().getKey().equals( key ) ) {
        correctPercentage = true;
        break;
      }
      --diff;
    }
    return correctPercentage;
  }

  /*
   *
   * Initializes the values and rounded values maps with the current pie chart values and rounded down values respectively
   * @param dataset the current pie chart dataset
   * @param total the total sum of the values in the dataset
   * @param values map of pie chart keys and values
   * @param roundedValues map of pie chart keys and rounded down values
   */
  private void initValuesRoundedDown( PieDataset dataset, double total, Map<Comparable, Double> values,
                                      Map<Comparable, Integer> roundedValues ) {
    List keys = dataset.getKeys();
    Iterator iterator = keys.iterator();

    while ( iterator.hasNext() ) {
      Comparable current = (Comparable) iterator.next();
      if ( current != null ) {
        Number value1 = dataset.getValue(current);
        if ( value1 != null ) {
          roundedValues.put( current, Double.valueOf( Math.floor( ( value1.doubleValue() / total ) * 100  ) ).intValue() );
          values.put( current, Double.valueOf( value1.doubleValue() ) );
        }
      }
    }
  }

}
