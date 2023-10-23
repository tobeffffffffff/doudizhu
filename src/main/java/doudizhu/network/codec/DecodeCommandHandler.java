

package doudizhu.network.codec;

import doudizhu.network.ICommandSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class DecodeCommandHandler extends ChannelInboundHandlerAdapter {

    private ICommandSerializer iCommandSerializer;

    public DecodeCommandHandler(ICommandSerializer commandSerializer) {
        this.iCommandSerializer = commandSerializer;
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object arg1) {
        if (arg1 == null) {
            context.close();
            return;
        }
        ByteBuf byteBuf = (ByteBuf) arg1;
        if (byteBuf.isReadable()) {
            System.out.println((char) byteBuf.readByte());
            System.out.flush();
        }
//        final ICommand command = iCommandSerializer.decode(byteBuf);
//        if (command == null) {
//            context.close();
//            return;
//        }
//        context.fireChannelRead(command);
    }

}
