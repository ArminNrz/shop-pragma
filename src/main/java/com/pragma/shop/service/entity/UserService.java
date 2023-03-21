package com.pragma.shop.service.entity;

import com.pragma.shop.constant.Constant;
import com.pragma.shop.dto.UserCreateDto;
import com.pragma.shop.entity.AppUser;
import com.pragma.shop.entity.Role;
import com.pragma.shop.mapper.AppUserMapper;
import com.pragma.shop.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final AppUserRepository repository;
    private final AppUserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        AppUser user = this.getByPhoneNumber(phoneNumber);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));

        return new User(user.getPhoneNumber(), user.getPassword(), authorities);
    }

    public AppUser getByPhoneNumber(String phoneNumber) {
        log.debug("Try to find User by phone number: {}", phoneNumber);

        Optional<AppUser> appUserOptional = repository.findByPhoneNumber(phoneNumber);
        if (appUserOptional.isEmpty()) {
            log.warn("No such User exist with phone number: {}", phoneNumber);
            return null;
        }

        log.debug("Found user: {}", appUserOptional.get());
        return appUserOptional.get();
    }

    @Transactional
    public void create(UserCreateDto createDto) {
        AppUser user = mapper.toEntity(createDto);
        user.setPassword(passwordEncoder.encode(createDto.getPassword()));
        this.grantUserRole(user);
        try {
            user = repository.save(user);
        } catch (DataIntegrityViolationException e) {
            log.error("This phone number is iterable, phoneNumber: {}", createDto.getPhoneNumber());
            throw Problem.valueOf(Status.NOT_ACCEPTABLE, Constant.APP_USER_PHONE_NUMBER_ITERATED);
        }
        log.info("Saved user: {}", user);
    }

    private void grantUserRole(AppUser user) {
        Role userRole = roleService.get("ROLE_USER");
        user.getRoles().add(userRole);
        log.debug("Grant role user to user: {}", user);
    }
}
