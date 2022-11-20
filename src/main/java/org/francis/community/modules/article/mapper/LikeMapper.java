package org.francis.community.modules.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.francis.community.modules.article.model.Like;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author francis
 * @since 2022-11-20
 */
public interface LikeMapper extends BaseMapper<Like> {

    int updateBatchLike(@Param("likeList") List<Like> likeList);
}
