package com.example.fahad.crypto.service;

import com.example.fahad.crypto.domain.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

/**
 * This is the class with the core logic of the application.
 * I have built this with Spring in mind, although I don't wire
 * up any classes per se. See tests / main for usages.
 */
public class OrderService {

  private final OrderRepository repository;

  public OrderService(final OrderRepository repository) {
    this.repository = repository;
  }

  public Long placeOrder(final Order order) {
    return repository.save(order)
                     .map(Order::getOrderId)
                     .orElseThrow(() -> new IllegalArgumentException("error while saving order"));
  }

  public void cancelOrder(final Long orderId) {
    repository.delete(orderId);
  }

  /**
   * I feel this implementation has got very unwieldy. Java map grouping operations are horrible
   * with all the static typing. Probs, would look nicer in clojure.
   * @return
   */
  public Map<OrderType, Map<CoinType, Map<BigDecimal, OrderSummary>>> getOrderSummary() {

    final Map<OrderType, Map<CoinType, Map<BigDecimal, List<Order>>>> groupedByOrderTypeAndCoinTypeAndPrice = repository.getAllOrders()
                                                                                                             .orElse(emptyList())
                                                                                                             .stream()
                                                                                                             .collect(
                                                                                                                     groupingBy(
                                                                                                                             Order::getOrderType,
                                                                                                                             groupingBy(
                                                                                                                                     Order::getCoinType,
                                                                                                                                     groupingBy(
                                                                                                                                             Order::getPrice))));


    return groupedByOrderTypeAndCoinTypeAndPrice.entrySet()
                              .stream()
                              .collect(Collectors.toMap((e) -> e.getKey(),
                                                        (e) -> aggregateByCoinType(e.getValue(), e.getKey())));


  }

  private Map<CoinType, Map<BigDecimal, OrderSummary>> aggregateByCoinType(final Map<CoinType, Map<BigDecimal, List<Order>>> coinTypeToOrderMap,
                                                                           final OrderType type) {

    return coinTypeToOrderMap.entrySet()
            .stream()
            .collect(Collectors.toMap(e -> e.getKey(), e -> aggregateByPrice(e.getKey(), e.getValue(), type)));
  }

  private Map<BigDecimal, OrderSummary> aggregateByPrice(final CoinType coinType, final Map<BigDecimal, List<Order>> orderMap,
                                                         final OrderType type) {

    return orderMap.entrySet()
                   .stream()
                   .collect(Collectors.toMap(e -> e.getKey(), e -> aggregateOrderQtys(e.getKey(), e.getValue(), type, coinType)));
  }

  private OrderSummary aggregateOrderQtys(final BigDecimal price, final List<Order> value, final OrderType orderType,
                                          final CoinType coinType) {
    BigDecimal orderQty = value.stream().map(Order::getOrderQty).reduce(BigDecimal.ZERO, BigDecimal::add);

    return new OrderSummary(orderType, orderQty, price, coinType);
  }



}
