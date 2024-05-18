package com.project.nexushub.role;

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
        initializeRole(RoleType.ADMIN);
        initializeRole(RoleType.USER);
    }

    private void initializeRole(RoleType roleType) {
        if (!roleRepository.existsRoleByRoleName(roleType)) {
            Role role = new Role(roleType);
            roleRepository.save(role);
            System.out.println("Role {} created successfully" + roleType);
        } else {
            System.out.println("Role {} already exists" + roleType);
        }
    }
}
