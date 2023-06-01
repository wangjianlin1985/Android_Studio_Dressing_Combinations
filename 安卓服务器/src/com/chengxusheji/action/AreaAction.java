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

    private int areaId;
    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }
    public int getAreaId() {
        return areaId;
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

    /*待操作的Area对象*/
    private Area area;
    public void setArea(Area area) {
        this.area = area;
    }
    public Area getArea() {
        return this.area;
    }

    /*跳转到添加Area视图*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        return "add_view";
    }

    /*添加Area信息*/
    @SuppressWarnings("deprecation")
    public String AddArea() {
        ActionContext ctx = ActionContext.getContext();
        try {
            areaDAO.AddArea(area);
            ctx.put("message",  java.net.URLEncoder.encode("Area添加成功!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("图片文件格式不对!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Area添加失败!"));
            return "error";
        }
    }

    /*查询Area信息*/
    public String QueryArea() {
        if(currentPage == 0) currentPage = 1;
        List<Area> areaList = areaDAO.QueryAreaInfo(currentPage);
        /*计算总的页数和总的记录数*/
        areaDAO.CalculateTotalPageAndRecordNumber();
        /*获取到总的页码数目*/
        totalPage = areaDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = areaDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("areaList",  areaList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        return "query_view";
    }

    /*后台导出到excel*/
    public String QueryAreaOutputToExcel() { 
        List<Area> areaList = areaDAO.QueryAreaInfo();
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Area信息记录"; 
        String[] headers = { "地区id","地区名称"};
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
		HttpServletResponse response = null;//创建一个HttpServletResponse对象 
		OutputStream out = null;//创建一个输出流对象 
		try { 
			response = ServletActionContext.getResponse();//初始化HttpServletResponse对象 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"Area.xls");//filename是下载的xls的名，建议最好用英文 
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
    /*前台查询Area信息*/
    public String FrontQueryArea() {
        if(currentPage == 0) currentPage = 1;
        List<Area> areaList = areaDAO.QueryAreaInfo(currentPage);
        /*计算总的页数和总的记录数*/
        areaDAO.CalculateTotalPageAndRecordNumber();
        /*获取到总的页码数目*/
        totalPage = areaDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = areaDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("areaList",  areaList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        return "front_query_view";
    }

    /*查询要修改的Area信息*/
    public String ModifyAreaQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键areaId获取Area对象*/
        Area area = areaDAO.GetAreaByAreaId(areaId);

        ctx.put("area",  area);
        return "modify_view";
    }

    /*查询要修改的Area信息*/
    public String FrontShowAreaQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键areaId获取Area对象*/
        Area area = areaDAO.GetAreaByAreaId(areaId);

        ctx.put("area",  area);
        return "front_show_view";
    }

    /*更新修改Area信息*/
    public String ModifyArea() {
        ActionContext ctx = ActionContext.getContext();
        try {
            areaDAO.UpdateArea(area);
            ctx.put("message",  java.net.URLEncoder.encode("Area信息更新成功!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Area信息更新失败!"));
            return "error";
       }
   }

    /*删除Area信息*/
    public String DeleteArea() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            areaDAO.DeleteArea(areaId);
            ctx.put("message",  java.net.URLEncoder.encode("Area删除成功!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Area删除失败!"));
            return "error";
        }
    }

}
