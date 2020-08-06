package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.entity.RsEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RsRepository extends CrudRepository<RsEntity, Integer> {
    List<RsEntity> findAll();
    Optional<RsEntity> findById(Integer index);
}
