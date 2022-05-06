package com.example.tfhbackend.repository;

import com.example.tfhbackend.model.Table;
import com.example.tfhbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TableRepository extends JpaRepository<Table, Long> {
    List<Table> findByWaiter_Id(Long waiterId);

    Optional<Table> findByOrdinalNumber(int ordinalNumber);

    @Query("select w from Table t " +
            "   join t.bookings b " +
            "   join t.waiter w " +
            "where b.status = 'ACTIVE' " +
            "   and t.ordinalNumber = :tableNumber")
    Optional<User> getCurrentWaiter(@Param("tableNumber") Integer tableNumber);

    @Query(nativeQuery = true, value = "select * " +
            "from tables " +
            "where id not in " +
            "      (" +
            "          select table_id " +
            "          from bookings b " +
            "          where ((b.time <= :startTime and b.time + (b.duration * interval '1 minute') >= :endTime) " +
            "             or (b.time >= :startTime and b.time + (b.duration * interval '1 minute') <= :endTime) " +
            "             or (b.time < :startTime and b.time + (b.duration * interval '1 minute') > :startTime) " +
            "             or (b.time > :startTime and b.time < :endTime)) " +
            "           and b.confirmed = true " +
            "      )")
    List<Table> getFreeTablesForTimeRange(@Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);


}
