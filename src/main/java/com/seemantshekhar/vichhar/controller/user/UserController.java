package com.seemantshekhar.vichhar.controller.user;

import com.seemantshekhar.vichhar.domain.user.UserDto;
import com.seemantshekhar.vichhar.service.userservice.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping({"/user"})
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public UserDto currentUser(@AuthenticationPrincipal UserDto.Auth authUser){
        return userService.currentUser(authUser);
    }

    @PutMapping
    public UserDto update(@RequestBody @Valid UserDto.Update update, @AuthenticationPrincipal UserDto.Auth authUser){
        return  userService.update(update, authUser);
    }
}
