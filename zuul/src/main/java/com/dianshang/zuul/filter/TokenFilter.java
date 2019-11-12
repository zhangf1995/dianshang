package com.dianshang.zuul.filter;

import com.dianshang.common.en.StateCode;
import com.dianshang.common.resp.Result;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.sun.xml.internal.ws.client.ResponseContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * @BelongsProject: dianshang
 * @Author: zf
 * @CreateTime: 2019-11-11 15:59
 * @Description: 自定义拦截
 */
@Slf4j
public class TokenFilter extends ZuulFilter {

    //开启pre类型过滤器
    @Override
    public String filterType() {
        // 在进行Zuul过滤的时候可以设置其他过滤执行的位置，那么此时有如下几种类型
        // pre:请求前设置
        // route 请求的时候
        // post :发送的
        // error: 出错之后
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    //true表示开启该过滤器
    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        log.info("come in zuul");
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String url = request.getRequestURI();
        System.err.println(url);
        String token = request.getHeader("token");
        requestContext.setSendZuulResponse(true);
       /* if(StringUtils.isEmpty(token)){
            requestContext.setResponseStatusCode(StateCode.NOTAUTH.getCode());
            requestContext.setResponseBody(StateCode.NOTAUTH.getMsg());
        }*/
        return null;
    }
}