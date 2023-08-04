package com.example.LoginService.service.imp;


import com.example.LoginService.model.Role;
import com.example.LoginService.model.RoleName;
import com.example.LoginService.repo.IRoleRepository;
import com.example.LoginService.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    IRoleRepository roleRepository;
    @Override
    public Optional<Role> findByName(RoleName name) {
        return roleRepository.findByName(name);
    }
}
