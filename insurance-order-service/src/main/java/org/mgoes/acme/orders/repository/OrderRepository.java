package org.mgoes.acme.orders.repository;

import org.mgoes.acme.orders.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    Order getOne(String id);


//    @Query("select o from Order o where u.firstname like %?1")

    Optional<Order> findById(String id);



    List<Order> findByCustomerIdOrderByCreatedAtDesc(String id);
    List<Order> findByOrderByCreatedAtDesc();
}
