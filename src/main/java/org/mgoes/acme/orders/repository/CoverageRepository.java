package org.mgoes.acme.orders.repository;

import org.mgoes.acme.orders.model.Coverage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoverageRepository extends JpaRepository<Coverage, Integer> {
}
