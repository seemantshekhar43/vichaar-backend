package com.seemantshekhar.vichhar.repository;

import com.seemantshekhar.vichhar.beans.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("SELECT u FROM UserEntity u WHERE u.name = :name OR u.email = :email")
    List<UserEntity> findByNameOrEmail(@Param("name") String name, @Param("email") String email);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByName(String name);
}
