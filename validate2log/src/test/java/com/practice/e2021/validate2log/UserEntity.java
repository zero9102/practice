package com.practice.e2021.validate2log;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import java.util.Date;
import lombok.Data;

@Data
public class UserEntity {

//    @ExcelProperty({"用户信息", "姓名"})
//    @ExcelProperty("姓名")
    @Excel(name = "姓名", orderNum = "3", desensitizationRule="1,3")
    private String name;

//    @ExcelProperty({"用户信息", "年龄"})
//    @ExcelProperty("年龄")
    @Excel(name = "年龄")
    private Integer age;

//    @DateTimeFormat("yyyy-MM-dd HH:mm")
//    @ExcelProperty("时间")
    @Excel(name = "时间", format = "yyyy-MM-dd", width = 24.0)
    private Date time;

}
