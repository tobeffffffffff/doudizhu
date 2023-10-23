
package doudizhu.network;

import io.netty.buffer.ByteBuf;

public interface ICommandSerializer {

    /**
     * 编码
     * @param command
     * @return
     */
    ByteBuf encode(ICommand command);

    /**
     * 解码
     * @param byteBuf
     * @return
     */
    ICommand decode(ByteBuf byteBuf);

}
