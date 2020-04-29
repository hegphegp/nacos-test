package com.hegp.controller;

import com.hegp.config.NacosEventListenerConfiguration;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static com.alibaba.nacos.api.common.Constants.DEFAULT_GROUP;

/**
 * @description:
 */
@RestController
@RequestMapping("/config")
public class ConfigController {

    @NacosValue(value = "${useLocalCache:false}", autoRefreshed = true)
    private boolean useLocalCache;

    @NacosInjected
    private ConfigService configService;

    @GetMapping(value = "/get")
    @ResponseBody
    public boolean get() {
        return useLocalCache;
    }

    @GetMapping(value = "/register")
    @ResponseBody
    public boolean register() {
        try {
            configService.publishConfig(NacosEventListenerConfiguration.EXAMPLE, DEFAULT_GROUP, "useLocalCache = true");
            return true;
        } catch (NacosException e) {
            e.printStackTrace();
            return false;
        }
    }

    @GetMapping(value = "/remove")
    @ResponseBody
    public boolean remove() {
        try {
            configService.removeConfig(NacosEventListenerConfiguration.EXAMPLE, DEFAULT_GROUP);
            return true;
        } catch (NacosException e) {
            e.printStackTrace();
            return false;
        }
    }

    @NacosConfigListener(dataId = NacosEventListenerConfiguration.EXAMPLE, groupId = "DEFAULT_GROUP")
    public void onMessage(String config) {
        System.out.println(config);
    }
}
