package com.seemantshekhar.vichhar.service.userservice;

import com.seemantshekhar.vichhar.beans.user.UserDto;
import com.seemantshekhar.vichhar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;


    @Override
    public UserDto registration(UserDto.Registration registration) {
        return null;
    }

    @Override
    public UserDto login(UserDto.Login login) {
        return null;
    }

    @Override
    public UserDto currentUser(UserDto.Auth authUser) {
        return null;
    }

    @Override
    public UserDto update(UserDto.Update update, UserDto.Auth authUser) {
        return null;
    }
}
