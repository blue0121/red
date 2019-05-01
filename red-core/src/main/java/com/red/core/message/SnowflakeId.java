package com.red.core.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 分布式发号器
 *
 * @author zhengjin
 * @since 1.0 2018年08月23日
 */
public class SnowflakeId
{
	public static final long EPOCH = 1525104000000L; // 开始时间戳 (2018-05-01)
	protected static Logger logger = LoggerFactory.getLogger(SnowflakeId.class);

	protected final long machineIdBits; // 机器ID位数
	protected final long sequenceBits; // 序列在id中占的位数
	protected final long sequenceMask; // 最大序列值
	protected final long sequenceShift; // 序列左移位数
	protected final long timestampShift; // 时间戳左移位数

	private long sequence = 0L;
	protected long machineId; // 机器ID
	protected long lastTimestamp; // 上次时间戳

	/**
	 * 创建分布式发号器，机器ID 10位，序列12位，能用69年
	 *
	 * @param machineId 机器ID
	 */
	public SnowflakeId(long machineId)
	{
		this(10L, 12L, machineId);
	}

	/**
	 * 创建分布式发号器
	 *
	 * @param machineIdBits 机器ID位数
	 * @param sequenceBits 序列ID位数
	 * @param machineId 机器ID
	 */
	public SnowflakeId(long machineIdBits, long sequenceBits, long machineId)
	{
		this.machineIdBits = machineIdBits;
		this.sequenceBits = sequenceBits;
		this.machineId = machineId;
		this.sequenceMask = -1L ^ (-1L << sequenceBits);
		this.sequenceShift = machineIdBits;
		this.timestampShift = machineIdBits + sequenceBits;
	}

	/**
	 * 产生ID
	 */
	public long nextId()
	{
		this.generateId();
		if (machineIdBits == 0L)
		{
			return ((lastTimestamp - EPOCH) << timestampShift)
					| (sequence << sequenceShift);
		}
		else
		{
			return ((lastTimestamp - EPOCH) << timestampShift)
					| (sequence << sequenceShift)
					| machineId;
		}
	}

	/**
	 * 按规则生成ID
	 */
	protected synchronized void generateId()
	{
		long timestamp = System.currentTimeMillis();
		if (timestamp < lastTimestamp)
			throw new IllegalArgumentException(String.format("系统时钟回退，在 %d 毫秒内拒绝生成 ID", lastTimestamp - timestamp));

		if (lastTimestamp == timestamp) // 如果是同一时间生成的，则进行毫秒内序列
		{
			sequence = (sequence + 1) & sequenceMask;
			if (sequence == 0) // 毫秒内序列溢出
			{
				lastTimestamp = this.tillNextTimeUnit(lastTimestamp);
			}
		}
		else // 时间戳改变，毫秒内序列重置
		{
			lastTimestamp = timestamp;
			sequence = 0L;
		}
	}

	/**
	 * 阻塞到下一个毫秒，直到获得新的时间戳
	 *
	 * @param lastTimestamp 上次生成ID的时间戳
	 * @return 当前时间戳
	 */
	protected long tillNextTimeUnit(long lastTimestamp)
	{
		long timestamp = System.currentTimeMillis();
		while (timestamp <= lastTimestamp)
		{
			timestamp = System.currentTimeMillis();
		}
		return timestamp;
	}

	/**
	 * 根据解析SnowflakeId解析出元数据
	 *
	 * @param id SnowflakeId
	 * @return Id元数据
	 */
	public SnowflakeIdMetadata getMetadata(long id)
	{
		if (id <= 0)
			throw new IllegalArgumentException("SnowflakeId不能小于0");

		long timestamp = 0L;
		long sequence = 0L;
		long machineId = this.machineId;
		if (machineIdBits == 0L)
		{
			sequence = id & sequenceMask;
			timestamp = (id >> timestampShift) + EPOCH;
		}
		else
		{
			long machineIdMask = -1L ^ (-1L << machineIdBits);
			machineId = id & machineIdMask;
			sequence = (id >> machineIdBits) & sequenceMask;
			timestamp = (id >> timestampShift) + EPOCH;
		}
		return new SnowflakeIdMetadata(timestamp, sequence, machineId);
	}

}
