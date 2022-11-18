package org.francis.community.modules.article.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.francis.community.modules.article.model.Tag;
import org.francis.community.modules.article.mapper.TagMapper;
import org.francis.community.modules.article.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author francis
 * @since 2022-11-17
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public Tag findTagById(Long tagId) {
        return getOne(Wrappers.lambdaQuery(Tag.class).eq(Tag::getId, tagId));
    }

    @Override
    public List<Tag> findTagListByIds(List<Long> tagIds) {
        return list(Wrappers.lambdaQuery(Tag.class).in(Tag::getId,tagIds));
    }


}
