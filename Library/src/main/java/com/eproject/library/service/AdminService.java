package com.eproject.library.service;

import com.eproject.library.dto.AdminDTO;
import com.eproject.library.model.Admin;

public interface AdminService {
    Admin findByUsername(String username);

    Admin save(AdminDTO adminDTO);
}
