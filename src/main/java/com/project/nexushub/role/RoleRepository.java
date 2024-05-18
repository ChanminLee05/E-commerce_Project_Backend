package com.project.nexushub.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Role r WHERE r.roleName = :roleType")
    boolean existsRoleByRoleName(@Param("roleType") RoleType roleType);

    Optional<Role> findByRoleName(RoleType roleType);
}
