package com.chuan.server;

import java.util.List;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class CacheNettyHandler extends SimpleChannelInboundHandler<ByteBuf>{

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		// 参数规则 cmd,num,len,value,len,value...
		int cmd = msg.readUnsignedByte();
		//1:get,2:put
		if(cmd==1){
			Map<String,List<String>> result = Main.get(msg);
			String res = result.toString();
			System.out.println("要输出的数据："+res);
			ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
			buf.writeInt(res.getBytes().length);
			buf.writeBytes(res.getBytes());
			Channel channel = ctx.channel();
			channel.writeAndFlush(Unpooled.wrappedBuffer(buf));
		}else if(cmd==2){
			Main.put(msg);
		}
	}
	
}
