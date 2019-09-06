package com.xuecheng.govern.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.govern.gateway.service.GatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/8/20
 * @Description:com.xuecheng.govern.gateway.filter
 * @version:1.0
 */
@Component
public class CeShiFilter extends ZuulFilter
{
    @Autowired
    private GatewayService gatewayService;

    //设置类型  有  pre  route  post  error  四种类型
    @Override
    public String filterType()
    {
        return "pre";
    }

    //设置级别  数字越小优先执行
    @Override
    public int filterOrder()
    {
        return 0;
    }

    //判断是否往下执行run方法
    @Override
    public boolean shouldFilter()
    {
        return false;
    }

    //过滤器队请求进行处理
    @Override
    public Object run() throws ZuulException
    {
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();

        if(gatewayService.checkTokenFromCookie(request)&&gatewayService.checkJwtFromHeader(request))
        {
            return null;
        }
        this.access_denied();
        return null;
    }

    //拒绝访问
    private void access_denied()
    {
        RequestContext currentContext = RequestContext.getCurrentContext();
        //拒绝访问
        currentContext.setSendZuulResponse(false);

        //设置响应内容
        HttpServletResponse response = currentContext.getResponse();
        ResponseResult responseResult=new ResponseResult(CommonCode.FAIL);
        String result = JSON.toJSONString(responseResult);
        currentContext.setResponseBody(result);
        currentContext.setResponseStatusCode(200);
        response.setContentType("application/json;charset=utf-8");

    }
}
