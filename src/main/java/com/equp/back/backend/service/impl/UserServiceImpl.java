package com.equp.back.backend.service.impl;

import com.equp.back.backend.model.Role;
import com.equp.back.backend.model.Status;
import com.equp.back.backend.model.User;
import com.equp.back.backend.repository.RoleRepository;
import com.equp.back.backend.repository.UserRepository;
import com.equp.back.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void create(User user) {

        Role roleUser = roleRepository.findByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);
        user.setStatus(Status.ACTIVE);

        User registeredUser = userRepository.save(user);
        log.info("IN register - user: {} successfully registered", registeredUser);
    }

    @Override
    public List<User> readAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).get();
        }


    @Override
    public boolean update(User user, String newPassword) {
        if (userRepository.existsById(user.getId())) {
            user.setPassword(newPassword);
            userRepository.save(user);
            return true;
        }

        return false;
    }

    @Override
    public boolean updateName(User user, String newName) {
        if (userRepository.existsById(user.getId())) {
            user.setName(newName);
            userRepository.save(user);
            return true;
        }

        return false;
    }

    @Override
    public boolean delete(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public User findByEmail(String email){

        return userRepository.findByEmail(email);
    }

    @Override
    public User findByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public boolean updateSubscription(User user,String subscriptionValue) {
        if (userRepository.existsById(user.getId())) {
            user.setSubscription(subscriptionValue);
            userRepository.save(user);
            return true;
        }
        return false;
    }

}
