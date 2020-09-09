package com.api.yirang.auth.application.basicService;


import com.api.yirang.auth.domain.user.exceptions.UserNullException;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.presentation.dto.RegisterDto;
import com.api.yirang.auth.repository.persistence.maria.UserDao;
import com.api.yirang.auth.support.type.Authority;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    // DI dao
    private final UserDao userDao;

    public Authority getAuthorityByUserId(Long userId) {
        return userDao.findByUserId(userId).get().getAuthority();
    }

    public void registerAdmin(RegisterDto registerDto){
        // RegisterDto -> User
        User user = User.builder()
                        .userId(registerDto.getUserId())
                        .fileUrl(registerDto.getFileUrl())
                        .username(registerDto.getUsername())
                        .sex(registerDto.getSex())
                        .email(registerDto.getEmail())
                        .authority(Authority.ROLE_ADMIN)
                        .build();
        userDao.save(user);
    }

    public void saveUser(User user){
        userDao.save(user);
    }

    public boolean isRegisteredUserByUserId(Long userId){
        return userDao.existsUserByUserId(userId);
    }

    public User findUserByUserId(Long userId){
        return userDao.findByUserId(userId).orElseThrow(UserNullException::new);
    }
}
