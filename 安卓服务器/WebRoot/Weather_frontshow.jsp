<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%> 
<%@ page import="com.chengxusheji.domain.Weather" %>
<%@ page import="com.chengxusheji.domain.Area" %>
<%@ page import="com.chengxusheji.domain.WeatherData" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //获取所有的Area信息
    List<Area> areaList = (List<Area>)request.getAttribute("areaList");
    //获取所有的WeatherData信息
    List<WeatherData> weatherDataList = (List<WeatherData>)request.getAttribute("weatherDataList");
    Weather weather = (Weather)request.getAttribute("weather");

%>
<HTML><HEAD><TITLE>查看天气预报</TITLE>
<STYLE type=text/css>
body{margin:0px; font-size:12px; background-image:url(<%=basePath%>images/bg.jpg); background-position:bottom; background-repeat:repeat-x; background-color:#A2D5F0;}
.STYLE1 {color: #ECE9D8}
.label {font-style.:italic; }
.errorLabel {font-style.:italic;  color:red; }
.errorMessage {font-weight:bold; color:red; }
</STYLE>
 <script src="<%=basePath %>calendar.js"></script>
</HEAD>
<BODY><br/><br/>
<s:fielderror cssStyle="color:red" />
<TABLE align="center" height="100%" cellSpacing=0 cellPadding=0 width="80%" border=0>
  <TBODY>
  <TR>
    <TD align="left" vAlign=top ><s:form action="" method="post" onsubmit="return checkForm();" enctype="multipart/form-data" name="form1">
<table width='100%' cellspacing='1' cellpadding='3'  class="tablewidth">
  <tr>
    <td width=30%>天气id:</td>
    <td width=70%><%=weather.getWeatherId() %></td>
  </tr>

  <tr>
    <td width=30%>地区:</td>
    <td width=70%>
      <%=weather.getAreaObj().getAreaName() %>
    </td>
  </tr>

  <tr>
    <td width=30%>天气日期:</td>
        <% java.text.DateFormat weatherDateSDF = new java.text.SimpleDateFormat("yyyy-MM-dd");  %>
    <td width=70%><%=weatherDateSDF.format(weather.getWeatherDate()) %></td>
  </tr>

  <tr>
    <td width=30%>天气:</td>
    <td width=70%>
      <%=weather.getWeatherDataObj().getWeatherDataName() %>
    </td>
  </tr>

  <tr>
    <td width=30%>天气图像:</td>
    <td width=70%><img src="<%=basePath %><%=weather.getWeatherImage() %>" width="200px" border="0px"/></td>
  </tr>
  <tr>
    <td width=30%>温度:</td>
    <td width=70%><%=weather.getTemperature() %></td>
  </tr>

  <tr>
    <td width=30%>空气质量:</td>
    <td width=70%><%=weather.getAirQuality() %></td>
  </tr>

  <tr>
      <td colspan="4" align="center">
        <input type="button" value="返回" onclick="history.back();"/>
      </td>
    </tr>

</table>
</s:form>
   </TD></TR>
  </TBODY>
</TABLE>
</BODY>
</HTML>
