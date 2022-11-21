package org.francis.community.modules.article.convert;

import org.francis.community.modules.article.model.CommentMulti;
import org.francis.community.modules.article.model.request.CommentMultiCreateRequest;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author Franc1s
 * @date 2022/11/21
 * @apiNote
 */
@Component
@Mapper(componentModel = "spring")
public interface CommentMultiConvert {

    CommentMulti createRequest2Entity(CommentMultiCreateRequest commentCreateRequest, Long userId);
}
