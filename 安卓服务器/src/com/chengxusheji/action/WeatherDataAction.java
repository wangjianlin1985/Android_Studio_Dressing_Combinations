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

    private int weatherDataId;
    public void setWeatherDataId(int weatherDataId) {
        this.weatherDataId = weatherDataId;
    }
    public int getWeatherDataId() {
        return weatherDataId;
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
    @Resource WeatherDataDAO weatherDataDAO;

    /*��������WeatherData����*/
    private WeatherData weatherData;
    public void setWeatherData(WeatherData weatherData) {
        this.weatherData = weatherData;
    }
    public WeatherData getWeatherData() {
        return this.weatherData;
    }

    /*��ת�����WeatherData��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        return "add_view";
    }

    /*���WeatherData��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddWeatherData() {
        ActionContext ctx = ActionContext.getContext();
        try {
            /*������������ͼ���ϴ�*/
            String weatherImagePath = "upload/noimage.jpg"; 
       	 	if(weatherImageFile != null)
       	 		weatherImagePath = photoUpload(weatherImageFile,weatherImageFileContentType);
       	 	weatherData.setWeatherImage(weatherImagePath);
            weatherDataDAO.AddWeatherData(weatherData);
            ctx.put("message",  java.net.URLEncoder.encode("WeatherData��ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("WeatherData���ʧ��!"));
            return "error";
        }
    }

    /*��ѯWeatherData��Ϣ*/
    public String QueryWeatherData() {
        if(currentPage == 0) currentPage = 1;
        List<WeatherData> weatherDataList = weatherDataDAO.QueryWeatherDataInfo(currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        weatherDataDAO.CalculateTotalPageAndRecordNumber();
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = weatherDataDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = weatherDataDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("weatherDataList",  weatherDataList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        return "query_view";
    }

    /*��̨������excel*/
    public String QueryWeatherDataOutputToExcel() { 
        List<WeatherData> weatherDataList = weatherDataDAO.QueryWeatherDataInfo();
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "WeatherData��Ϣ��¼"; 
        String[] headers = { "��¼id","������������","��������ͼ��"};
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
		HttpServletResponse response = null;//����һ��HttpServletResponse���� 
		OutputStream out = null;//����һ����������� 
		try { 
			response = ServletActionContext.getResponse();//��ʼ��HttpServletResponse���� 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"WeatherData.xls");//filename�����ص�xls���������������Ӣ�� 
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
    /*ǰ̨��ѯWeatherData��Ϣ*/
    public String FrontQueryWeatherData() {
        if(currentPage == 0) currentPage = 1;
        List<WeatherData> weatherDataList = weatherDataDAO.QueryWeatherDataInfo(currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        weatherDataDAO.CalculateTotalPageAndRecordNumber();
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = weatherDataDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = weatherDataDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("weatherDataList",  weatherDataList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        return "front_query_view";
    }

    /*��ѯҪ�޸ĵ�WeatherData��Ϣ*/
    public String ModifyWeatherDataQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������weatherDataId��ȡWeatherData����*/
        WeatherData weatherData = weatherDataDAO.GetWeatherDataByWeatherDataId(weatherDataId);

        ctx.put("weatherData",  weatherData);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�WeatherData��Ϣ*/
    public String FrontShowWeatherDataQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������weatherDataId��ȡWeatherData����*/
        WeatherData weatherData = weatherDataDAO.GetWeatherDataByWeatherDataId(weatherDataId);

        ctx.put("weatherData",  weatherData);
        return "front_show_view";
    }

    /*�����޸�WeatherData��Ϣ*/
    public String ModifyWeatherData() {
        ActionContext ctx = ActionContext.getContext();
        try {
            /*������������ͼ���ϴ�*/
            if(weatherImageFile != null) {
            	String weatherImagePath = photoUpload(weatherImageFile,weatherImageFileContentType);
            	weatherData.setWeatherImage(weatherImagePath);
            }
            weatherDataDAO.UpdateWeatherData(weatherData);
            ctx.put("message",  java.net.URLEncoder.encode("WeatherData��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("WeatherData��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��WeatherData��Ϣ*/
    public String DeleteWeatherData() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            weatherDataDAO.DeleteWeatherData(weatherDataId);
            ctx.put("message",  java.net.URLEncoder.encode("WeatherDataɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("WeatherDataɾ��ʧ��!"));
            return "error";
        }
    }

}
