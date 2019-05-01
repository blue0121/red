package com.red.core.message;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 分布式发号器元数据
 *
 * @author zhengjin
 * @since 1.0 2018年10月16日
 */
public class SnowflakeIdMetadata
{
	private LocalDateTime time;
	private long sequence;
	private long machineId;

	public SnowflakeIdMetadata(long timestamp, long sequence, long machineId)
	{
		LocalDateTime time = Instant.ofEpochMilli(timestamp)
				.atZone(ZoneId.systemDefault())
				.toLocalDateTime();
		this.time = time;
		this.sequence = sequence;
		this.machineId = machineId;
	}

	public SnowflakeIdMetadata(LocalDateTime time, long sequence, long machineId)
	{
		this.time = time;
		this.sequence = sequence;
		this.machineId = machineId;
	}

	public LocalDateTime getTime()
	{
		return time;
	}

	public long getSequence()
	{
		return sequence;
	}

	public long getMachineId()
	{
		return machineId;
	}
}
