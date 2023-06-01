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

	/*ͼƬ���ļ��ֶ�weatherImage��������*/
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
    /*�������Ҫ��ѯ������: ����*/
    private Area areaObj;
    public void setAreaObj(Area areaObj) {
        this.areaObj = areaObj;
    }
    public Area getAreaObj() {
        return this.areaObj;
    }

    /*�������Ҫ��ѯ������: ��������*/
    private String weatherDate;
    public void setWeatherDate(String weatherDate) {
        this.weatherDate = weatherDate;
    }
    public String getWeatherDate() {
        return this.weatherDate;
    }

    /*�������Ҫ��ѯ������: ����*/
    private WeatherData weatherDataObj;
    public void setWeatherDataObj(WeatherData weatherDataObj) {
        this.weatherDataObj = weatherDataObj;
    }
    public WeatherData getWeatherDataObj() {
        return this.weatherDataObj;
    }

    /*��ǰ�ڼ�ҳ*/
    private int currentPage;
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    public int getCurrentPage() {
        return currentPage;
    }

    /*һ������ҳ*/
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

    /*��ǰ��ѯ���ܼ�¼��Ŀ*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*ҵ������*/
    @Resource AreaDAO areaDAO;
    @Resource WeatherDataDAO weatherDataDAO;
    @Resource WeatherDAO weatherDAO;

    /*��������Weather����*/
    private Weather weather;
    public void setWeather(Weather weather) {
        this.weather = weather;
    }
    public Weather getWeather() {
        return this.weather;
    }

    /*��ת�����Weather��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*��ѯ���е�Area��Ϣ*/
        List<Area> areaList = areaDAO.QueryAllAreaInfo();
        ctx.put("areaList", areaList);
        /*��ѯ���е�WeatherData��Ϣ*/
        List<WeatherData> weatherDataList = weatherDataDAO.QueryAllWeatherDataInfo();
        ctx.put("weatherDataList", weatherDataList);
        return "add_view";
    }

    /*���Weather��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddWeather() {
        ActionContext ctx = ActionContext.getContext();
        try {
            Area areaObj = areaDAO.GetAreaByAreaId(weather.getAreaObj().getAreaId());
            weather.setAreaObj(areaObj);
            WeatherData weatherDataObj = weatherDataDAO.GetWeatherDataByWeatherDataId(weather.getWeatherDataObj().getWeatherDataId());
            weather.setWeatherDataObj(weatherDataObj);
            
            weather.setWeatherImage(weatherDataObj.getWeatherImage());
            
            /*��������ͼ���ϴ�
            String weatherImagePath = "upload/noimage.jpg"; 
       	 	if(weatherImageFile != null)
       	 		weatherImagePath = photoUpload(weatherImageFile,weatherImageFileContentType);
       	 	weather.setWeatherImage(weatherImagePath);*/
            
            weatherDAO.AddWeather(weather);
            ctx.put("message",  java.net.URLEncoder.encode("Weather��ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Weather���ʧ��!"));
            return "error";
        }
    }

    /*��ѯWeather��Ϣ*/
    public String QueryWeather() {
        if(currentPage == 0) currentPage = 1;
        if(weatherDate == null) weatherDate = "";
        List<Weather> weatherList = weatherDAO.QueryWeatherInfo(areaObj, weatherDate, weatherDataObj, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        weatherDAO.CalculateTotalPageAndRecordNumber(areaObj, weatherDate, weatherDataObj);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = weatherDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
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

    /*��̨������excel*/
    public String QueryWeatherOutputToExcel() { 
        if(weatherDate == null) weatherDate = "";
        List<Weather> weatherList = weatherDAO.QueryWeatherInfo(areaObj,weatherDate,weatherDataObj);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Weather��Ϣ��¼"; 
        String[] headers = { "����id","����","��������","����","����ͼ��","�¶�","��������"};
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
		HttpServletResponse response = null;//����һ��HttpServletResponse���� 
		OutputStream out = null;//����һ����������� 
		try { 
			response = ServletActionContext.getResponse();//��ʼ��HttpServletResponse���� 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"Weather.xls");//filename�����ص�xls���������������Ӣ�� 
			response.setContentType("application/msexcel;charset=UTF-8");//�������� 
			response.setHeader("Pragma","No-cache");//����ͷ 
			response.setHeader("Cache-Control","no-cache");//����ͷ 
			response.setDateHeader("Expires", 0);//��������ͷ  
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
    /*ǰ̨��ѯWeather��Ϣ*/
    public String FrontQueryWeather() {
        if(currentPage == 0) currentPage = 1;
        if(weatherDate == null) weatherDate = "";
        List<Weather> weatherList = weatherDAO.QueryWeatherInfo(areaObj, weatherDate, weatherDataObj, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        weatherDAO.CalculateTotalPageAndRecordNumber(areaObj, weatherDate, weatherDataObj);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = weatherDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
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

    /*��ѯҪ�޸ĵ�Weather��Ϣ*/
    public String ModifyWeatherQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������weatherId��ȡWeather����*/
        Weather weather = weatherDAO.GetWeatherByWeatherId(weatherId);

        List<Area> areaList = areaDAO.QueryAllAreaInfo();
        ctx.put("areaList", areaList);
        List<WeatherData> weatherDataList = weatherDataDAO.QueryAllWeatherDataInfo();
        ctx.put("weatherDataList", weatherDataList);
        ctx.put("weather",  weather);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�Weather��Ϣ*/
    public String FrontShowWeatherQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������weatherId��ȡWeather����*/
        Weather weather = weatherDAO.GetWeatherByWeatherId(weatherId);

        List<Area> areaList = areaDAO.QueryAllAreaInfo();
        ctx.put("areaList", areaList);
        List<WeatherData> weatherDataList = weatherDataDAO.QueryAllWeatherDataInfo();
        ctx.put("weatherDataList", weatherDataList);
        ctx.put("weather",  weather);
        return "front_show_view";
    }

    /*�����޸�Weather��Ϣ*/
    public String ModifyWeather() {
        ActionContext ctx = ActionContext.getContext();
        try {
            Area areaObj = areaDAO.GetAreaByAreaId(weather.getAreaObj().getAreaId());
            weather.setAreaObj(areaObj);
            WeatherData weatherDataObj = weatherDataDAO.GetWeatherDataByWeatherDataId(weather.getWeatherDataObj().getWeatherDataId());
            weather.setWeatherDataObj(weatherDataObj);
            /*��������ͼ���ϴ�*/
            if(weatherImageFile != null) {
            	String weatherImagePath = photoUpload(weatherImageFile,weatherImageFileContentType);
            	weather.setWeatherImage(weatherImagePath);
            }
            weatherDAO.UpdateWeather(weather);
            ctx.put("message",  java.net.URLEncoder.encode("Weather��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Weather��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��Weather��Ϣ*/
    public String DeleteWeather() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            weatherDAO.DeleteWeather(weatherId);
            ctx.put("message",  java.net.URLEncoder.encode("Weatherɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Weatherɾ��ʧ��!"));
            return "error";
        }
    }

}
