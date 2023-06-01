<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%> 
<%@ page import="com.chengxusheji.domain.Weather" %>
<%@ page import="com.chengxusheji.domain.Area" %>
<%@ page import="com.chengxusheji.domain.WeatherData" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //获取所有的Area信息
    List<Area> areaList = (List<Area>)request.getAttribute("areaList");
    //获取所有的WeatherData信息
    List<WeatherData> weatherDataList = (List<WeatherData>)request.getAttribute("weatherDataList");
    Weather weather = (Weather)request.getAttribute("weather");

    String username=(String)session.getAttribute("username");
    if(username==null){
        response.getWriter().println("<script>top.location.href='" + basePath + "login/login_view.action';</script>");
    }
%>
<HTML><HEAD><TITLE>修改天气预报</TITLE>
<STYLE type=text/css>
BODY {
	MARGIN-LEFT: 0px; BACKGROUND-COLOR: #ffffff
}
.STYLE1 {color: #ECE9D8}
.label {font-style.:italic; }
.errorLabel {font-style.:italic;  color:red; }
.errorMessage {font-weight:bold; color:red; }
</STYLE>
 <script src="<%=basePath %>calendar.js"></script>
<script language="javascript">
/*验证表单*/
function checkForm() {
    var temperature = document.getElementById("weather.temperature").value;
    if(temperature=="") {
        alert('请输入温度!');
        return false;
    }
    var airQuality = document.getElementById("weather.airQuality").value;
    if(airQuality=="") {
        alert('请输入空气质量!');
        return false;
    }
    return true; 
}
 </script>
</HEAD>
<BODY background="<%=basePath %>images/adminBg.jpg">
<s:fielderror cssStyle="color:red" />
<TABLE align="center" height="100%" cellSpacing=0 cellPadding=0 width="80%" border=0>
  <TBODY>
  <TR>
    <TD align="left" vAlign=top ><s:form action="Weather/Weather_ModifyWeather.action" method="post" onsubmit="return checkForm();" enctype="multipart/form-data" name="form1">
<table width='100%' cellspacing='1' cellpadding='3' class="tablewidth">
  <tr>
    <td width=30%>天气id:</td>
    <td width=70%><input id="weather.weatherId" name="weather.weatherId" type="text" value="<%=weather.getWeatherId() %>" readOnly /></td>
  </tr>

  <tr>
    <td width=30%>地区:</td>
    <td width=70%>
      <select name="weather.areaObj.areaId">
      <%
        for(Area area:areaList) {
          String selected = "";
          if(area.getAreaId() == weather.getAreaObj().getAreaId())
            selected = "selected";
      %>
          <option value='<%=area.getAreaId() %>' <%=selected %>><%=area.getAreaName() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>天气日期:</td>
    <% DateFormat weatherDateSDF = new SimpleDateFormat("yyyy-MM-dd");  %>
    <td width=70%><input type="text" readonly  id="weather.weatherDate"  name="weather.weatherDate" onclick="setDay(this);" value='<%=weatherDateSDF.format(weather.getWeatherDate()) %>'/></td>
  </tr>

  <tr>
    <td width=30%>天气:</td>
    <td width=70%>
      <select name="weather.weatherDataObj.weatherDataId">
      <%
        for(WeatherData weatherData:weatherDataList) {
          String selected = "";
          if(weatherData.getWeatherDataId() == weather.getWeatherDataObj().getWeatherDataId())
            selected = "selected";
      %>
          <option value='<%=weatherData.getWeatherDataId() %>' <%=selected %>><%=weatherData.getWeatherDataName() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>天气图像:</td>
    <td width=70%><img src="<%=basePath %><%=weather.getWeatherImage() %>" width="200px" border="0px"/><br/>
    <input type=hidden name="weather.weatherImage" value="<%=weather.getWeatherImage() %>" />
    <input id="weatherImageFile" name="weatherImageFile" type="file" size="50" /></td>
  </tr>
  <tr>
    <td width=30%>温度:</td>
    <td width=70%><input id="weather.temperature" name="weather.temperature" type="text" size="20" value='<%=weather.getTemperature() %>'/></td>
  </tr>

  <tr>
    <td width=30%>空气质量:</td>
    <td width=70%><input id="weather.airQuality" name="weather.airQuality" type="text" size="20" value='<%=weather.getAirQuality() %>'/></td>
  </tr>

  <tr bgcolor='#FFFFFF'>
      <td colspan="4" align="center">
        <input type='submit' name='button' value='保存' >
        &nbsp;&nbsp;
        <input type="reset" value='重写' />
      </td>
    </tr>

</table>
</s:form>
   </TD></TR>
  </TBODY>
</TABLE>
</BODY>
</HTML>
