package com.chuan.server.business;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import io.netty.buffer.ByteBuf;

public class SimpleDevCache {
	
	// 开发商 - 市场  -  包名
	private Map<String,Map<String,List<String>>> dev2market2pack = new HashMap<String,Map<String,List<String>>>();
	
	
	// 参数规则 num,len,value,len,value...
	public void put(ByteBuf msg){
		int num = msg.readInt();
		for(int i = 0;i<num;i++){
			int len = msg.readInt();
			byte[] dst = new byte[len];
			msg.readBytes(dst, 0, len);
			String str = "";
			try {
				str = new String(dst,"utf-8");
				JSONObject src = JSONObject.parseObject(str);
				String dev = src.getString("dev");
				String market = src.getString("market");
				String packs = src.getString("packs");
				List<String> packList = Arrays.asList(packs.split(","));
				
				if(dev2market2pack.get(dev)==null){
					Map<String,List<String>> market2pack  = new HashMap<String,List<String>>();
					market2pack.put(market, packList);
					dev2market2pack.put(dev, market2pack);
				}else{
					dev2market2pack.get(dev).put(market, packList);
				}
				
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			System.out.println(str);
		}
	}

}
