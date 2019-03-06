package com.chuan.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class CacheHttpNetty {
	public static void start() {
		String addr = "127.0.0.1";
		int port = 8888;

		ServerBootstrap bootstrap = new ServerBootstrap();

		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		NioEventLoopGroup workGroup = new NioEventLoopGroup();

		bootstrap.group(bossGroup, workGroup)
		.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<Channel>() {

					@Override
					protected void initChannel(Channel ch) throws Exception {
						// TODO Auto-generated method stub
						ch.pipeline().addLast(new HttpRequestDecoder());
						ch.pipeline().addLast(new HttpResponseEncoder());
						ch.pipeline().addLast(new HttpObjectAggregator(1024 * 1024));
						ch.pipeline().addLast(new CacheNettyHttpHandler());
					}
				});
		try {
			ChannelFuture future = bootstrap.bind(addr, port).sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		start();
	}
	
	
}
