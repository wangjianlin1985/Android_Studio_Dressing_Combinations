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
import com.chengxusheji.dao.AreaDAO;
import com.chengxusheji.domain.Area;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class AreaAction extends BaseAction {

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

    private int areaId;
    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }
    public int getAreaId() {
        return areaId;
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

    /*��������Area����*/
    private Area area;
    public void setArea(Area area) {
        this.area = area;
    }
    public Area getArea() {
        return this.area;
    }

    /*��ת�����Area��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        return "add_view";
    }

    /*���Area��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddArea() {
        ActionContext ctx = ActionContext.getContext();
        try {
            areaDAO.AddArea(area);
            ctx.put("message",  java.net.URLEncoder.encode("Area��ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Area���ʧ��!"));
            return "error";
        }
    }

    /*��ѯArea��Ϣ*/
    public String QueryArea() {
        if(currentPage == 0) currentPage = 1;
        List<Area> areaList = areaDAO.QueryAreaInfo(currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        areaDAO.CalculateTotalPageAndRecordNumber();
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = areaDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = areaDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("areaList",  areaList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        return "query_view";
    }

    /*��̨������excel*/
    public String QueryAreaOutputToExcel() { 
        List<Area> areaList = areaDAO.QueryAreaInfo();
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Area��Ϣ��¼"; 
        String[] headers = { "����id","��������"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<areaList.size();i++) {
        	Area area = areaList.get(i); 
        	dataset.add(new String[]{area.getAreaId() + "",area.getAreaName()});
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
			response.setHeader("Content-disposition","attachment; filename="+"Area.xls");//filename�����ص�xls���������������Ӣ�� 
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
    /*ǰ̨��ѯArea��Ϣ*/
    public String FrontQueryArea() {
        if(currentPage == 0) currentPage = 1;
        List<Area> areaList = areaDAO.QueryAreaInfo(currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        areaDAO.CalculateTotalPageAndRecordNumber();
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = areaDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = areaDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("areaList",  areaList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        return "front_query_view";
    }

    /*��ѯҪ�޸ĵ�Area��Ϣ*/
    public String ModifyAreaQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������areaId��ȡArea����*/
        Area area = areaDAO.GetAreaByAreaId(areaId);

        ctx.put("area",  area);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�Area��Ϣ*/
    public String FrontShowAreaQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������areaId��ȡArea����*/
        Area area = areaDAO.GetAreaByAreaId(areaId);

        ctx.put("area",  area);
        return "front_show_view";
    }

    /*�����޸�Area��Ϣ*/
    public String ModifyArea() {
        ActionContext ctx = ActionContext.getContext();
        try {
            areaDAO.UpdateArea(area);
            ctx.put("message",  java.net.URLEncoder.encode("Area��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Area��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��Area��Ϣ*/
    public String DeleteArea() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            areaDAO.DeleteArea(areaId);
            ctx.put("message",  java.net.URLEncoder.encode("Areaɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Areaɾ��ʧ��!"));
            return "error";
        }
    }

}
