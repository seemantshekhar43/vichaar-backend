package com.seemantshekhar.vichhar.service.profileservice;

import com.seemantshekhar.vichhar.domain.profile.FollowEntity;
import com.seemantshekhar.vichhar.domain.profile.ProfileDto;
import com.seemantshekhar.vichhar.domain.user.UserDto;
import com.seemantshekhar.vichhar.domain.user.UserEntity;
import com.seemantshekhar.vichhar.exception.AppException;
import com.seemantshekhar.vichhar.exception.Error;
import com.seemantshekhar.vichhar.repository.FollowRepository;
import com.seemantshekhar.vichhar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService{
    private final UserRepository userRepository;
    private final FollowRepository followRepository;


    @Override
    public ProfileDto getProfile(String username, UserDto.Auth authUser) {
        UserEntity user = userRepository.findByName(username)
                .orElseThrow(() -> new AppException(Error.USER_NOT_FOUND));
        Boolean following = followRepository.findByFolloweeIdAndFollowerId(user.getId(), authUser.getId()).isPresent();
        return convertToProfileDto(user, following);
    }

    @Transactional
    @Override
    public ProfileDto followUser(String name, UserDto.Auth authUser) {
        UserEntity followee = userRepository.findByName(name)
                .orElseThrow(() -> new AppException(Error.USER_NOT_FOUND));
        UserEntity follower = UserEntity.builder()
                .id(authUser.getId())
                .build();
        followRepository.findByFolloweeIdAndFollowerId(followee.getId(), follower.getId())
                .ifPresent(follow -> {throw new AppException(Error.ALREADY_FOLLOWED_USER);});
        FollowEntity followEntity = FollowEntity.builder()
                .follower(follower)
                .followee(followee)
                .build();
        followRepository.save(followEntity);
        return convertToProfileDto(followee, true);

    }

    @Transactional
    @Override
    public ProfileDto unfollowUser(String name, UserDto.Auth authUser) {
        UserEntity followee = userRepository.findByName(name)
                .orElseThrow(() -> new AppException(Error.USER_NOT_FOUND));
        UserEntity follower = UserEntity.builder()
                .id(authUser.getId())
                .build();
        FollowEntity followEntity = followRepository.findByFolloweeIdAndFollowerId(followee.getId(), follower.getId())
                .orElseThrow(() -> new AppException(Error.FOLLOW_NOT_FOUND));
        followRepository.delete(followEntity);
        return convertToProfileDto(followee, false);
    }

    private ProfileDto convertToProfileDto(UserEntity user, Boolean following) {
        return ProfileDto.builder()
                .name(user.getName())
                .bio(user.getBio())
                .image(user.getImage())
                .following(following)
                .build();
    }
}
