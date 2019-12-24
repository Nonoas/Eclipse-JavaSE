package SQL_Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class  ContentValues {
	private HashMap<String, Object> mValues;
	/**
	 * �޲ι�����,Ĭ�ϳ���Ϊ8
	 */
	public ContentValues() {
		mValues=new HashMap<String, Object>(8);
	}
	/**
	 * ָ�����ȵĹ�����
	 * @param size mValues����
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
	 * ���س���
	 * @return mValues�ĳ���
	 */
	public long size() {
		return mValues.size();
	}
	
	/**
	 * ��ȡ���еļ���
	 * @return ��������
	 */
	public List<String> getkeys() {
		List<String> list=new ArrayList<String>();
		for(String key:mValues.keySet()) {
			list.add(key);
		}
		return list;
	}
	/**
	 * ��ȡ���е�ֵ
	 * @return ֵ�ü���
	 */
	public List<Object> getValues(){
		List<Object> list=new ArrayList<Object>();
		for(Object key:mValues.values()) {
			list.add(key);
		}
		return list;
	}
}