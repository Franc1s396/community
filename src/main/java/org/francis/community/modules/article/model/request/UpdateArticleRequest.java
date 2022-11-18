package org.francis.community.modules.article.model.request;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author Franc1s
 * @date 2022/11/18
 * @apiNote
 */
@Data
@ApiModel("帖子更新参数")
public class UpdateArticleRequest {
    @ApiModelProperty(value = "帖子编号")
    @NotNull(message = "帖子编号不能为空")
    private Long id;

    @ApiModelProperty(value = "帖子标题")
    private String title;

    @ApiModelProperty(value = "帖子内容")
    private String content;

    @ApiModelProperty(value = "标签编号")
    private Long tagId;
}
