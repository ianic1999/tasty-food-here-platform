package com.example.tfhbackend.repository;

import com.example.tfhbackend.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByReferenceId(String referenceId);

    @Query("select b from Booking b where b.status = 'ACTIVE'")
    List<Booking> findAllActive();

    @Query("select b from Booking b " +
            "join b.table t " +
            "where t.id = :tableId " +
            "and b.status = 'ACTIVE'")
    Optional<Booking> getActiveForTable(@Param("tableId") Long tableId);

    @Query("select b from Booking b where b.status = 'UPCOMING' and b.confirmed = true")
    List<Booking> findAllUpcoming();

    @Query("select b from Booking b where b.status = 'ACTIVE' and b.table.id = :tableId")
    Optional<Booking> findActiveForTable(@Param("tableId") Long tableId);
}
