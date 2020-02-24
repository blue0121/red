module red.core
{
    requires transitive org.slf4j;
    requires transitive io.netty.all;
    requires fastjson;
    requires snappy.java;

    exports blue.red.core.codec;
    exports blue.red.core.handler;
    exports blue.red.core.message;
    exports blue.red.core.util;
}