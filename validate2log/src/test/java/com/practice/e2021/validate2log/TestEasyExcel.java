package com.practice.e2021.validate2log;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.alibaba.excel.EasyExcel;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.poi.ss.usermodel.Workbook;

public class TestEasyExcel {

    public static void main(String[] args) throws IOException {

        String sheetName = "用户信息";
        String file = System.currentTimeMillis() + ".xlsx";
        // EasyExcel.write(file, UserEntity.class).sheet(sheetName).doWrite(getData());
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("用户", "用户信息"),
                UserEntity.class, getData());
        FileOutputStream fos = new FileOutputStream("easypoi-user.xlsx");
        workbook.write(fos);
        fos.close();
    }

    private static List<UserEntity> getData() {
        return IntStream.range(1, 20).mapToObj(v -> {
            UserEntity u = new UserEntity();
            u.setAge(v);
            u.setName("测试-" + v);
            u.setTime(new Date(System.currentTimeMillis() + v));
            return u;
        }).collect(Collectors.toList());
    }
}
