package com.example.tfhbackend.repository;

import com.example.tfhbackend.model.Table;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TableRepository extends JpaRepository<Table, Long> {
    List<Table> findByWaiter_Id(Long waiterId);
}
