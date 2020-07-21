<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../base.jsp"%>
<!DOCTYPE html>
<html>

<head>
    <!-- 页面meta -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>数据 - AdminLTE2定制版</title>
    <meta name="description" content="AdminLTE2定制版">
    <meta name="keywords" content="AdminLTE2定制版">
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no" name="viewport">
    <!-- 页面meta /-->
</head>
<body>
<div id="frameContent" class="content-wrapper" style="margin-left:0px;">
    <section class="content-header">
        <h1>
            统计分析
            <small>商品销售top10</small>
        </h1>
    </section>
    <section class="content">
        <div class="box box-primary">
            <div id="main" style="width: 900px;height:500px;"></div>
        </div>
    </section>
</div>
</body>

<script src="${ctx}/plugins/echarts/echarts.min.js"></script>
<script type="text/javascript">
    $.ajax({
        url:"${ctx}/stat/getProductData.do",
        success:function (resp) {
            var nameArr=[];
            var valueArr=[];

            //遍历json数组,获取到商品名字
            for(var i=0;i<resp.length;i++){
                nameArr[i]=resp[i].name;
                valueArr[i]=resp[i].value;
            }

            // 基于准备好的dom，初始化echarts实例
            var myChart = echarts.init(document.getElementById('main'));

            // 指定图表的配置项和数据
            option = {
                xAxis: {
                    type: 'category',
                    data: nameArr,
                    axisLabel: {
                        interval:0,
                        rotate:40
                    }
                },
                yAxis: {
                    type: 'value'
                },
                series: [{
                    data: valueArr,
                    type: 'bar'
                }]
            };


            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
        }
    })
</script>
</html>