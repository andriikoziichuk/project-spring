package com.eproject.library.service.impl;

import com.eproject.library.dto.AdminDTO;
import com.eproject.library.model.Admin;
import com.eproject.library.repository.AdminRepository;
import com.eproject.library.repository.RoleRepository;
import com.eproject.library.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AdminServiceImpl implements AdminService {
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Admin findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    @Override
    public Admin save(AdminDTO adminDTO) {
        Admin admin = new Admin();
        admin.setFirstName(adminDTO.getFirstName());
        admin.setLastName(adminDTO.getLastName());
        admin.setUsername(adminDTO.getUsername());
        admin.setPassword(adminDTO.getPassword());
        admin.setRoles(Collections.singletonList(roleRepository.findByName("ADMIN")));

        return adminRepository.save(admin);
    }
}
