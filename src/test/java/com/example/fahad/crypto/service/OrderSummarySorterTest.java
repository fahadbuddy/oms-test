package com.example.fahad.crypto.service;

import com.example.fahad.crypto.domain.OrderSummary;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

public class OrderSummarySorterTest {

  @Test
  public void canSortByDescending() {
    // Given
    Map<BigDecimal, OrderSummary> inputSummaryMap = new HashMap<>();
    inputSummaryMap.put(new BigDecimal(13.6, MathContext.DECIMAL32), new OrderSummary());
    inputSummaryMap.put(new BigDecimal(14), new OrderSummary());
    inputSummaryMap.put(new BigDecimal(13.9, MathContext.DECIMAL32), new OrderSummary());

    // When
    Map<BigDecimal, OrderSummary> actual = OrderSummarySorter.sortByPriceDescending(inputSummaryMap);

    // Then
    assertThat(actual.keySet()).containsExactlyInAnyOrder(new BigDecimal(14, MathContext.DECIMAL32),
                                                          new BigDecimal(13.9, MathContext.DECIMAL32),
                                                          new BigDecimal(13.6, MathContext.DECIMAL32));
  }

  @Test
  public void canSortByAscending() {
    // Given
    Map<BigDecimal, OrderSummary> inputSummaryMap = new HashMap<>();
    inputSummaryMap.put(new BigDecimal(13.6, MathContext.DECIMAL32), new OrderSummary());
    inputSummaryMap.put(new BigDecimal(14), new OrderSummary());
    inputSummaryMap.put(new BigDecimal(13.9, MathContext.DECIMAL32), new OrderSummary());

    // When
    Map<BigDecimal, OrderSummary> actual = OrderSummarySorter.sortByPriceAscending(inputSummaryMap);

    // Then
    assertThat(actual.keySet()).containsExactlyInAnyOrder(new BigDecimal(13.6, MathContext.DECIMAL32),
                                                          new BigDecimal(13.9, MathContext.DECIMAL32),
                                                          new BigDecimal(14, MathContext.DECIMAL32));
  }

}