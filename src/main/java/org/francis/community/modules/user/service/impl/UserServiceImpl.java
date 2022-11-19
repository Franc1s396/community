package org.francis.community.modules.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.francis.community.modules.user.model.User;
import org.francis.community.modules.user.mapper.UserMapper;
import org.francis.community.modules.user.model.dto.UserDTO;
import org.francis.community.modules.user.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author francis
 * @since 2022-11-14
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User findByUsername(String username) {
        return getOne(queryWrapper().eq(User::getUsername, username));
    }

    @Override
    public User findByEmail(String email) {
        return getOne(queryWrapper().eq(User::getEmail, email));
    }

    @Override
    public List<UserDTO> findUserListByIds(List<Long> userIds) {
        List<User> userList = list(Wrappers.lambdaQuery(User.class).in(User::getId, userIds));
        return userList.stream()
                .map(user -> {
                    UserDTO userDTO = new UserDTO();
                    BeanUtils.copyProperties(user, userDTO);
                    return userDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO findUserById(Long userId) {
        User user = getOne(Wrappers.lambdaQuery(User.class).eq(User::getId, userId));
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    @Override
    public UserDTO findOauthUserByAccountId(Long accountId) {
        // 根据accountId查询数据库
        LambdaQueryWrapper<User> queryWrapper = queryWrapper()
                .eq(User::getAccountId, accountId);
        User user = getOne(queryWrapper);

        // 如果没有则返回空
        if (Objects.isNull(user)) {
            return null;
        }

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDTO saveOauthUser(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        save(user);
        log.info("添加Oauth用户,user:{}", user);
        return userDTO;
    }

    private LambdaQueryWrapper<User> queryWrapper() {
        return new LambdaQueryWrapper<>();
    }
}
