<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%> 
<%@ page import="com.chengxusheji.domain.WeatherData" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    WeatherData weatherData = (WeatherData)request.getAttribute("weatherData");

    String username=(String)session.getAttribute("username");
    if(username==null){
        response.getWriter().println("<script>top.location.href='" + basePath + "login/login_view.action';</script>");
    }
%>
<HTML><HEAD><TITLE>修改天气数据</TITLE>
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
    var weatherDataName = document.getElementById("weatherData.weatherDataName").value;
    if(weatherDataName=="") {
        alert('请输入天气数据名称!');
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
    <TD align="left" vAlign=top ><s:form action="WeatherData/WeatherData_ModifyWeatherData.action" method="post" onsubmit="return checkForm();" enctype="multipart/form-data" name="form1">
<table width='100%' cellspacing='1' cellpadding='3' class="tablewidth">
  <tr>
    <td width=30%>记录id:</td>
    <td width=70%><input id="weatherData.weatherDataId" name="weatherData.weatherDataId" type="text" value="<%=weatherData.getWeatherDataId() %>" readOnly /></td>
  </tr>

  <tr>
    <td width=30%>天气数据名称:</td>
    <td width=70%><input id="weatherData.weatherDataName" name="weatherData.weatherDataName" type="text" size="20" value='<%=weatherData.getWeatherDataName() %>'/></td>
  </tr>

  <tr>
    <td width=30%>天气数据图像:</td>
    <td width=70%><img src="<%=basePath %><%=weatherData.getWeatherImage() %>" width="200px" border="0px"/><br/>
    <input type=hidden name="weatherData.weatherImage" value="<%=weatherData.getWeatherImage() %>" />
    <input id="weatherImageFile" name="weatherImageFile" type="file" size="50" /></td>
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
