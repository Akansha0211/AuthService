package org.example.repository;

import org.example.entities.RefreshToken;
import org.example.entities.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Integer>{

    /*
    * SELECT * from tokens where token = token_1
    * */
    Optional<RefreshToken> findByToken(String token_1);
}
