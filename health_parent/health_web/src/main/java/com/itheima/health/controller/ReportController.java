package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.service.MemberService;

import com.itheima.health.service.ReportService;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    MemberService memberService;

    @Reference
    SetmealService setmealService;

    @Reference
    ReportService reportService;
    private Map<String, Object> map;
    private Map<String, Object> businessReport;

    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){
        try {
            Calendar calendar=Calendar.getInstance();
            calendar.add(Calendar.MONTH,-12);//获得12个月之前的日期（当前日期减去12个月）
            List<String> list =new ArrayList<>();//存储12个月份
            for (int i = 0; i <12 ; i++) {
                calendar.add(Calendar.MONTH,1);
                list.add(new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));
            }
            map = new HashMap<>();
            map.put("months",list);//将12个月份存入map集合
            List<Integer> memberCount=memberService.findCountByMonths(list);
            map.put("memberCount",memberCount);
        } catch (Exception e) {
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
        return new Result(true,MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);

    }

    @RequestMapping("/getMemberReportByDate")
    public Result getMemberReportByDate(String startDate,String endDate){
        Map<String,Object> map=new HashMap<>();
        try {
            //存储相隔月份
            List<String> monthBetween = getMonthBetween(startDate, endDate);
            map.put("months",monthBetween);
            List<Integer> countByMonths = memberService.findCountByMonths(monthBetween);
            map.put("memberCount",countByMonths);
        } catch (ParseException e) {
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
        return new Result(true,MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }


    private static List<String> getMonthBetween(String minDate, String maxDate) throws ParseException {
        ArrayList<String> result = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        min.setTime(sdf.parse(minDate));
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

        max.setTime(sdf.parse(maxDate));
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

        Calendar curr = min;
        while (curr.before(max)) {
            result.add(sdf.format(curr.getTime()));
            curr.add(Calendar.MONTH, 1);
        }

        return result;
    }


    @RequestMapping("getSetmealReport")
    public Result getSetmealReport(){
       List<Map<String,Object>> list = setmealService.findSetmealCount();
       Map<String,Object> map=new HashMap<>();
       map.put("setmealCount",list);
        List<String> setmealNames = new ArrayList<String>();
        for (Map<String, Object> stringObjectMap : list) {
            String name = (String)stringObjectMap.get("name");
            setmealNames.add(name);
        }
        map.put("setmealNames",setmealNames);
        return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
    }

    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        try {
            businessReport = reportService.getBusinessReport();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,businessReport);
    }

    @RequestMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        //远程调用报表服务获取报表数据
        Map<String, Object> result = null;
        try {
            result = reportService.getBusinessReport();
            //取出返回结果数据，准备将报表数据写入到Excel文件中
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");
            //获得Excel模板文件绝对路径
            String temlateRealPath = request.getSession().getServletContext().getRealPath("template/report_template.xlsx");
            System.out.println(temlateRealPath);

            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(temlateRealPath)));
            XSSFSheet sheet = workbook.getSheetAt(0);

            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);//日期

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            int rowNum = 12;
            for(Map map : hotSetmeal){//热门套餐
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum ++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }

            //通过输出流进行文件下载
            ServletOutputStream out = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            workbook.write(out);

            out.flush();
            out.close();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/getSexReportData")
    public Result getSexReportData(){
        List<Map<String,Object>> list=new ArrayList<>();
        Integer boy = memberService.findMemberCountFromBoy();
        Integer gril = memberService.findMemberCountFromGirl();
        Map<String,Object> boymap=new HashMap<>();
        boymap.put("name","男");
        boymap.put("value",boy);
        list.add(boymap);

        Map<String,Object> girlmap=new HashMap<>();
        girlmap.put("name","女");
        girlmap.put("value",gril);
        list.add(girlmap);


        Map<String,Object> map=new HashMap<>();
        map.put("sexCount",list);
        List<String> setmealNames = new ArrayList<String>();
        for (Map<String, Object> stringObjectMap : list) {
            String name = (String)stringObjectMap.get("name");
            setmealNames.add(name);
        }
        map.put("sexNames",setmealNames);
        return new Result(true,MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }


    @RequestMapping("/getAgeReportData")
    public Result getAgeReportData(){
        List<Map<String,Object>> list=new ArrayList<>();

        Integer age18=memberService.findCountBy18();
        Map<String,Object> age18Map=new HashMap<>();
        age18Map.put("name","0-18");
        age18Map.put("value",age18);
        list.add(age18Map);

        Integer age18To30=memberService.findCountBy18To30();
        Map<String,Object> age18To30Map=new HashMap<>();
        age18To30Map.put("name","18-30");
        age18To30Map.put("value",age18To30);
        list.add(age18To30Map);

        Integer age30To45=memberService.findCountBy30To45();
        Map<String,Object> age30To45Map=new HashMap<>();
        age30To45Map.put("name","30-45");
        age30To45Map.put("value",age30To45);
        list.add(age30To45Map);

        Integer age45=memberService.findCountBy45();
        Map<String,Object> age45Map=new HashMap<>();
        age45Map.put("name","45岁以上");
        age45Map.put("value",age45);
        list.add(age45Map);

        Map<String,Object> map=new HashMap<>();
        map.put("ageCount",list);

        List<String> ageNames = new ArrayList<String>();
        for (Map<String, Object> stringObjectMap : list) {
            String name = (String)stringObjectMap.get("name");
            ageNames.add(name);
        }
        map.put("ageNames",ageNames);
        return new Result(true,MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }
}
