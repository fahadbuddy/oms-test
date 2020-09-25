package com.example.fahad.crypto.domain;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {

  private Long orderId;
  private String userId;
  private OrderType orderType;
  private CoinType coinType;
  private BigDecimal orderQty;
  private BigDecimal price;

  /**
   * This is a static Order constructor to create a new Order
   * object from an existing order, with a different id.
   * @param order
   * @param id
   * @return
   */
  public static Order from(final Order order, final long id) {
    return Order.builder()
            .orderId(id)
            .userId(order.getUserId())
            .orderType(order.getOrderType())
            .coinType(order.getCoinType())
            .orderQty(order.getOrderQty())
            .price(order.getPrice())
            .build();
  }
}
