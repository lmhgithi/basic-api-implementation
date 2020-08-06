package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.entity.RsEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {
    List<UserEntity> findAll();
    List<UserEntity> findAllByName(String name);
    Optional<UserEntity> findById(Integer index);
}
