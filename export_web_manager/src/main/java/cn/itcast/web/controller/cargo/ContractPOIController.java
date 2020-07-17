package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.ContractProductVo;
import cn.itcast.service.cargo.ContractService;
import cn.itcast.utils.DownloadUtil;
import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("cargo/contract")
public class ContractPOIController  extends BaseController {
    @Reference
    ContractService contractService;

    @RequestMapping(value = "print",name = "跳转到出货表页面")
    public String print(){
        return "cargo/print/contract-print";
    }

    @RequestMapping(value = "printExcel1",name = "出货表打印")
    public void printExcel1(String inputDate, HttpServletResponse response) throws IOException {
        List<ContractProductVo> list = contractService.findByShipTime(inputDate, getCompanyId());

        // 创建一个工作薄
        Workbook wk = new XSSFWorkbook();

        //创建表
        Sheet sheet = wk.createSheet();

        //合并标题行的单元格
        sheet.addMergedRegion(new CellRangeAddress(0,0,1,8));

        //设置lie列宽
        sheet.setColumnWidth(1,26*256);
        sheet.setColumnWidth(2,12*256);
        sheet.setColumnWidth(3,26*256);
        sheet.setColumnWidth(4,12*256);
        sheet.setColumnWidth(5,12*256);
        sheet.setColumnWidth(6,12*256);
        sheet.setColumnWidth(7,12*256);
        sheet.setColumnWidth(8,12*256);



        Row row = sheet.createRow(0);
        row.setHeightInPoints(36);
        Cell cell = row.createCell(1);
        cell.setCellStyle(bigTitle(wk));
        cell.setCellValue(inputDate.replace("-","年")+"月份出货表");

        String[] titleArr = {"","客户","订单号","货号","数量","工厂","工厂交期","船期","贸易条款"};
        row = sheet.createRow(1);
        row.setHeightInPoints(24);
        CellStyle titleCss =title(wk);

        for (int i = 1; i < titleArr.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(titleArr[i]);
            cell.setCellStyle(titleCss);
        }
        int index = 2;
        CellStyle textCss = text(wk);

        if (list!=null && list.size()>0) {
            for (ContractProductVo vo : list) {
                row = sheet.createRow(index);
              
                row.setHeightInPoints(24);
                cell = row.createCell(1);
                cell.setCellValue(vo.getCustomName());
                cell.setCellStyle(textCss);


                cell = row.createCell(2);
                cell.setCellValue(vo.getContractNo());
                cell.setCellStyle(textCss);


                cell = row.createCell(3);
                cell.setCellValue(vo.getProductNo());
                cell.setCellStyle(textCss);


                cell = row.createCell(4);
                cell.setCellValue(vo.getCnumber());
                cell.setCellStyle(textCss);


                cell = row.createCell(5);
                cell.setCellValue(vo.getFactoryName());
                cell.setCellStyle(textCss);


                cell = row.createCell(6);
                cell.setCellValue(DateFormatUtils.format(vo.getDeliveryPeriod(),"yyyy-MM-dd"));
                cell.setCellStyle(textCss);


                cell = row.createCell(7);
                cell.setCellValue(DateFormatUtils.format(vo.getShipTime(),"yyyy-MM-dd"));
                cell.setCellStyle(textCss);


                cell = row.createCell(8);
                cell.setCellValue(vo.getTradeTerms());
                cell.setCellStyle(textCss);

                index++;
            }
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        wk.write(bos);

        DownloadUtil.download(bos,response,inputDate.replace("-","年")+"月份出货表.xlsx");
    }


    //大标题的样式
    public CellStyle bigTitle(Workbook wb){
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short)16);
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    //小标题的样式
    public CellStyle title(Workbook wb){
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short)12);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    //文字样式
    public CellStyle text(Workbook wb){
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short)10);

        style.setFont(font);

        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        return style;
    }
    @RequestMapping(value = "printExcel",name = "出货表模版打印")
    public void printExcel(String inputDate, HttpServletResponse response) throws IOException {
        Workbook wk = new XSSFWorkbook(session.getServletContext().getResourceAsStream("/make/xlsprint/tOUTPRODUCT.xlsx"));

        Sheet sheet = wk.getSheetAt(0);


        Row row = sheet.getRow(0);
        Cell cell = row.getCell(1);
        cell.setCellValue(inputDate.replace("-","年")+"月份出货表");

        CellStyle[] cssArr = new CellStyle[9];
        row = sheet.getRow(2);
        for (int i = 1; i < row.getLastCellNum() ; i++) {
            cssArr[i] = row.getCell(i).getCellStyle();
        }

        List<ContractProductVo> list = contractService.findByShipTime(inputDate, getCompanyId());
        if (list!=null && list.size()>0) {
            int index = 2;
            for (ContractProductVo vo : list) {
                row = sheet.createRow(index);

                cell = row.createCell(1);
                cell.setCellValue(vo.getCustomName());
                cell.setCellStyle(cssArr[1]);

                cell = row.createCell(2);
                cell.setCellValue(vo.getContractNo());
                cell.setCellStyle(cssArr[2]);


                cell = row.createCell(3);
                cell.setCellValue(vo.getProductNo());
                cell.setCellStyle(cssArr[3]);


                cell = row.createCell(4);
                cell.setCellValue(vo.getCnumber());
                cell.setCellStyle(cssArr[4]);


                cell = row.createCell(5);
                cell.setCellValue(vo.getFactoryName());
                cell.setCellStyle(cssArr[5]);


                cell = row.createCell(6);
                cell.setCellValue(DateFormatUtils.format(vo.getDeliveryPeriod(),"yyyy-MM-dd"));
                cell.setCellStyle(cssArr[6]);


                cell = row.createCell(7);
                cell.setCellValue(DateFormatUtils.format(vo.getShipTime(),"yyyy-MM-dd"));
                cell.setCellStyle(cssArr[7]);


                cell = row.createCell(8);
                cell.setCellValue(vo.getTradeTerms());
                cell.setCellStyle(cssArr[8]);


                index++;
            }
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        wk.write(bos);

        DownloadUtil.download(bos,response,inputDate.replace("-","年")+"月份出货表.xlsx");

    }

}
