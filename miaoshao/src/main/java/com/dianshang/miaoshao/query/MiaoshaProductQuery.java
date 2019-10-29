package com.dianshang.miaoshao.query;

import com.dianshang.common.entity.Page;
import lombok.Data;

import java.util.Date;

/**
 * @BelongsProject: dianshang
 * @Author: zf
 * @CreateTime: 2019-10-27 17:22
 * @Description:
 */
@Data
public class MiaoshaProductQuery extends Page {
    private String id;
    private String productId;
    private Date startTime;
    private Date endTime;
}