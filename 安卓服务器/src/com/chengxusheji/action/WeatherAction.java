package com.chengxusheji.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import com.opensymphony.xwork2.ActionContext;
import com.chengxusheji.dao.WeatherDAO;
import com.chengxusheji.domain.Weather;
import com.chengxusheji.dao.AreaDAO;
import com.chengxusheji.domain.Area;
import com.chengxusheji.dao.WeatherDataDAO;
import com.chengxusheji.domain.WeatherData;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class WeatherAction extends BaseAction {

	/*图片或文件字段weatherImage参数接收*/
	private File weatherImageFile;
	private String weatherImageFileFileName;
	private String weatherImageFileContentType;
	public File getWeatherImageFile() {
		return weatherImageFile;
	}
	public void setWeatherImageFile(File weatherImageFile) {
		this.weatherImageFile = weatherImageFile;
	}
	public String getWeatherImageFileFileName() {
		return weatherImageFileFileName;
	}
	public void setWeatherImageFileFileName(String weatherImageFileFileName) {
		this.weatherImageFileFileName = weatherImageFileFileName;
	}
	public String getWeatherImageFileContentType() {
		return weatherImageFileContentType;
	}
	public void setWeatherImageFileContentType(String weatherImageFileContentType) {
		this.weatherImageFileContentType = weatherImageFileContentType;
	}
    /*界面层需要查询的属性: 地区*/
    private Area areaObj;
    public void setAreaObj(Area areaObj) {
        this.areaObj = areaObj;
    }
    public Area getAreaObj() {
        return this.areaObj;
    }

    /*界面层需要查询的属性: 天气日期*/
    private String weatherDate;
    public void setWeatherDate(String weatherDate) {
        this.weatherDate = weatherDate;
    }
    public String getWeatherDate() {
        return this.weatherDate;
    }

    /*界面层需要查询的属性: 天气*/
    private WeatherData weatherDataObj;
    public void setWeatherDataObj(WeatherData weatherDataObj) {
        this.weatherDataObj = weatherDataObj;
    }
    public WeatherData getWeatherDataObj() {
        return this.weatherDataObj;
    }

    /*当前第几页*/
    private int currentPage;
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    public int getCurrentPage() {
        return currentPage;
    }

    /*一共多少页*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    private int weatherId;
    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }
    public int getWeatherId() {
        return weatherId;
    }

    /*当前查询的总记录数目*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*业务层对象*/
    @Resource AreaDAO areaDAO;
    @Resource WeatherDataDAO weatherDataDAO;
    @Resource WeatherDAO weatherDAO;

    /*待操作的Weather对象*/
    private Weather weather;
    public void setWeather(Weather weather) {
        this.weather = weather;
    }
    public Weather getWeather() {
        return this.weather;
    }

    /*跳转到添加Weather视图*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*查询所有的Area信息*/
        List<Area> areaList = areaDAO.QueryAllAreaInfo();
        ctx.put("areaList", areaList);
        /*查询所有的WeatherData信息*/
        List<WeatherData> weatherDataList = weatherDataDAO.QueryAllWeatherDataInfo();
        ctx.put("weatherDataList", weatherDataList);
        return "add_view";
    }

    /*添加Weather信息*/
    @SuppressWarnings("deprecation")
    public String AddWeather() {
        ActionContext ctx = ActionContext.getContext();
        try {
            Area areaObj = areaDAO.GetAreaByAreaId(weather.getAreaObj().getAreaId());
            weather.setAreaObj(areaObj);
            WeatherData weatherDataObj = weatherDataDAO.GetWeatherDataByWeatherDataId(weather.getWeatherDataObj().getWeatherDataId());
            weather.setWeatherDataObj(weatherDataObj);
            
            weather.setWeatherImage(weatherDataObj.getWeatherImage());
            
            /*处理天气图像上传
            String weatherImagePath = "upload/noimage.jpg"; 
       	 	if(weatherImageFile != null)
       	 		weatherImagePath = photoUpload(weatherImageFile,weatherImageFileContentType);
       	 	weather.setWeatherImage(weatherImagePath);*/
            
            weatherDAO.AddWeather(weather);
            ctx.put("message",  java.net.URLEncoder.encode("Weather添加成功!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("图片文件格式不对!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Weather添加失败!"));
            return "error";
        }
    }

    /*查询Weather信息*/
    public String QueryWeather() {
        if(currentPage == 0) currentPage = 1;
        if(weatherDate == null) weatherDate = "";
        List<Weather> weatherList = weatherDAO.QueryWeatherInfo(areaObj, weatherDate, weatherDataObj, currentPage);
        /*计算总的页数和总的记录数*/
        weatherDAO.CalculateTotalPageAndRecordNumber(areaObj, weatherDate, weatherDataObj);
        /*获取到总的页码数目*/
        totalPage = weatherDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = weatherDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("weatherList",  weatherList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("areaObj", areaObj);
        List<Area> areaList = areaDAO.QueryAllAreaInfo();
        ctx.put("areaList", areaList);
        ctx.put("weatherDate", weatherDate);
        ctx.put("weatherDataObj", weatherDataObj);
        List<WeatherData> weatherDataList = weatherDataDAO.QueryAllWeatherDataInfo();
        ctx.put("weatherDataList", weatherDataList);
        return "query_view";
    }

    /*后台导出到excel*/
    public String QueryWeatherOutputToExcel() { 
        if(weatherDate == null) weatherDate = "";
        List<Weather> weatherList = weatherDAO.QueryWeatherInfo(areaObj,weatherDate,weatherDataObj);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Weather信息记录"; 
        String[] headers = { "天气id","地区","天气日期","天气","天气图像","温度","空气质量"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<weatherList.size();i++) {
        	Weather weather = weatherList.get(i); 
        	dataset.add(new String[]{weather.getWeatherId() + "",weather.getAreaObj().getAreaName(),
new SimpleDateFormat("yyyy-MM-dd").format(weather.getWeatherDate()),weather.getWeatherDataObj().getWeatherDataName(),
weather.getWeatherImage(),weather.getTemperature(),weather.getAirQuality()});
        }
        /*
        OutputStream out = null;
		try {
			out = new FileOutputStream("C://output.xls");
			ex.exportExcel(title,headers, dataset, out);
		    out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		HttpServletResponse response = null;//创建一个HttpServletResponse对象 
		OutputStream out = null;//创建一个输出流对象 
		try { 
			response = ServletActionContext.getResponse();//初始化HttpServletResponse对象 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"Weather.xls");//filename是下载的xls的名，建议最好用英文 
			response.setContentType("application/msexcel;charset=UTF-8");//设置类型 
			response.setHeader("Pragma","No-cache");//设置头 
			response.setHeader("Cache-Control","no-cache");//设置头 
			response.setDateHeader("Expires", 0);//设置日期头  
			String rootPath = ServletActionContext.getServletContext().getRealPath("/");
			ex.exportExcel(rootPath,title,headers, dataset, out);
			out.flush();
		} catch (IOException e) { 
			e.printStackTrace(); 
		}finally{
			try{
				if(out!=null){ 
					out.close(); 
				}
			}catch(IOException e){ 
				e.printStackTrace(); 
			} 
		}
		return null;
    }
    /*前台查询Weather信息*/
    public String FrontQueryWeather() {
        if(currentPage == 0) currentPage = 1;
        if(weatherDate == null) weatherDate = "";
        List<Weather> weatherList = weatherDAO.QueryWeatherInfo(areaObj, weatherDate, weatherDataObj, currentPage);
        /*计算总的页数和总的记录数*/
        weatherDAO.CalculateTotalPageAndRecordNumber(areaObj, weatherDate, weatherDataObj);
        /*获取到总的页码数目*/
        totalPage = weatherDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = weatherDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("weatherList",  weatherList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("areaObj", areaObj);
        List<Area> areaList = areaDAO.QueryAllAreaInfo();
        ctx.put("areaList", areaList);
        ctx.put("weatherDate", weatherDate);
        ctx.put("weatherDataObj", weatherDataObj);
        List<WeatherData> weatherDataList = weatherDataDAO.QueryAllWeatherDataInfo();
        ctx.put("weatherDataList", weatherDataList);
        return "front_query_view";
    }

    /*查询要修改的Weather信息*/
    public String ModifyWeatherQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键weatherId获取Weather对象*/
        Weather weather = weatherDAO.GetWeatherByWeatherId(weatherId);

        List<Area> areaList = areaDAO.QueryAllAreaInfo();
        ctx.put("areaList", areaList);
        List<WeatherData> weatherDataList = weatherDataDAO.QueryAllWeatherDataInfo();
        ctx.put("weatherDataList", weatherDataList);
        ctx.put("weather",  weather);
        return "modify_view";
    }

    /*查询要修改的Weather信息*/
    public String FrontShowWeatherQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键weatherId获取Weather对象*/
        Weather weather = weatherDAO.GetWeatherByWeatherId(weatherId);

        List<Area> areaList = areaDAO.QueryAllAreaInfo();
        ctx.put("areaList", areaList);
        List<WeatherData> weatherDataList = weatherDataDAO.QueryAllWeatherDataInfo();
        ctx.put("weatherDataList", weatherDataList);
        ctx.put("weather",  weather);
        return "front_show_view";
    }

    /*更新修改Weather信息*/
    public String ModifyWeather() {
        ActionContext ctx = ActionContext.getContext();
        try {
            Area areaObj = areaDAO.GetAreaByAreaId(weather.getAreaObj().getAreaId());
            weather.setAreaObj(areaObj);
            WeatherData weatherDataObj = weatherDataDAO.GetWeatherDataByWeatherDataId(weather.getWeatherDataObj().getWeatherDataId());
            weather.setWeatherDataObj(weatherDataObj);
            /*处理天气图像上传*/
            if(weatherImageFile != null) {
            	String weatherImagePath = photoUpload(weatherImageFile,weatherImageFileContentType);
            	weather.setWeatherImage(weatherImagePath);
            }
            weatherDAO.UpdateWeather(weather);
            ctx.put("message",  java.net.URLEncoder.encode("Weather信息更新成功!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Weather信息更新失败!"));
            return "error";
       }
   }

    /*删除Weather信息*/
    public String DeleteWeather() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            weatherDAO.DeleteWeather(weatherId);
            ctx.put("message",  java.net.URLEncoder.encode("Weather删除成功!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Weather删除失败!"));
            return "error";
        }
    }

}
