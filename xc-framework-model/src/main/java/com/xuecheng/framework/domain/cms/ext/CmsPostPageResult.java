package com.xuecheng.framework.domain.cms.ext;

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/8/12
 * @Description:com.xuecheng.framework.domain.cms.ext
 * @version:1.0
 */
@Data
@ToString
@NoArgsConstructor
public class CmsPostPageResult extends ResponseResult
{
    private String pageUrl;

    public CmsPostPageResult(ResultCode resultCode, String pageUrl)
    {
        super(resultCode);
        this.pageUrl = pageUrl;
    }
}
