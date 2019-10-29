package com.dianshang.miaoshao.controller;
import com.dianshang.common.en.StateCode;
import com.dianshang.common.resp.Result;
import com.dianshang.miaoshao.entity.MiaoshaProduct;
import com.dianshang.miaoshao.service.MiaoshaProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @BelongsProject: dianshang
 * @Author: zf
 * @CreateTime: 2019-10-26 16:29
 * @Description: 秒杀controller
 */
@RequestMapping("/miaosha")
@RestController
public class MiaoshaController {

    @Autowired
    private MiaoshaProductService miaoshaProductService;

    /**
     * 秒杀核心业务代码
     * @param miaoshaProduct
     * @return
     */
    @RequestMapping(value = "/addMiaosha", method = RequestMethod.POST)
    public Result addMiaosha(@RequestBody MiaoshaProduct miaoshaProduct) {
        if (!ObjectUtils.isEmpty(miaoshaProduct) && !StringUtils.isEmpty(miaoshaProduct.getId())) {
            //秒杀核心流程
            try{
                miaoshaProductService.miaoshaStart(miaoshaProduct);
            }catch (Exception e){
                return Result.me().response(StateCode.FAIL.getCode(),e.getMessage());
            }
        } else {
            //暂时这么写
            return Result.me().response(StateCode.FAIL.getCode(),"");
        }
        return Result.me().response(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMsg());
    }
}