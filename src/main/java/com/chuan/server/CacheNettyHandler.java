package com.chuan.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class CacheNettyHandler extends SimpleChannelInboundHandler<ByteBuf>{

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		// 参数规则 cmd,num,len,value,len,value...
		int cmd = msg.readUnsignedByte();
		//1:get,2:put
		if(cmd==1){
			
		}else if(cmd==2){
			Main.put(msg);
		}
	}
	
}
