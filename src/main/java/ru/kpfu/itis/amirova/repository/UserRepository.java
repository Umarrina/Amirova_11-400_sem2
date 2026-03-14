package ru.kpfu.itis.amirova.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.amirova.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.username = :username")
    Optional<User> findByUsernameWithRoles(@Param("username") String username);

    @Query(value = "select u from User u where u.username = :username")
    Optional<User> getByUsername(String username);

    @Query(value = "select * from users u where u.username = ?1", nativeQuery = true)
    Optional<User> getByUsernameNative(String username);

    @Modifying
    @Query(value = "insert into users (username) values (:#{#entity.username})", nativeQuery = true)
    void create(@Param("entity") User entity);

    @Modifying
    @Query(value = "update users set username = :#{#entity.username} where id = :#{#entity.id}", nativeQuery = true)
    void update(@Param("entity") User entity);

    @Override
    void delete(User entity);
}