package com.example.tfhbackend.repository;

import com.example.tfhbackend.model.Table;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepository extends JpaRepository<Table, Long> {
}
