package com.theshan.product_management.service;

import com.theshan.product_management.entity.Role;
import com.theshan.product_management.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    @Transactional
    public void createRole(Role role) {
        role.setCreatedAt(new Date());
        this.roleRepository.save(role);
    }

    public Role findById(int roleId) {
        Optional<Role> roleById = this.roleRepository.findById(roleId);

        if (roleById.isEmpty()) {
            throw new IllegalStateException("Role not found.");
        }

        return roleById.get();
    }

    public Role findByName(String name) {
        Optional<Role> roleById = this.roleRepository.findByName(name);

        if (roleById.isEmpty()) {
            throw new IllegalStateException("Role not found.");
        }

        return roleById.get();
    }
}
