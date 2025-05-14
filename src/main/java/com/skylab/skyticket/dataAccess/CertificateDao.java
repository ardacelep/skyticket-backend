package com.skylab.skyticket.dataAccess;

import com.skylab.skyticket.entities.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface CertificateDao extends JpaRepository<Certificate,UUID> {

    List<Certificate> findByOwners_Id(UUID userId);
    List<Certificate> findAllByEventId(UUID eventId);

    @Query("SELECT DISTINCT c FROM Certificate c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(c.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    Set<Certificate> searchByNameOrDescription(@Param("query") String query);


}
