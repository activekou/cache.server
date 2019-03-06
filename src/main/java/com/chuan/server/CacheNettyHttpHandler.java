package com.chuan.server;

import java.lang.annotation.Annotation;
import java.lang.annotation.AnnotationTypeMismatchException;
import java.lang.reflect.Method;
import java.nio.channels.AsynchronousChannelGroup;
import java.util.HashMap;
import java.util.Map;

import org.omg.Messaging.SyncScopeHelper;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

public class CacheNettyHttpHandler extends SimpleChannelInboundHandler<FullHttpRequest>{

	private static Map<String,Method> methods = new HashMap<String,Method>();
	static {
		Method[] ms = CacheNettyHttpHandler.class.getDeclaredMethods();
		for(Method m:ms) {
			RequestAnnotation req = m.getAnnotation(RequestAnnotation.class);
			if(req!=null) {
				methods.put(req.value(), m);
			}
		}
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
		// TODO Auto-generated method stub
		String uri = msg.uri();
		String k = uri.substring(0, uri.indexOf("/", 1)+1);
		String param = uri.substring(uri.indexOf("/", 1)+1,uri.length());
		System.out.println(k);
		System.out.println(param);
		Method m = methods.get(k);
		m.invoke(this,param,ctx);
	}

	@RequestAnnotation("/restart/")
	private void restart(String path,ChannelHandlerContext ctx) {
		String[] params = path.split("/");
		System.err.println("进入反射方法："+params[0]);
		writeResponse(ctx,HttpResponseStatus.OK,params[0]);
	}
	
	private void writeResponse(ChannelHandlerContext ctx,HttpResponseStatus states,String info) {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, states);
		System.out.println(info);
		ByteBuf buf = Unpooled.copiedBuffer(info,CharsetUtil.UTF_8);
		response.headers().set(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
		response.content().writeBytes(buf);
		ctx.writeAndFlush(response);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("出现异常："+cause);
		ctx.close();
	}
}
