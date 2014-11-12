/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.googlecode.canoe.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.googlecode.canoe.core.message.MessageCodec;
import com.googlecode.canoe.core.message.Request;
import com.googlecode.canoe.core.message.Response;

/**
 *
 * @author panzd
 */
public class CanoeProtocolCodecFactory implements ProtocolCodecFactory {

    private MessageCodec messageCodec;
    private CanoeProtocolEncoder encoder = new CanoeProtocolEncoder();
    private CanoeProtocolDecoder decoder = new CanoeProtocolDecoder();

    /**
     * @param messageCodec the messageCodec to set
     */
    public void setMessageCodec(MessageCodec messageCodec) {
        this.messageCodec = messageCodec;
    }

    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return encoder;
    }

    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return decoder;
    }

    class CanoeProtocolDecoder extends CumulativeProtocolDecoder {

        @Override
        protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
            if (in.prefixedDataAvailable(2)) {
                short length = in.getShort();
                byte[] data = new byte[length];
                in.get(data);

                Request request = messageCodec.decodeRequest(data);
                out.write(request);

                return true;
            }

            return false;
        }
    }

    class CanoeProtocolEncoder extends ProtocolEncoderAdapter {

        public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {

            byte[] responseBytes = (byte[])messageCodec.encodeResponse((Response) message);

            short responseLength = (short) responseBytes.length;
            IoBuffer buffer = IoBuffer.allocate(2 + responseLength);
            buffer.putShort(responseLength);
            buffer.put(responseBytes);
            buffer.flip();

            out.write(buffer);
        }
    }
}
