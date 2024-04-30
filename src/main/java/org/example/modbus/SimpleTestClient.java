package org.example.modbus;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;


/**
 * @author:lmw
 * @date:2024/4/12 21:17
 **/
public class SimpleTestClient {

    static ModbusMessage message;

    public static void main(String[] args) throws InterruptedException {
        //客户端需要一个事件循环组
        EventLoopGroup eventExecutors=new NioEventLoopGroup();
        String[] arg;
        byte code;

        try{
            //创建客户端启动对象
            Bootstrap bootstrap =new Bootstrap();
            //设置参数
            bootstrap.group(eventExecutors)//设置线程组
                    .channel(NioSocketChannel.class)//设置客户端通道的实现类（反射）
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline().addLast(new ModbusMessageEnCoder());
                            channel.pipeline().addLast(new ModbusClientHandler());//加入自己的处理器
                        }
                    });
            System.out.println("客户端 ok..");
            //启动客户端连接服务器端
            ChannelFuture channelFuture=bootstrap.connect("127.0.0.1",502).sync();

            Channel channel=channelFuture.channel();
            Scanner scanner =new Scanner(System.in);

            while (scanner.hasNextLine()){
                String msg=scanner.nextLine();
                arg=msg.trim().split(",");
                code=MainWork.convest(arg[1]);
                if (code==0xff){
                    continue;
                }
                message=MainWork.loadmessage(arg,code);
                if(arg[0].equals("1")){
                    channel.writeAndFlush(message);
                }else{
                    channel.eventLoop().scheduleAtFixedRate(new Runnable() {
                        @Override
                        public void run() {
                            channel.writeAndFlush(message);
                        }
                    },1,5, TimeUnit.SECONDS);
                }

            }


            //对通道关闭进行监听
            channelFuture.channel().closeFuture().sync();
        }finally {
            eventExecutors.shutdownGracefully();
        }
    }

}
