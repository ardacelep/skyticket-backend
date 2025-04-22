package com.skylab.skyticket.dataAccess;

import com.skylab.skyticket.entities.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CertificateDao extends JpaRepository<Certificate,UUID> {

    List<Certificate> findByOwners_Id(UUID userId);
    List<Certificate> findAllByEventId(UUID eventId);


}
