package org.example.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelMatcher;

/**
 * @author:lmw
 * @date:2024/3/19 20:43
 **/
public class MyMatcher implements  ChannelMatcher{

    Channel ch;
    public MyMatcher(Channel ch){
            this.ch=ch;
    }
    @Override
    public boolean matches(Channel channel) {
        if(channel !=ch) {
            return true;
        }
        return false;
    }
}
