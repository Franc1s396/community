package org.francis.community.modules.attention.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@TableName("bbs_attention")
@ApiModel(value="Attention对象", description="")
public class Attention implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "关注id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "作者id")
    private Long authorId;

    @ApiModelProperty(value = "粉丝id")
    private Long fansId;


}
