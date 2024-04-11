package com.project.nexushub.service;

import com.project.nexushub.entity.Role;
import com.project.nexushub.entity.RoleType;
import com.project.nexushub.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void initializeRoles() {
        if(!roleRepository.existsRoleByRoleName(RoleType.ADMIN)) {
            Role adminRole = new Role(RoleType.ADMIN);
            roleRepository.save(adminRole);
        } else {
            throw new IllegalArgumentException("ADMIN role already exists");
        }

        if (!roleRepository.existsRoleByRoleName(RoleType.USER)) {
            Role userRole = new Role(RoleType.USER);
            roleRepository.save(userRole);
        } else {
            throw new IllegalArgumentException("USER role already exists");
        }
    }
}
