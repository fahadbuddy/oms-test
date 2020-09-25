package com.example.fahad.crypto.orderboard;

import com.example.fahad.crypto.domain.*;
import com.example.fahad.crypto.service.OrderService;
import com.example.fahad.crypto.service.OrderSummarySorter;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Map;
import java.util.function.BiConsumer;

import static java.lang.String.*;

/**
 * As this is the library class that will be used, I have chosen to
 * provide PrintStream for the clients, and with a defaulted System.out print stream
 * for tests.
 */
public class LiveOrderBoard {

  private final OrderService service;

  public LiveOrderBoard(final OrderService service) {
    this.service = service;
  }

  public void printLiveOrdersToSystemOut() {
    printLiveOrders(System.out);
  }

  public void printLiveOrders(final PrintStream os) {

    final Map<OrderType, Map<CoinType, Map<BigDecimal, OrderSummary>>> orderSummary = service.getOrderSummary();
    os.println("Live Order Board");
    os.println("================");
    orderSummary.forEach((orderType, coinTypeToOrderSummaryMap) -> {
      os.println(format("%s ORDERS", orderType));
      os.println("===========");
      coinTypeToOrderSummaryMap.forEach((k, v) -> {
        os.println(format("COIN TYPE: %s", k));
        switch (orderType) {
          case BUY:
            OrderSummarySorter.sortByPriceAscending(v)
                              .forEach(printOrderInfo(orderType, os));
            break;
          case SELL:
            OrderSummarySorter.sortByPriceDescending(v)
                              .forEach(printOrderInfo(orderType, os));

        }
      });

    });


  }

  private BiConsumer<BigDecimal, OrderSummary> printOrderInfo(final OrderType orderType, PrintStream os) {

    return (price, summary) -> {
      os.println(format("%s: %s %s @ £%s", orderType, summary.getOrderQty(), summary.getCoinType(),
                        summary.getOrderPrice()));
    };
  }
}
