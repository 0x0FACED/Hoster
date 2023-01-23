package ru.microhost.hoster.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.microhost.hoster.repos.UserRepository;
import ru.microhost.hoster.users.User;

@Service
@RequiredArgsConstructor
public class RegisterService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public boolean addUser(User user) {
        User userFromDb = userRepository.findByName(user.getName());
        if (userFromDb == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            user.setActive(true);
            return true;
        } else {
            return false;
        }
    }
}
