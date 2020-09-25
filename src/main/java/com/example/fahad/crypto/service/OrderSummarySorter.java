package com.example.fahad.crypto.service;

import com.example.fahad.crypto.domain.OrderSummary;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.*;

/**
 * Separate Helper class for Sorting. Used by the LiveOrderBoard.
 */
public class OrderSummarySorter {
  public static Map<BigDecimal, OrderSummary> sortByPriceDescending(
          final Map<BigDecimal, OrderSummary> inputSummaryMap) {

    return sortBy(unmodifiableMap(inputSummaryMap), Comparator.naturalOrder());

  }

  public static Map<BigDecimal, OrderSummary> sortByPriceAscending(
          final Map<BigDecimal, OrderSummary> inputSummaryMap) {

    return sortBy(unmodifiableMap(inputSummaryMap), Comparator.naturalOrder());
  }

  private static Map<BigDecimal, OrderSummary> sortBy(final Map<BigDecimal, OrderSummary> result,
                                                      final Comparator<BigDecimal> keyComparator) {

    return result.entrySet()
                 .stream()
                 .sorted(Comparator.comparing(e -> e.getKey(), keyComparator))
                 .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue,
                                           LinkedHashMap::new));
  }
}
