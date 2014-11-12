package com.googlecode.canoe.amf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.exadel.flamingo.flex.messaging.amf.io.AMF3Deserializer;
import com.exadel.flamingo.flex.messaging.amf.io.AMF3Serializer;
import com.googlecode.canoe.core.message.Message;
import com.googlecode.canoe.core.message.Request;
import com.googlecode.canoe.core.message.Response;
import com.googlecode.canoe.core.message.support.AbstractMessageCodec;

/**
 *
 * @author dingdang
 */
public class AMFMessageCodec extends AbstractMessageCodec {
	private static Logger log = LoggerFactory.getLogger(AMFMessageCodec.class);

	public AMFMessageCodec() {
		super();
	}

	@Override
	protected Request doDecodeRequest(Object message) {
		byte[] bytes = (byte[]) message;
		Request request = new Request();

		decodeMessage(bytes, request);

		return request;
	}

	@Override
	protected Object doEncodeResponse(Response response) {
		if (response == null) {
			return null;
		}

		return encodeMessage(response);
	}

	protected byte[] encodeMessage(Message message) {
		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		AMF3Serializer out = new AMF3Serializer(bytesOut);

		try {
			out.writeShort(message.getOpcode());
			if (message.getData() != null) {
				out.writeObject(message.getData());
			}

			out.flush();
			bytesOut.flush();
			return bytesOut.toByteArray();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			return null;
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}

			try {
				bytesOut.close();
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	protected void decodeMessage(byte[] bytes, Message message) {
		AMF3Deserializer in = new AMF3Deserializer(new ByteArrayInputStream(
				bytes));
		short opcode;

		try {
			try {
				opcode = in.readShort();
				message.setOpcode(opcode);
				if (in.available() != 0) {
					message.setData(in.readObject());
				}
			} catch (IOException ex) {
				log.error(ex.getMessage(), ex);
			}
		} finally {
			try {
				in.close();
			} catch (IOException ex) {
				log.error(ex.getMessage(), ex);
			}
		}
	}
}
