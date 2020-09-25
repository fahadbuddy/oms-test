package com.example.fahad.crypto.domain;

import lombok.*;

import java.math.BigDecimal;

/**
 * This class is used to hold aggregated Order information
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderSummary {
  private OrderType orderType;
  private BigDecimal orderQty;
  private BigDecimal orderPrice;
  private CoinType coinType;
}
