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
import com.chengxusheji.domain.WeatherData;

@Service @Transactional
public class WeatherDataDAO {

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
    public void AddWeatherData(WeatherData weatherData) throws Exception {
    	Session s = factory.getCurrentSession();
     s.save(weatherData);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<WeatherData> QueryWeatherDataInfo(int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From WeatherData weatherData where 1=1";
    	Query q = s.createQuery(hql);
    	/*计算当前显示页码的开始记录*/
    	int startIndex = (currentPage-1) * this.PAGE_SIZE;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.PAGE_SIZE);
    	List weatherDataList = q.list();
    	return (ArrayList<WeatherData>) weatherDataList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<WeatherData> QueryWeatherDataInfo() { 
    	Session s = factory.getCurrentSession();
    	String hql = "From WeatherData weatherData where 1=1";
    	Query q = s.createQuery(hql);
    	List weatherDataList = q.list();
    	return (ArrayList<WeatherData>) weatherDataList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<WeatherData> QueryAllWeatherDataInfo() {
        Session s = factory.getCurrentSession(); 
        String hql = "From WeatherData";
        Query q = s.createQuery(hql);
        List weatherDataList = q.list();
        return (ArrayList<WeatherData>) weatherDataList;
    }

    /*计算总的页数和记录数*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber() {
        Session s = factory.getCurrentSession();
        String hql = "From WeatherData weatherData where 1=1";
        Query q = s.createQuery(hql);
        List weatherDataList = q.list();
        recordNumber = weatherDataList.size();
        int mod = recordNumber % this.PAGE_SIZE;
        totalPage = recordNumber / this.PAGE_SIZE;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public WeatherData GetWeatherDataByWeatherDataId(int weatherDataId) {
        Session s = factory.getCurrentSession();
        WeatherData weatherData = (WeatherData)s.get(WeatherData.class, weatherDataId);
        return weatherData;
    }

    /*更新WeatherData信息*/
    public void UpdateWeatherData(WeatherData weatherData) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(weatherData);
    }

    /*删除WeatherData信息*/
    public void DeleteWeatherData (int weatherDataId) throws Exception {
        Session s = factory.getCurrentSession();
        Object weatherData = s.load(WeatherData.class, weatherDataId);
        s.delete(weatherData);
    }

}
