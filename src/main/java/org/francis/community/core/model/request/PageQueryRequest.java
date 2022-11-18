package org.francis.community.core.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author francis
 */
@Data
public class PageQueryRequest {

	@ApiModelProperty(value="分页页数",required = true)
	private Integer page = 1;

	@ApiModelProperty(value="每页条数",required = true)
	private Integer limit = 10;

}