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
import com.chengxusheji.domain.WeatherData;
import com.chengxusheji.domain.Weather;

@Service @Transactional
public class WeatherDAO {

	@Resource SessionFactory factory;
    /*ÿҳ��ʾ��¼��Ŀ*/
    private final int PAGE_SIZE = 10;

    /*�����ѯ���ܵ�ҳ��*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*�����ѯ�����ܼ�¼��*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*���ͼ����Ϣ*/
    public void AddWeather(Weather weather) throws Exception {
    	Session s = factory.getCurrentSession();
     s.save(weather);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Weather> QueryWeatherInfo(Area areaObj,String weatherDate,WeatherData weatherDataObj,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Weather weather where 1=1";
    	if(null != areaObj && areaObj.getAreaId()!=0) hql += " and weather.areaObj.areaId=" + areaObj.getAreaId();
    	if(!weatherDate.equals("")) hql = hql + " and weather.weatherDate like '%" + weatherDate + "%'";
    	if(null != weatherDataObj && weatherDataObj.getWeatherDataId()!=0) hql += " and weather.weatherDataObj.weatherDataId=" + weatherDataObj.getWeatherDataId();
    	Query q = s.createQuery(hql);
    	/*���㵱ǰ��ʾҳ��Ŀ�ʼ��¼*/
    	int startIndex = (currentPage-1) * this.PAGE_SIZE;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.PAGE_SIZE);
    	List weatherList = q.list();
    	return (ArrayList<Weather>) weatherList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Weather> QueryWeatherInfo(Area areaObj,String weatherDate,WeatherData weatherDataObj) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Weather weather where 1=1";
    	if(null != areaObj && areaObj.getAreaId()!=0) hql += " and weather.areaObj.areaId=" + areaObj.getAreaId();
    	if(!weatherDate.equals("")) hql = hql + " and weather.weatherDate like '%" + weatherDate + "%'";
    	if(null != weatherDataObj && weatherDataObj.getWeatherDataId()!=0) hql += " and weather.weatherDataObj.weatherDataId=" + weatherDataObj.getWeatherDataId();
    	Query q = s.createQuery(hql);
    	List weatherList = q.list();
    	return (ArrayList<Weather>) weatherList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Weather> QueryAllWeatherInfo() {
        Session s = factory.getCurrentSession(); 
        String hql = "From Weather";
        Query q = s.createQuery(hql);
        List weatherList = q.list();
        return (ArrayList<Weather>) weatherList;
    }

    /*�����ܵ�ҳ���ͼ�¼��*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(Area areaObj,String weatherDate,WeatherData weatherDataObj) {
        Session s = factory.getCurrentSession();
        String hql = "From Weather weather where 1=1";
        if(null != areaObj && areaObj.getAreaId()!=0) hql += " and weather.areaObj.areaId=" + areaObj.getAreaId();
        if(!weatherDate.equals("")) hql = hql + " and weather.weatherDate like '%" + weatherDate + "%'";
        if(null != weatherDataObj && weatherDataObj.getWeatherDataId()!=0) hql += " and weather.weatherDataObj.weatherDataId=" + weatherDataObj.getWeatherDataId();
        Query q = s.createQuery(hql);
        List weatherList = q.list();
        recordNumber = weatherList.size();
        int mod = recordNumber % this.PAGE_SIZE;
        totalPage = recordNumber / this.PAGE_SIZE;
        if(mod != 0) totalPage++;
    }

    /*����������ȡ����*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public Weather GetWeatherByWeatherId(int weatherId) {
        Session s = factory.getCurrentSession();
        Weather weather = (Weather)s.get(Weather.class, weatherId);
        return weather;
    }

    /*����Weather��Ϣ*/
    public void UpdateWeather(Weather weather) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(weather);
    }

    /*ɾ��Weather��Ϣ*/
    public void DeleteWeather (int weatherId) throws Exception {
        Session s = factory.getCurrentSession();
        Object weather = s.load(Weather.class, weatherId);
        s.delete(weather);
    }

}
