package com.example.fahad.crypto;

import com.example.fahad.crypto.data.TestOrdersForLiveOrderBoard;
import com.example.fahad.crypto.orderboard.LiveOrderBoard;
import com.example.fahad.crypto.service.OrderRepository;
import com.example.fahad.crypto.service.OrderService;

/**
 * The main method replicates the same test as LiveOrderBoardTest
 */
public class CryptoSolutionApplication {

	public static void main(String[] args) {

		OrderService service = new OrderService(new OrderRepository());
		TestOrdersForLiveOrderBoard.prepareOrders(service);

		LiveOrderBoard orderBoard = new LiveOrderBoard(service);

		orderBoard.printLiveOrdersToSystemOut();

	}


}
