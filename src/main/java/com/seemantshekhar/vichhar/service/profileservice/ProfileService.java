package com.seemantshekhar.vichhar.service.profileservice;

import com.seemantshekhar.vichhar.domain.profile.ProfileDto;
import com.seemantshekhar.vichhar.domain.user.UserDto;

public interface ProfileService {
    ProfileDto getProfile(final String username, final UserDto.Auth authUser);

    ProfileDto followUser(final String name, final UserDto.Auth authUser);

    ProfileDto unfollowUser(final String name, final UserDto.Auth authUser);
}
