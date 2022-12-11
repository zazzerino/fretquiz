package com.kdp.fretquiz.user;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findBySessionId(String sessionId);

    @Modifying
    @Query("""
        DELETE FROM "user" WHERE "user"."session_id" = :sessionId""")
    int deleteBySessionId(@Param("sessionId") String sessionId);
}
