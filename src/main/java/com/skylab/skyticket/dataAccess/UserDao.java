package com.skylab.skyticket.dataAccess;

import com.skylab.skyticket.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserDao extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END " +
            "FROM User u JOIN u.certificates c " +
            "WHERE u.id = :userId AND c.id = :certificateId")
    boolean existsUserHasCertificate(@Param("userId") UUID userId,
                                     @Param("certificateId") UUID certificateId);


}
