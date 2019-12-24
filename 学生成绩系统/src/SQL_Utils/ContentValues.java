package SQL_Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class  ContentValues {
	private HashMap<String, Object> mValues;
	/**
	 * 无参构造器,默认长度为8
	 */
	public ContentValues() {
		mValues=new HashMap<String, Object>(8);
	}
	/**
	 * 指定长度的构造器
	 * @param size mValues长度
	 */
	public ContentValues(int size) {
		mValues=new HashMap<String, Object>(size);
	}
	public void put(String key,Integer values) {
		mValues.put(key,values);
	}
	public void put(String key,Boolean values) {
		mValues.put(key,values);
	}
	public void put(String key,Float values) {
		mValues.put(key,values);
	}
	public void put(String key,Double values) {
		mValues.put(key,values);
	}
	public void put(String key,String values) {
		mValues.put(key,values);
	}
	public void put(String key,Short values) {
		mValues.put(key,values);
	}
	public Object get(String key) {
		return mValues.get(key);
	}
	/**
	 * 返回长度
	 * @return mValues的长度
	 */
	public long size() {
		return mValues.size();
	}
	
	/**
	 * 获取所有的键名
	 * @return 键名集合
	 */
	public List<String> getkeys() {
		List<String> list=new ArrayList<String>();
		for(String key:mValues.keySet()) {
			list.add(key);
		}
		return list;
	}
	/**
	 * 获取所有的值
	 * @return 值得集合
	 */
	public List<Object> getValues(){
		List<Object> list=new ArrayList<Object>();
		for(Object key:mValues.values()) {
			list.add(key);
		}
		return list;
	}
}