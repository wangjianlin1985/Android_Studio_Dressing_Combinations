package com.chengxusheji.dao;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.chengxusheji.domain.Area;

@Service @Transactional
public class AreaDAO {

	@Resource SessionFactory factory;
    /*每页显示记录数目*/
    private final int PAGE_SIZE = 10;

    /*保存查询后总的页数*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*保存查询到的总记录数*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*添加图书信息*/
    public void AddArea(Area area) throws Exception {
    	Session s = factory.getCurrentSession();
     s.save(area);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Area> QueryAreaInfo(int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Area area where 1=1";
    	Query q = s.createQuery(hql);
    	/*计算当前显示页码的开始记录*/
    	int startIndex = (currentPage-1) * this.PAGE_SIZE;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.PAGE_SIZE);
    	List areaList = q.list();
    	return (ArrayList<Area>) areaList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Area> QueryAreaInfo() { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Area area where 1=1";
    	Query q = s.createQuery(hql);
    	List areaList = q.list();
    	return (ArrayList<Area>) areaList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Area> QueryAllAreaInfo() {
        Session s = factory.getCurrentSession(); 
        String hql = "From Area";
        Query q = s.createQuery(hql);
        List areaList = q.list();
        return (ArrayList<Area>) areaList;
    }

    /*计算总的页数和记录数*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber() {
        Session s = factory.getCurrentSession();
        String hql = "From Area area where 1=1";
        Query q = s.createQuery(hql);
        List areaList = q.list();
        recordNumber = areaList.size();
        int mod = recordNumber % this.PAGE_SIZE;
        totalPage = recordNumber / this.PAGE_SIZE;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public Area GetAreaByAreaId(int areaId) {
        Session s = factory.getCurrentSession();
        Area area = (Area)s.get(Area.class, areaId);
        return area;
    }

    /*更新Area信息*/
    public void UpdateArea(Area area) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(area);
    }

    /*删除Area信息*/
    public void DeleteArea (int areaId) throws Exception {
        Session s = factory.getCurrentSession();
        Object area = s.load(Area.class, areaId);
        s.delete(area);
    }

}
