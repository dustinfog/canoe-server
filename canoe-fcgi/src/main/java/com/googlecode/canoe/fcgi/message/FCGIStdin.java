package com.googlecode.canoe.fcgi.message;

import java.nio.ByteBuffer;

import com.googlecode.canoe.fcgi.FCGIException;
import com.googlecode.canoe.fcgi.constant.FCGIHeaderType;

/**
 *
 * @author panzd
 */
public class FCGIStdin {
    public static final FCGIStdin NULL = new FCGIStdin();

    private FCGIHeader header;
    private byte[] data;

    public FCGIStdin(byte[] data)
    {
        if(data == null){
            throw new FCGIException("FCGI_STDIN's data can't be null");
        }

        this.data = data;
        header = new FCGIHeader(FCGIHeaderType.FCGI_STDIN, data.length);
    }

    public FCGIStdin(String str)
    {
        this(str != null ? str.getBytes() : null);
    }

    public int getLength()
    {
        return data == null ? 0 : data.length;
    }

    private FCGIStdin()
    {
        header = new FCGIHeader(FCGIHeaderType.FCGI_STDIN, 0);
    }

    public ByteBuffer[] getByteBuffers() {
        if(data == null)
        {
            return new ByteBuffer[]{header.getByteBuffer()};
        }
        else
        {
            ByteBuffer[] buffers = new ByteBuffer[2];

            buffers[0] = header.getByteBuffer();
            buffers[1] = ByteBuffer.wrap(data);
            return buffers;
        }
    }
}
