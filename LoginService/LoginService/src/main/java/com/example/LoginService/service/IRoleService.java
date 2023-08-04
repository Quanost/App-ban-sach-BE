package com.example.LoginService.service;



import com.example.LoginService.model.Role;
import com.example.LoginService.model.RoleName;

import java.util.Optional;

public interface IRoleService {
    Optional<Role> findByName(RoleName name);
}
