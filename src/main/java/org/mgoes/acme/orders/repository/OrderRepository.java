package org.mgoes.acme.orders.repository;

import org.mgoes.acme.orders.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    Order getOne(String id);
    Optional<Order> findById(String id);
    List<Order> findByCustomerIdOrderByCreatedAtDesc(String id);
    List<Order> findByOrderByCreatedAtDesc();
}
