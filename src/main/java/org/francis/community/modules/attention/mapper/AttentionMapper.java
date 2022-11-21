package org.francis.community.modules.attention.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.francis.community.modules.attention.model.Attention;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author francis
 * @since 2022-11-20
 */
public interface AttentionMapper extends BaseMapper<Attention> {

    default Attention findFollowAttention(Long userId, Long authorId) {
        return selectOne(Wrappers.lambdaQuery(Attention.class)
                .eq(Attention::getFansId, userId)
                .eq(Attention::getAuthorId, authorId));
    }

    default void deleteFollowAttention(Long userId, Long authorId) {
        delete(Wrappers.lambdaQuery(Attention.class)
                .eq(Attention::getFansId, userId)
                .eq(Attention::getAuthorId, authorId));
    }

    default List<Attention> findFansList(Long userId) {
        return selectList(Wrappers.lambdaQuery(Attention.class).eq(Attention::getAuthorId, userId));
    }
}
