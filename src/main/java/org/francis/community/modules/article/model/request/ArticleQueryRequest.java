package org.francis.community.modules.article.model.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Franc1s
 * @date 2022/11/18
 * @apiNote
 */
@Data
@ApiModel("帖子查询参数")
public class ArticleQueryRequest {
    @ApiModelProperty(value = "帖子编号")
    private Long id;

    @ApiModelProperty(value = "帖子标题")
    private String title;

    @ApiModelProperty(value = "标签编号")
    private Long tagId;

    @ApiModelProperty(value = "用户编号")
    private Long userId;
}
