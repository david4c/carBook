package com.example.carbook.service;

import com.example.carbook.config.security.dto.CustomUserDetails;
import com.example.carbook.exception.UserAlreadyExistsException;
import com.example.carbook.exception.UserNotFoundException;
import com.example.carbook.mapper.UserRequestMapper;
import com.example.carbook.mapper.UserResponseMapper;
import com.example.carbook.model.dto.UserRequest;
import com.example.carbook.model.dto.UserResponse;
import com.example.carbook.model.entity.User;
import com.example.carbook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService, CRUDService<User, Long> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserResponseMapper userResponseMapper;
    private final UserRequestMapper userRequestMapper;

    public User findById(final Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User save(final User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(final Long id, final User user) {
        User currentUser = findById(id);
        if (!StringUtils.hasText(user.getPassword())) {
            currentUser.setPassword(currentUser.getPassword());
        }
        currentUser.setEmail(user.getEmail());
        currentUser.setFirstName(user.getFirstName());
        currentUser.setLastName(user.getLastName());
        currentUser.setPatronymic(user.getPatronymic());
        currentUser.setRole(user.getRole());
        return save(currentUser);
    }

    public UserResponse createUser(UserRequest userRequest) {
        Optional<User> optionalUser = userRepository.findByEmail(userRequest.email());
        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistsException("User with email " + userRequest.email() + " already exists");
        }

        final User user = userRequestMapper.userDtoToUser(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.password()));
        return userResponseMapper.userToUserDto(save(user));
    }

    public UserResponse updateUser(final Long id, final UserRequest userRequest) {
        return userResponseMapper.userToUserDto(update(id, userRequestMapper.userDtoToUser(userRequest)));
    }

    @Override
    public void deleteById(final Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new CustomUserDetails(findByEmail(email));
    }

    private User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
