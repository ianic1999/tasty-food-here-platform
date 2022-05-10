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
    @Query("select t from Table t " +
            "join t.waiter w " +
            "join t.bookings b " +
            "where w.id = :waiterId " +
            "and b.status = 'ACTIVE'")
    List<Table> findActiveForWaiter(Long waiterId);

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
            "   where ((b.time <= :startTime and b.time + (b.duration * interval '1 minute') > :startTime) " +
            "            or (b.time < :endTime and b.time + (b.duration * interval '1 minute') >= :endTime) " +
            "            or (:startTime <= b.time and :endTime > b.time) " +
            "            or (:startTime < b.time + (b.duration * interval '1 minute') and :endTime >= b.time + (b.duration * interval '1 minute')))" +
            "           and b.confirmed = true " +
            "           and b.status <> 'CLOSED'" +
            "      )")
    List<Table> getFreeTablesForTimeRange(@Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);

    @Query(nativeQuery = true, value = "select case when (count(*) = 0) then false else true end " +
            "from tables " +
            "where id not in " +
            "      (" +
            "          select table_id " +
            "          from bookings b " +
            "   where ((b.time <= :startTime and b.time + (b.duration * interval '1 minute') > :startTime) " +
            "            or (b.time < :endTime and b.time + (b.duration * interval '1 minute') >= :endTime) " +
            "            or (:startTime <= b.time and :endTime > b.time) " +
            "            or (:startTime < b.time + (b.duration * interval '1 minute') and :endTime >= b.time + (b.duration * interval '1 minute')))" +
            "           and b.confirmed = true " +
            "           and b.status <> 'CLOSED'" +
            "      )")
    boolean isTimeAvailable(@Param("startTime") LocalDateTime start,
                            @Param("endTime") LocalDateTime end);
}
