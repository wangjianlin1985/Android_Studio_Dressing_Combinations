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
import com.chengxusheji.dao.WeatherDataDAO;
import com.chengxusheji.domain.WeatherData;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class WeatherDataAction extends BaseAction {

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

    private int weatherDataId;
    public void setWeatherDataId(int weatherDataId) {
        this.weatherDataId = weatherDataId;
    }
    public int getWeatherDataId() {
        return weatherDataId;
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
    @Resource WeatherDataDAO weatherDataDAO;

    /*待操作的WeatherData对象*/
    private WeatherData weatherData;
    public void setWeatherData(WeatherData weatherData) {
        this.weatherData = weatherData;
    }
    public WeatherData getWeatherData() {
        return this.weatherData;
    }

    /*跳转到添加WeatherData视图*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        return "add_view";
    }

    /*添加WeatherData信息*/
    @SuppressWarnings("deprecation")
    public String AddWeatherData() {
        ActionContext ctx = ActionContext.getContext();
        try {
            /*处理天气数据图像上传*/
            String weatherImagePath = "upload/noimage.jpg"; 
       	 	if(weatherImageFile != null)
       	 		weatherImagePath = photoUpload(weatherImageFile,weatherImageFileContentType);
       	 	weatherData.setWeatherImage(weatherImagePath);
            weatherDataDAO.AddWeatherData(weatherData);
            ctx.put("message",  java.net.URLEncoder.encode("WeatherData添加成功!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("图片文件格式不对!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("WeatherData添加失败!"));
            return "error";
        }
    }

    /*查询WeatherData信息*/
    public String QueryWeatherData() {
        if(currentPage == 0) currentPage = 1;
        List<WeatherData> weatherDataList = weatherDataDAO.QueryWeatherDataInfo(currentPage);
        /*计算总的页数和总的记录数*/
        weatherDataDAO.CalculateTotalPageAndRecordNumber();
        /*获取到总的页码数目*/
        totalPage = weatherDataDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = weatherDataDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("weatherDataList",  weatherDataList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        return "query_view";
    }

    /*后台导出到excel*/
    public String QueryWeatherDataOutputToExcel() { 
        List<WeatherData> weatherDataList = weatherDataDAO.QueryWeatherDataInfo();
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "WeatherData信息记录"; 
        String[] headers = { "记录id","天气数据名称","天气数据图像"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<weatherDataList.size();i++) {
        	WeatherData weatherData = weatherDataList.get(i); 
        	dataset.add(new String[]{weatherData.getWeatherDataId() + "",weatherData.getWeatherDataName(),weatherData.getWeatherImage()});
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
			response.setHeader("Content-disposition","attachment; filename="+"WeatherData.xls");//filename是下载的xls的名，建议最好用英文 
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
    /*前台查询WeatherData信息*/
    public String FrontQueryWeatherData() {
        if(currentPage == 0) currentPage = 1;
        List<WeatherData> weatherDataList = weatherDataDAO.QueryWeatherDataInfo(currentPage);
        /*计算总的页数和总的记录数*/
        weatherDataDAO.CalculateTotalPageAndRecordNumber();
        /*获取到总的页码数目*/
        totalPage = weatherDataDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = weatherDataDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("weatherDataList",  weatherDataList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        return "front_query_view";
    }

    /*查询要修改的WeatherData信息*/
    public String ModifyWeatherDataQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键weatherDataId获取WeatherData对象*/
        WeatherData weatherData = weatherDataDAO.GetWeatherDataByWeatherDataId(weatherDataId);

        ctx.put("weatherData",  weatherData);
        return "modify_view";
    }

    /*查询要修改的WeatherData信息*/
    public String FrontShowWeatherDataQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键weatherDataId获取WeatherData对象*/
        WeatherData weatherData = weatherDataDAO.GetWeatherDataByWeatherDataId(weatherDataId);

        ctx.put("weatherData",  weatherData);
        return "front_show_view";
    }

    /*更新修改WeatherData信息*/
    public String ModifyWeatherData() {
        ActionContext ctx = ActionContext.getContext();
        try {
            /*处理天气数据图像上传*/
            if(weatherImageFile != null) {
            	String weatherImagePath = photoUpload(weatherImageFile,weatherImageFileContentType);
            	weatherData.setWeatherImage(weatherImagePath);
            }
            weatherDataDAO.UpdateWeatherData(weatherData);
            ctx.put("message",  java.net.URLEncoder.encode("WeatherData信息更新成功!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("WeatherData信息更新失败!"));
            return "error";
       }
   }

    /*删除WeatherData信息*/
    public String DeleteWeatherData() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            weatherDataDAO.DeleteWeatherData(weatherDataId);
            ctx.put("message",  java.net.URLEncoder.encode("WeatherData删除成功!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("WeatherData删除失败!"));
            return "error";
        }
    }

}
