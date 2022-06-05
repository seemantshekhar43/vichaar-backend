package com.seemantshekhar.vichhar.controller.profile;

import com.seemantshekhar.vichhar.beans.profile.ProfileDto;
import com.seemantshekhar.vichhar.beans.user.UserDto;
import com.seemantshekhar.vichhar.service.profileservice.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profiles")
public class profileController {
    private final ProfileService profileService;

    @GetMapping("/{username}")
    public ProfileDto getProfile(@PathVariable("username") String name, @AuthenticationPrincipal UserDto.Auth authUser){
        return profileService.getProfile(name, authUser);
    }

    @PostMapping("/{username}/follow")
    public ProfileDto followUser(@PathVariable("username") String name, @AuthenticationPrincipal UserDto.Auth authUser){
        return profileService.followUser(name, authUser);
    }

    @DeleteMapping("/{username}/follow")
    public ProfileDto unfollowUser(@PathVariable("username") String name, @AuthenticationPrincipal UserDto.Auth authUser){
        return profileService.unfollowUser(name, authUser);
    }
}
