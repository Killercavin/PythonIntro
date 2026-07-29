package io.github.devcavin.wattwise.alertservice.repository;

import io.github.devcavin.wattwise.alertservice.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
}
