package blue.red.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @author Jin Zheng
 * @since 1.0 2019-07-26
 */
public class JsonUtil
{
	private static SerializerFeature[] serializer = new SerializerFeature[] {
			SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue,
			SerializerFeature.WriteClassName};

	private static Feature[] feature = new Feature[] {};

	static
	{
		ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
	}

	private JsonUtil()
	{
	}

	public static byte[] toBytes(Object object)
	{
		if (object == null)
			return new byte[0];

		return JSON.toJSONBytes(object, serializer);
	}

	public static <T> T fromBytes(byte[] bytes)
	{
		if (bytes == null || bytes.length == 0)
			return null;

		return JSON.parseObject(bytes, Object.class, feature);
	}

}
