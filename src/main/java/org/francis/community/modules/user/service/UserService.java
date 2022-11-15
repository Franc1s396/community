package org.francis.community.modules.user.service;

import org.francis.community.modules.user.model.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.francis.community.modules.user.model.dto.UserDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author francis
 * @since 2022-11-14
 */
public interface UserService extends IService<User> {

    /**
     * 根据AccountId查询用户
     * @param accountId accountId
     * @return 用户信息
     */
    UserDTO findOauthUserByAccountId(Long accountId);

    /**
     * 保存Oauth用户
     * @param userDTO 用户参数
     * @return 用户信息
     */
    UserDTO saveOauthUser(UserDTO userDTO);

    /**
     * 根据username查询用户
     * @param username 用户名
     * @return 用户信息
     */
    User findByUsername(String username);

    /**
     * 根据email 查询用户
     * @param email 邮箱
     * @return 用户信息
     */
    User findByEmail(String email);
}
