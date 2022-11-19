package org.francis.community.modules.article.controller;


import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author francis
 * @since 2022-11-17
 */
@Api(tags = "标签接口")
@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
public class TagController {

}

