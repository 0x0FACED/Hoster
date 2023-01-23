package ru.microhost.hoster.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.microhost.hoster.users.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByName(String name);

}
