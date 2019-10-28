package com.dianshang.miaoshao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dianshang.miaoshao.entity.MiaoshaProduct;
import com.dianshang.miaoshao.query.MiaoshaProductQuery;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MiaoshaProductMapper extends BaseMapper<MiaoshaProduct> {

    /**
     * 条件查询miaosha_product
     * @param miaoshaProductQuery
     * @return
     */
    MiaoshaProduct queryByCondition(MiaoshaProductQuery miaoshaProductQuery);
}
