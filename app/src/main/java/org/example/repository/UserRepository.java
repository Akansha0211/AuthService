package org.example.repository;

import org.example.entities.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/*
* Creating Repository for dealing with SQL queries
* CrudRepository<T, Id> : T --> UserInfo(TABLE), id --> table id
* */
@Repository
public interface UserRepository extends CrudRepository<UserInfo, Long> {
    public UserInfo findUsername(String username);

}
