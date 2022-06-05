package com.seemantshekhar.vichhar.service.profile;

import com.seemantshekhar.vichhar.beans.profile.ProfileDto;
import com.seemantshekhar.vichhar.beans.user.UserDto;

public interface ProfileService {
    ProfileDto getProfile(final String username, final UserDto.Auth authUser);

    ProfileDto followUser(final String name, final UserDto.Auth authUser);

    ProfileDto unfollowUser(final String name, final UserDto.Auth authUser);
}
