module red.client
{
    requires red.core;
    requires com.github.benmanes.caffeine;

    exports blue.red.client;
    exports blue.red.client.cache;
    exports blue.red.client.registry;
    exports blue.red.client.net;
    exports blue.red.client.config;
}