package com.dianshang.common.entity;

import lombok.Data;

import java.util.List;

/**
 * @BelongsProject: dianshang
 * @Author: zf
 * @CreateTime: 2019-10-25 16:34
 * @Description:
 */
@Data
public class Page<T> {
    private Integer currentPage;
    private Integer pageSize;
    private Integer total;
    private List<T> data;

    public Integer getCurrentNo(){
        if(null == currentPage || currentPage < 1){
            return null;
        }else{
            return (currentPage-1)*pageSize;
        }
    }
}