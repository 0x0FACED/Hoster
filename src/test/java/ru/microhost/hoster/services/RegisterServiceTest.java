package ru.microhost.hoster.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.microhost.hoster.repos.UserRepository;
import ru.microhost.hoster.users.User;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RegisterServiceTest {
    @Autowired
    private RegisterService registerService;

    @MockBean
    private UserRepository userRepository;

    private static final String username = "molly";
    private static final User user = mock(User.class);

    @BeforeAll
    public static void setup(){
        when(user.getName()).thenReturn(username);
    }

    @Test
    public void registerShouldAcceptUserWithNewUsername(){
        when(userRepository.findByName(username)).thenReturn(null);
        registerService.addUser(user);
    }

    @Test
    public void registerShouldRejectUserWithUsedUsername(){
        when(userRepository.findByName(username)).thenReturn(user);
        registerService.addUser(user);
    }

}
