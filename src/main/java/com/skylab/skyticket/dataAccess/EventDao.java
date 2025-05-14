package com.skylab.skyticket.dataAccess;

import com.skylab.skyticket.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface EventDao extends JpaRepository<Event, UUID> {
    @Query("SELECT DISTINCT e FROM Event e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(e.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Set<Event> searchByNameOrDescription(@Param("keyword") String keyword);
}
