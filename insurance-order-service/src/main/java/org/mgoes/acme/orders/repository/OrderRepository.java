package org.mgoes.acme.orders.repository;

import org.mgoes.acme.orders.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    Optional<Order> findById(UUID id);
    List<Order> findByCustomerIdOrderByCreatedAtDesc(UUID id);
    List<Order> findByOrderByCreatedAtDesc();
}
