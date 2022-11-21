package org.francis.community.modules.attention.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.francis.community.core.enums.CodeEnums;
import org.francis.community.core.exception.ServiceException;
import org.francis.community.core.model.request.PageQueryRequest;
import org.francis.community.core.utils.SecurityUtils;
import org.francis.community.modules.attention.model.Attention;
import org.francis.community.modules.attention.mapper.AttentionMapper;
import org.francis.community.modules.attention.model.request.AttentionFollowRequest;
import org.francis.community.modules.attention.service.AttentionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.francis.community.modules.user.service.UserService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author francis
 * @since 2022-11-20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AttentionServiceImpl extends ServiceImpl<AttentionMapper, Attention> implements AttentionService {

    private final AttentionMapper attentionMapper;

    private final StringRedisTemplate redisTemplate;

    private final UserService userService;

    private static final String FOLLOW_SET_KEY_PREFIX = "community:follow:";

    @Override
    public IPage<Attention> findFollowList(PageQueryRequest pageQueryRequest, Long userId) {
        Page<Attention> attentionPage = new Page<>(pageQueryRequest.getPage(), pageQueryRequest.getLimit());
        return page(attentionPage, Wrappers.lambdaQuery(Attention.class).eq(Attention::getFansId, userId));
    }

    @Override
    public IPage<Attention> findFansList(PageQueryRequest pageQueryRequest, Long userId) {
        Page<Attention> attentionPage = new Page<>(pageQueryRequest.getPage(), pageQueryRequest.getLimit());
        return page(attentionPage, Wrappers.lambdaQuery(Attention.class).eq(Attention::getAuthorId, userId));
    }

    @Override
    public List<Attention> findFansList(Long userId) {
        return attentionMapper.findFansList(userId);
    }

    @Override
    public Set<Long> findCommonAttention(Long userId) {
        Long loginUserId = SecurityUtils.getUserId();
        String loginUserKey = FOLLOW_SET_KEY_PREFIX + loginUserId;
        String userKey = FOLLOW_SET_KEY_PREFIX + userId;
        Set<String> intersect = redisTemplate.opsForSet().intersect(loginUserKey, userKey);
        if (CollectionUtils.isEmpty(intersect)) {
            return null;
        }
        return intersect.stream().map(Long::parseLong).collect(Collectors.toSet());
    }

    /**
     * 关注或者取消用户
     */
    @Override
    public void followUser(AttentionFollowRequest attentionFollowRequest) {
        Long userId = SecurityUtils.getUserId();
        Long authorId = attentionFollowRequest.getUserId();

        if (Objects.equals(userId, authorId)) {
            throw new ServiceException(CodeEnums.ATTENTION_ERROR.getCode(), CodeEnums.ATTENTION_ERROR.getMessage());
        }

        // 组成redis key
        userService.findUserById(authorId);
        Boolean isFollow = attentionFollowRequest.getIsFollow();
        String key = FOLLOW_SET_KEY_PREFIX + userId;
        // 关注用户情况下 查数据库是否已经有相同数据 如果有则什么抛出异常
        if (isFollow) {
            Attention attention = attentionMapper.findFollowAttention(userId, authorId);
            if (Objects.nonNull(attention)) {
                throw new ServiceException(CodeEnums.ATTENTION_ERROR.getCode(), CodeEnums.ATTENTION_ERROR.getMessage());
            }
            // 插入数据库
            attention = new Attention();
            attention.setAuthorId(authorId);
            attention.setFansId(userId);
            attentionMapper.insert(attention);
            redisTemplate.opsForSet().add(key, authorId.toString());
            log.info("用户id:{} *关注*了id:{}用户", userId, authorId);
        } else {
            attentionMapper.deleteFollowAttention(userId, authorId);
            redisTemplate.opsForSet().remove(key, authorId.toString());
            log.info("用户id:{} *取消关注*了id:{}用户", userId, authorId);
        }
    }

}
