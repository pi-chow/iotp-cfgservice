package com.cetiti.iotpcfgservice.common.id;

import com.cetiti.iotpcfgservice.common.utils.DateUtils;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * id生成器。
 *
 * @author weiyinglei
 */
public class IdGenerator {

    /**
     * module.date
     */
    private String seq_GENERATOR_KEY_PREFIX = "id:seq:%s:%s";

    @SuppressWarnings("rawtypes")
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 得到当前序列值。
     *
     * @param type
     * @return
     */
    protected long getCurrentSeq(String type) {
        Preconditions.checkArgument(type != null && type.trim().length() > 0);

        String key = String.format(seq_GENERATOR_KEY_PREFIX, type, new SimpleDateFormat("yyyyMMdd").format(new Date()));
        String value = stringRedisTemplate.opsForValue().get(key);
        if (value == null) {
            return 0;
        }
        return Long.parseLong(value);
    }

    /**
     * 加1并且返回当前序列值。
     *
     * @param type
     * @return
     */
    protected long incSeqAndGet(String type) {
        Preconditions.checkArgument(type != null && type.trim().length() > 0);

        String key = String.format(seq_GENERATOR_KEY_PREFIX, type, new SimpleDateFormat("yyyyMMdd").format(new Date()));

        RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        long value = entityIdCounter.getAndIncrement();

        if (value == 0) {
            entityIdCounter.expireAt(DateUtils.todayLastDate());
        }

        return value;
    }

    /**
     * 7位长度的序列值。
     *
     * @param type
     * @return
     */
    protected String generateSeqString(String type) {
        Preconditions.checkArgument(type != null && type.trim().length() > 0);

        long seq = incSeqAndGet(type);
        return frontCompWithZore(seq, 7);
    }

    /**
     * 三位随机数。
     *
     * @return
     */
    protected String generateRandomString() {
        int random = new Random().nextInt(999);
        return frontCompWithZore(random, 3);
    }

    /**
     * 将元数据前补零，补后的总长度为指定的长度，以字符串的形式返回。
     *
     * @param sourceDate
     * @param formatLength
     * @return 重组后的数据
     */
    public static String frontCompWithZore(long sourceDate, int formatLength) {
        String newString = String.format("%0" + formatLength + "d", sourceDate);
        return newString;
    }
}
