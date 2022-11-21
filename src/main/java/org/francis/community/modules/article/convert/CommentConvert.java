package org.francis.community.modules.article.convert;

import org.francis.community.modules.article.model.Comment;
import org.francis.community.modules.article.model.request.CommentCreateRequest;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author Franc1s
 * @date 2022/11/21
 * @apiNote
 */
@Component
@Mapper(componentModel = "spring")
public interface CommentConvert {

    Comment createRequest2Entity(CommentCreateRequest commentCreateRequest, Long userId);
}
