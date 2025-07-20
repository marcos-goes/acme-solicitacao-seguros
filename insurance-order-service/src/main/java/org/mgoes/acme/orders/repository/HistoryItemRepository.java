package org.mgoes.acme.orders.repository;

import org.mgoes.acme.orders.model.HistoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryItemRepository extends JpaRepository<HistoryItem, Integer> {
}
