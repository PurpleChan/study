package com.xuecheng.framework.domain.learning.ext;

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/8/18
 * @Description:com.xuecheng.framework.domain.learning.ext
 * @version:1.0
 */
@Data
@ToString
@NoArgsConstructor
public class GetMediaResult extends ResponseResult
{
    private String fileUrl;

    public GetMediaResult(ResultCode resultCode, String fileUrl)
    {
        super(resultCode);
        this.fileUrl = fileUrl;
    }
}
