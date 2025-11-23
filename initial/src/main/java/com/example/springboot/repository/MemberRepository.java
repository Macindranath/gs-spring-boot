package com.example.springboot.repository;

import com.example.springboot.model.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<Member, Long> {

       /**
     * This is a custom query method. Spring Data JPA will
     * automatically generate the SQL query:
     * "SELECT * FROM member WHERE archived_at IS NULL" for speed and efficiency.
     */
    List<Member> findByArchivedAtIsNull();

    /**
     * This one will automatically generate:
     * "SELECT * FROM member WHERE archived_at IS NOT NULL"
     */
    List<Member> findByArchivedAtIsNotNull();
}

