package com.seemantshekhar.vichhar.repository;

import com.seemantshekhar.vichhar.domain.profile.FollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, Long> {
    Optional<FollowEntity> findByFolloweeIdAndFollowerId(Long followeeId, Long followerID);

    List<FollowEntity> findByFollowerId(Long followerId);

    List<FollowEntity> findByFollowerIdAndFolloweeIdIn(Long id, List<Long> authorIds);
}
