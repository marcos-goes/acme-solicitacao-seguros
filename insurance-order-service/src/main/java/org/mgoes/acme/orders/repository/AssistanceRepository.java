package org.mgoes.acme.orders.repository;

import org.mgoes.acme.orders.model.Assistance;
import org.mgoes.acme.orders.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssistanceRepository extends JpaRepository<Assistance, Integer> {
}
