package com.example.demo.repository;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByRole(Role role);

    /** Used by the Admin Panel "Total Users" card (Task 5) — counts CLIENT accounts. */
    long countByRole(Role role);

    /**
     * Real-time client search for the admin "book on behalf of a client"
     * flow (Task 4). Only returns CLIENT-role accounts — an admin can't
     * book a car against another admin's account.
     *
     * The role is passed as a bind parameter rather than written as an
     * enum literal in the query string: Hibernate's HQL parser is picky
     * about the exact syntax for enum literals (and a fully-qualified
     * class name is definitely not it), so a parameter sidesteps the
     * issue entirely and works the same way across Hibernate versions.
     */
    @Query("SELECT u FROM User u WHERE u.role = :role AND "
            + "(LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) "
            + "OR u.phone LIKE CONCAT('%', :keyword, '%'))")
    List<User> searchClients(@Param("keyword") String keyword, @Param("role") Role role);
}