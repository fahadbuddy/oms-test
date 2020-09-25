package com.example.fahad.crypto.orderboard;

import com.example.fahad.crypto.service.OrderRepository;
import com.example.fahad.crypto.service.OrderService;
import org.junit.Test;

import static com.example.fahad.crypto.data.TestOrdersForLiveOrderBoard.*;

public class LiveOrderBoardTest {

  @Test
  public void canPrintOrderBoard() {
    // Given
    OrderService service = new OrderService(new OrderRepository());
    prepareOrders(service);

    LiveOrderBoard orderBoard = new LiveOrderBoard(service);

    // When
    orderBoard.printLiveOrdersToSystemOut();
  }


}