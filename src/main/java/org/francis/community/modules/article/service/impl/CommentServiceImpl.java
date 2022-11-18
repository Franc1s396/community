package org.francis.community.modules.article.service.impl;

import org.francis.community.modules.article.model.Comment;
import org.francis.community.modules.article.mapper.CommentMapper;
import org.francis.community.modules.article.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author francis
 * @since 2022-11-17
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

}
