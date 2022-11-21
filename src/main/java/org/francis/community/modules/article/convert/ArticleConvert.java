package org.francis.community.modules.article.convert;

import org.francis.community.modules.article.model.Article;
import org.francis.community.modules.article.model.request.CreateArticleRequest;
import org.francis.community.modules.article.model.request.UpdateArticleRequest;
import org.francis.community.modules.article.model.vo.ArticleInfoVO;
import org.francis.community.modules.article.model.vo.ArticleVO;
import org.francis.community.modules.user.model.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

/**
 * @author Franc1s
 * @date 2022/11/21
 * @apiNote
 */
@Component
@Mapper(componentModel = "spring")
public interface ArticleConvert {

    @Mappings({
            @Mapping(source = "article.id",target = "id"),
            @Mapping(source = "article.gmtCreate",target = "gmtCreate"),
            @Mapping(source = "userDTO.gmtModified",target = "gmtModified"),
            @Mapping(source = "userDTO.id",target = "userId"),
            @Mapping(source = "tagName",target = "tagName"),
            @Mapping(source = "likeStatus",target = "likeStatus")
    })
    ArticleInfoVO entity2VO(Article article, UserDTO userDTO, String tagName, boolean likeStatus);

    Article request2Entity(CreateArticleRequest createArticleRequest,Long userId);


    void updateRequest2Entity(UpdateArticleRequest updateArticleRequest,@MappingTarget Article article);
}
