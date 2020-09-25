package com.example.fahad.crypto.data;

import com.example.fahad.crypto.domain.Order;
import com.example.fahad.crypto.service.OrderService;

import java.math.BigDecimal;
import java.math.MathContext;

import static com.example.fahad.crypto.domain.CoinType.*;
import static com.example.fahad.crypto.domain.OrderType.*;

public class TestOrdersForLiveOrderBoard {

   public static void  prepareOrders(final OrderService service) {

    service.placeOrder(getDefaultSellOrder(350.1, 13.6)
                               .build());
    service.placeOrder(getDefaultSellOrder(50.5, 14.0).userId("user2").

            build());
    service.placeOrder(getDefaultSellOrder(441.8, 13.9).
                                                               userId("user3").
                                                               build());
    service.placeOrder(getDefaultSellOrder(3.5, 13.6).
                                                             userId("user4").
                                                             build());


    service.placeOrder(getDefaultBuyOrder(300.5, 140.0).userId("user2").
            build());

    service.placeOrder(getDefaultBuyOrder(76.8, 130.87).
                                                               userId("user3").
                                                               build());
    service.placeOrder(getDefaultBuyOrder(3.5, 130.87).
                                                              userId("user4").
                                                              build());
  }

  private static Order.OrderBuilder getDefaultSellOrder(final double orderQty, final double orderPrice) {

    return Order.builder().coinType(Ethereum)
                .orderQty(new BigDecimal(orderQty, MathContext.DECIMAL32))
                .price(new BigDecimal(orderPrice, MathContext.DECIMAL32))
                .orderType(SELL)
                .userId("user1");
  }

  private static Order.OrderBuilder getDefaultBuyOrder(final double orderQty, final double orderPrice) {

    return Order.builder().coinType(Bitcoin)
                .orderQty(new BigDecimal(orderQty, MathContext.DECIMAL32))
                .price(new BigDecimal(orderPrice, MathContext.DECIMAL32))
                .orderType(BUY)
                .userId("user1");
  }
}


