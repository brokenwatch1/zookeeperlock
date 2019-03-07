package com.chenlei.test;

import java.io.UnsupportedEncodingException;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

public class MyZkSerializer implements ZkSerializer{

	@Override
	public Object deserialize(byte[] arg0) throws ZkMarshallingError {
		 try {
			return new String(arg0, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return null;
	}

	@Override
	public byte[] serialize(Object arg0) throws ZkMarshallingError {
		try {
			return String.valueOf(arg0).getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }

}
