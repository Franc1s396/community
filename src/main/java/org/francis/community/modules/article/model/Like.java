package org.francis.community.modules.article.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author francis
 * @since 2022-11-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("bbs_like")
@ApiModel(value="Like对象", description="")
public class Like implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "点赞id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "文章id")
    private Long articleId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "点赞状态")
    private Boolean status;
}
