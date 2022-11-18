package org.francis.community.modules.article.model.vo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author francis
 * @since 2022-11-17
 */
@Data
public class ArticleInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;

    private String content;

    private Long tagId;

    private String tagName;

    private Long userId;

    private String nickname;

    private String avatarUrl;

    private Integer commentCount;

    private Integer likeCount;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;


}
