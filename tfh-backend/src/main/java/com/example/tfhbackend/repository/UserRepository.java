package com.example.tfhbackend.repository;

import com.example.tfhbackend.model.User;
import com.example.tfhbackend.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhone(String phone);
    Optional<User> findByEmail(String email);
    long countAllByRole(UserRole role);

    @Query(value = "select u.* from users u " +
            "where u.role = 'WAITER' " +
            "and u.id not in (" +
            " select w.id from bookings b " +
            "   join users w on b.waiter_id = w.id " +
            "   where ((b.time <= :start and b.time + (b.duration * interval '1 minute') > :start) " +
            "            or (b.time < :endTime and b.time + (b.duration * interval '1 minute') >= :endTime) " +
            "            or (:start <= b.time and :endTime > b.time) " +
            "            or (:start < b.time + (b.duration * interval '1 minute') and :endTime >= b.time + (b.duration * interval '1 minute')))" +
            "   and b.status <> 'CLOSED' " +
            ")", nativeQuery = true)
    List<User> getFreeWaitersForTimeRange(@Param("start") LocalDateTime start, @Param("endTime") LocalDateTime end);

    @Query(value = "select u.* from bookings b " +
            "   join users u on b.waiter_id = u.id " +
            "   where ((b.time <= :start and b.time + (b.duration * interval '1 minute') > :start) " +
            "            or (b.time < :endTime and b.time + (b.duration * interval '1 minute') >= :endTime) " +
            "            or (:start <= b.time and :endTime > b.time) " +
            "            or (:start < b.time + (b.duration * interval '1 minute') and :endTime >= b.time + (b.duration * interval '1 minute')))" +
            "   and b.status <> 'CLOSED'", nativeQuery = true)
    List<User> getAllWaitersForTimeRage(@Param("start") LocalDateTime start, @Param("endTime") LocalDateTime end);
}
