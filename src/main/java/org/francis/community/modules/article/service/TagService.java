package org.francis.community.modules.article.service;

import org.francis.community.modules.article.model.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author francis
 * @since 2022-11-17
 */
public interface TagService extends IService<Tag> {

    /**
     * 根据id找标签
     * @param tagId 标签id
     * @return 返回标签
     */
    Tag findTagById(Long tagId);

    /**
     * 根据标签id列表查询标签列表
     * @param tagIds id列表
     * @return 标签列表
     */
    List<Tag> findTagListByIds(List<Long> tagIds);
}
