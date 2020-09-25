package com.example.fahad.crypto.service;

import com.example.fahad.crypto.domain.CoinType;
import com.example.fahad.crypto.domain.Order;
import org.junit.Test;

import java.util.Collection;
import java.util.Optional;

import static com.example.fahad.crypto.service.TestOrderData.*;
import static org.assertj.core.api.Assertions.*;

public class OrderRepositoryTest {

  @Test
  public void canSaveOrder() {
    // Given
    OrderRepository repository = new OrderRepository();

    // When
    Optional<Order> actual = repository.save(getTestBuyOrder());

    // Then
    assertThat(actual.get()).isEqualToIgnoringGivenFields(getTestBuyOrderBuilder().build(), "orderId");
  }

  @Test
  public void canFindOrderByOrderId() {
    // Given
    OrderRepository repository = new OrderRepository();
    Order expectedOrder = repository.save(getTestBuyOrder())
                                    .get();

    // When
    Optional<Order> actual = repository.findById(expectedOrder.getOrderId());

    // Then
    assertThat(actual.get()).isEqualTo(expectedOrder);
  }

  @Test
  public void canDeleteOrder() {
    // Given
    OrderRepository repository = new OrderRepository();
    Order order = getTestBuyOrderBuilder().build();
    Long expectedOrderId = repository.save(order)
                                     .get()
                                     .getOrderId();

    // When
    repository.delete(expectedOrderId);

    // Then
    assertThat(repository.findById(expectedOrderId)).isEmpty();
  }

  @Test
  public void canGetAllOrders() {
    // Given
    OrderRepository repository = new OrderRepository();
    Order order1 = repository.save(getTestBuyOrderBuilder().coinType(CoinType.Ethereum)
                                                           .build())
                             .get();
    Order order2 = repository.save(getTestBuyOrderBuilder().coinType(CoinType.Litecoin)
                                                           .build())
                             .get();
    Order order3 = repository.save(getTestBuyOrderBuilder().coinType(CoinType.Bitcoin)
                                                           .build())
                             .get();

    // When
    Optional<Collection<Order>> actual = repository.getAllOrders();

    // Then
    assertThat(actual.get()).usingElementComparatorIgnoringFields("orderId")
                            .containsExactlyInAnyOrder(order1, order2, order3);
  }

}