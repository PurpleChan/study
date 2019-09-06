package com.xuecheng.framework.domain.course.response;

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/8/11
 * @Description:com.xuecheng.framework.domain.course.response
 * @version:1.0
 */
@Data
@ToString
@NoArgsConstructor
public class CoursePublishResult extends ResponseResult
{
    private String previewUrl;

    public CoursePublishResult(String previewUrl, ResultCode resultCode)
    {
        super(resultCode);
        this.previewUrl = previewUrl;
    }
}
