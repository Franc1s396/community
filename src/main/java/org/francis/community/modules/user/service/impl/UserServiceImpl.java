package org.francis.community.modules.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.francis.community.modules.user.model.User;
import org.francis.community.modules.user.mapper.UserMapper;
import org.francis.community.modules.user.model.dto.UserDTO;
import org.francis.community.modules.user.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author francis
 * @since 2022-11-14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User findByUsername(String username) {
        return getOne(queryWrapper()
                .eq(User::getUsername, username));
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
        BeanUtils.copyProperties(userDTO,user);
        save(user);
        return userDTO;
    }

    private LambdaQueryWrapper<User> queryWrapper() {
        return new LambdaQueryWrapper<>();
    }
}
