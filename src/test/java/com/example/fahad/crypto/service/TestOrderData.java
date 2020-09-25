package com.example.fahad.crypto.service;

import com.example.fahad.crypto.domain.*;

import java.math.BigDecimal;

public class TestOrderData {

   static Order getTestBuyOrder() {
    return getTestBuyOrderBuilder()
                .build();
  }

  static Order.OrderBuilder getTestBuyOrderBuilder() {
    return Order.builder()
                .userId("matt")
                .coinType(CoinType.Ethereum)
                .orderType(OrderType.BUY)
                .orderQty(new BigDecimal(100.0))
                .price(new BigDecimal(10));
  }
}
