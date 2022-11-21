package org.francis.community.modules.article.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.francis.community.core.utils.SecurityUtils;
import org.francis.community.modules.article.enums.LikeEnums;
import org.francis.community.modules.article.mapper.LikeMapper;
import org.francis.community.modules.article.model.Like;
import org.francis.community.modules.article.service.LikeService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
public class LikeServiceImpl extends ServiceImpl<LikeMapper, Like> implements LikeService {

    /**
     * 记录所有点赞或取消点赞记录
     */
    private static final String LIKE_HASH_KEY = "community:article:like:hash";
    /**
     * 记录帖子点赞计数
     */
    private static final String LIKE_COUNT_HASH_KEY_PREFIX = "community:article:like:count";

    private final StringRedisTemplate redisTemplate;

    private final LikeMapper likeMapper;


    /**
     * redis结构: 1.String类型存储点赞计数  2.Hash类型存储点赞记录,HashKey:{Key-Value}
     * 业务流程： 1.添加点赞记录 2.自增点赞计数
     */
    @Override
    public void likeArticle(Long articleId) {
        Long userId = SecurityUtils.getUserId();
        String hashKey = articleId + "::" + userId;

        // 如果hash中存在并且是点赞状态 则不做任何操作
        Like like = likeMapper.findLikeData(articleId, userId);
        String code = (String) redisTemplate.opsForHash().get(LIKE_HASH_KEY, hashKey);
        boolean valid = StringUtils.hasText(code) && LikeEnums.LIKE.getCode().equals(Integer.parseInt(code))
                || Objects.nonNull(like);
        if (valid) {
            return;
        }

        redisTemplate.opsForHash().put(LIKE_HASH_KEY, hashKey, LikeEnums.LIKE.getCode().toString());
        redisTemplate.opsForHash().increment(LIKE_COUNT_HASH_KEY_PREFIX, articleId.toString(), 1);
    }

    @Override
    public void cancelLikeArticle(Long articleId) {
        Long userId = SecurityUtils.getUserId();
        String hashKey = articleId + "::" + userId;

        // 如果hash中存在并且是点赞状态 则不做任何操作
        Like like = likeMapper.findLikeData(articleId, userId);
        String code = (String) redisTemplate.opsForHash().get(LIKE_HASH_KEY, hashKey);
        boolean valid = StringUtils.hasText(code) && LikeEnums.CANCEL_LIKE.getCode().equals(Integer.parseInt(code))
                || Objects.nonNull(like);
        if (valid) {
            return;
        }

        redisTemplate.opsForHash().put(LIKE_HASH_KEY, hashKey, LikeEnums.CANCEL_LIKE.getCode().toString());
        redisTemplate.opsForHash().increment(LIKE_COUNT_HASH_KEY_PREFIX, articleId.toString(), -1);
    }

    @Override
    public boolean isLike(Long articleId) {
        Long userId = SecurityUtils.getUserId();
        String hashKey = articleId + "::" + userId;

        // 查redis
        String code = (String) redisTemplate.opsForHash().get(LIKE_HASH_KEY, hashKey);
        if (StringUtils.hasText(code)) {
            return LikeEnums.LIKE.getCode().equals(Integer.parseInt(code));
        } else {
            //查mysql
            Like like = likeMapper.selectOne(Wrappers.lambdaQuery(Like.class).eq(Like::getArticleId, articleId).eq(Like::getUserId, userId));
            return like.getStatus();
        }
    }

    @Override
    public void updateBatchLike(List<Like> likeList) {
        if (!CollectionUtils.isEmpty(likeList)) {
            int result = likeMapper.updateBatchLike(likeList);
            log.info("批量更新点赞数据状态 更新数量:{},实际更新数量:{}", likeList.size(), result);
        }
    }
}
