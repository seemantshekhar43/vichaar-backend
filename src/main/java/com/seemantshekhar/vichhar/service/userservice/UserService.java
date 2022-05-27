package com.seemantshekhar.vichhar.service.userservice;

import com.seemantshekhar.vichhar.beans.user.UserDto;

public interface UserService {
    UserDto registration(final UserDto.Registration registration);

    UserDto login(final UserDto.Login login);

    UserDto currentUser(final UserDto.Auth authUser);

    UserDto update(final UserDto.Update update, final UserDto.Auth authUser);
}
