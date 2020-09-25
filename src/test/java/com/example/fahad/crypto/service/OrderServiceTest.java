package com.example.fahad.crypto.service;

import com.example.fahad.crypto.domain.*;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.example.fahad.crypto.domain.OrderType.*;
import static com.example.fahad.crypto.service.TestOrderData.*;
import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;


public class OrderServiceTest {

  private OrderRepository repository;
  private OrderService orderService;

  @Before
  public void setup() {

    repository = new OrderRepository();
    orderService = new OrderService(repository);
  }

  @Test
  public void canPlaceAnOrder() {
    // Given
    Order order = getTestBuyOrderBuilder().build();

    // When
    Long actualOrderId = orderService.placeOrder(order);

    // Then
    assertThat(repository.findById(actualOrderId)
                         .get()).isEqualToIgnoringGivenFields(order, "orderId");
  }

  @Test
  public void canDeleteASavedOrder() {
    // Given
    Order order = getTestBuyOrderBuilder().build();
    Order expectedOrder = repository.save(order)
                                    .get();

    // When
    orderService.cancelOrder(expectedOrder.getOrderId());

    // Then
    assertThat(repository.findById(expectedOrder.getOrderId())).isEqualTo(Optional.empty());
  }

  @Test
  public void canShowSummaryOfOneOrder() {
    // Given
    BigDecimal expectedPrice = new BigDecimal(55.0, MathContext.DECIMAL32);
    Order expectedOrder = repository.save(getTestBuyOrderBuilder().price(expectedPrice)
                                                                  .orderQty(
                                                                          new BigDecimal(100.1, MathContext.DECIMAL32))
                                                                  .build())
                                    .get();

    OrderSummary expectedOrderSummary = new OrderSummary(BUY, expectedOrder.getOrderQty(), expectedOrder.getPrice(),
                                                         expectedOrder.getCoinType());

    final Map<OrderType, Map<CoinType, Map<BigDecimal, OrderSummary>>> expectedSummaryObject = buildOrderSummaryMap(
            expectedPrice, BUY, expectedOrderSummary);
    // When
    final Map<OrderType, Map<CoinType, Map<BigDecimal, OrderSummary>>> actual = orderService.getOrderSummary();

    // Then
    assertThat(actual).isEqualTo(expectedSummaryObject);
  }


  @Test
  public void canShowSummaryOfTwoOrdersForSamePrice() {
    // Given
    BigDecimal expectedPrice = new BigDecimal(55.0, MathContext.DECIMAL32);
    Order order1 = repository.save(getTestBuyOrderBuilder().coinType(CoinType.Ethereum)
                                                           .orderQty(new BigDecimal(100.1, MathContext.DECIMAL32))
                                                           .price(expectedPrice)
                                                           .build())
                             .get();
    Order order2 = repository.save(getTestBuyOrderBuilder().coinType(CoinType.Ethereum)
                                                           .orderQty(new BigDecimal(150.2, MathContext.DECIMAL32))
                                                           .price(expectedPrice)
                                                           .build())
                             .get();

    OrderSummary expectedSummary = new OrderSummary(BUY, new BigDecimal(250.3, MathContext.DECIMAL32), expectedPrice,
                                                    CoinType.Ethereum);


    final Map<OrderType, Map<CoinType, Map<BigDecimal, OrderSummary>>> expectedSummaryObject = buildOrderSummaryMap(
            expectedPrice, BUY, expectedSummary);
    // When
    final Map<OrderType, Map<CoinType, Map<BigDecimal, OrderSummary>>> actual = orderService.getOrderSummary();

    // Then
    assertThat(actual).isEqualTo(expectedSummaryObject);

  }

  @Test
  public void canGroupForBuyAndSellOrdersSeparately() {
    // Given
    BigDecimal expectedPrice = new BigDecimal(55.0);
    Order buyOrder1 = repository.save(getTestBuyOrderBuilder().orderType(BUY)
                                                              .coinType(CoinType.Ethereum)
                                                              .orderQty(new BigDecimal(100.1, MathContext.DECIMAL32))
                                                              .price(expectedPrice)
                                                              .build())
                                .get();
    Order buyOrder2 = repository.save(getTestBuyOrderBuilder().orderType(BUY)
                                                              .coinType(CoinType.Ethereum)
                                                              .orderQty(new BigDecimal(150.2, MathContext.DECIMAL32))
                                                              .price(expectedPrice)
                                                              .build())
                                .get();

    Order sellOrder1 = repository.save(getTestBuyOrderBuilder().orderType(OrderType.SELL)
                                                               .coinType(CoinType.Bitcoin)
                                                               .orderQty(new BigDecimal(100.1, MathContext.DECIMAL32))
                                                               .price(expectedPrice)
                                                               .build())
                                 .get();
    Order sellOrder2 = repository.save(getTestBuyOrderBuilder().orderType(OrderType.SELL)
                                                               .coinType(CoinType.Bitcoin)
                                                               .orderQty(new BigDecimal(150.2, MathContext.DECIMAL32))
                                                               .price(expectedPrice)
                                                               .build())
                                 .get();

    OrderSummary expectedBuySummary = new OrderSummary(BUY, new BigDecimal(250.3, MathContext.DECIMAL32), expectedPrice,
                                                       CoinType.Ethereum);
    OrderSummary expectedSellSummary = new OrderSummary(OrderType.SELL, new BigDecimal(250.3, MathContext.DECIMAL32),
                                                        expectedPrice, CoinType.Bitcoin);

    final Map<OrderType, Map<CoinType, Map<BigDecimal, OrderSummary>>> expectedSummaryObject = buildOrderSummaryMap(
            expectedPrice, BUY, expectedBuySummary, expectedSellSummary);


    // When
    final Map<OrderType, Map<CoinType, Map<BigDecimal, OrderSummary>>> actual = orderService.getOrderSummary();

    // Then
    assertThat(actual).isEqualTo(expectedSummaryObject);
  }

  private Map<OrderType, Map<CoinType, Map<BigDecimal, OrderSummary>>> buildOrderSummaryMap(
          final BigDecimal expectedPrice, final OrderType orderType, final OrderSummary... expectedOrderSummaries) {


//    Map<OrderType, Map<CoinType, Map<BigDecimal, List<OrderSummary>>>> intermediate = Arrays.stream(expectedOrderSummaries)
//                                                                                       .collect(groupingBy(
//                                                                                               OrderSummary::getOrderType,
//                                                                                               groupingBy(
//                                                                                                       OrderSummary::getCoinType,
//                                                                                                       groupingBy(
//                                                                                                               OrderSummary::getOrderPrice,
//                                                                                                               toList());
//



        Map<OrderType, Map<CoinType, Map<BigDecimal, OrderSummary>>> result = new HashMap();

        for (final OrderSummary expectedOrderSummary : expectedOrderSummaries) {
          if (result.get(expectedOrderSummary.getOrderType()) == null) {
            result.put(expectedOrderSummary.getOrderType(), new HashMap<>());
          }
          Map<CoinType, Map<BigDecimal, OrderSummary>> expectedCoinTypeToOrderSummaryMap = result.get(expectedOrderSummary.getOrderType());

          if (expectedCoinTypeToOrderSummaryMap.get(expectedOrderSummary.getCoinType()) == null) {
            expectedCoinTypeToOrderSummaryMap.put(expectedOrderSummary.getCoinType(), new HashMap<>());
          }

          Map<BigDecimal, OrderSummary> priceToOrderSummaryMap = expectedCoinTypeToOrderSummaryMap.get(expectedOrderSummary.getOrderPrice());

          if (priceToOrderSummaryMap == null) {
            priceToOrderSummaryMap = new HashMap<>();
          }

          priceToOrderSummaryMap.put(expectedOrderSummary.getOrderPrice(), expectedOrderSummary);
          expectedCoinTypeToOrderSummaryMap.put(expectedOrderSummary.getCoinType(), priceToOrderSummaryMap);
          result.put(expectedOrderSummary.getOrderType(), expectedCoinTypeToOrderSummaryMap);
        }
        return result;


  }
}