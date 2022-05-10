package com.example.tfhbackend.repository;

import com.example.tfhbackend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select o from Order o " +
            "join o.booking b " +
            "join b.table t " +
            "where t.id = :tableId " +
            "and b.status = 'ACTIVE'")
    List<Order> findByTableId(@Param("tableId") Long tableId);
}
