package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.entity.VoteEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;
import java.util.List;

public interface VoteRepository extends CrudRepository<VoteEntity, Integer> {
    List<VoteEntity> findAll();
//    @Query(value = "SELECT * FROM vote WHERE Timestamp BETWEEN ?1 and ?2", nativeQuery = true)
    List<VoteEntity> findByTimestampBetween(Timestamp start, Timestamp end);
}
