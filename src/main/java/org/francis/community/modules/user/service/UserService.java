package org.francis.community.modules.user.service;

import org.francis.community.modules.user.model.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.francis.community.modules.user.model.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * 根据id列表查询用户列表
     *
     * @param userIds 用户id列表
     * @return 用户列表
     */
    List<UserDTO> findUserListByIds(ArrayList<Long> userIds);

    /**
     * 根据id查询用户
     * @param userId 用户id
     * @return 用户信息
     */
    UserDTO findUserById(Long userId);
}
