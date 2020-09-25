package com.example.fahad.crypto.service;

import com.example.fahad.crypto.domain.Order;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Basic ConcurrentHashMap based storage repository. Alternatively can
 * also be backed using spring-data-jpa with repository.
 */
public class OrderRepository {

  private final Map<Long, Order> storage = new ConcurrentHashMap<>();
  private final AtomicLong idGenerator = new AtomicLong(1);

  Optional<Order> findById(Long id) {
    if (id == null) return Optional.empty();
    return Optional.ofNullable(storage.get(id));
  }

  public Optional<Order> save(final Order order) {
    long nextId = idGenerator.getAndIncrement();
    Order orderWithIdSet = Order.from(order, nextId);
    storage.put(nextId, orderWithIdSet);
    return Optional.of(orderWithIdSet);
  }

  public void delete(final Long orderId) {
    if (orderId != null) {
      storage.remove(orderId);
    }
  }

  public Optional<Collection<Order>> getAllOrders() {
    return Optional.of(storage.values());
  }
}
