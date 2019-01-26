package com.chuan.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

public class CacheNetty {
	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;
	private ServerBootstrap boostrap;

	public void start() throws InterruptedException {
		String addr = "127.0.0.1:1350";
		String host = addr.split(":")[0];
		int port = Integer.parseInt(addr.split(":")[1]);
		boostrap = new ServerBootstrap();
		bossGroup = new NioEventLoopGroup(1);
		workerGroup = new NioEventLoopGroup(2);
		boostrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
				.childOption(ChannelOption.SO_KEEPALIVE, true).childHandler(new ChannelInitializer<Channel>() {

					@Override
					protected void initChannel(Channel ch) throws Exception {
						// TODO Auto-generated method stub
						ChannelPipeline p = ch.pipeline();
						p.addLast("frameEncoder", new LengthFieldPrepender(4));
						p.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
						p.addLast("idleStateHandler", new IdleStateHandler(0, 0, 300)); // 5·ÖÖÓ¿ÕÏÐ¶Ï¿ª
						p.addLast("handler",new CacheNettyHandler());
					}
				});
		boostrap.bind(host, port).sync();
	}
}
