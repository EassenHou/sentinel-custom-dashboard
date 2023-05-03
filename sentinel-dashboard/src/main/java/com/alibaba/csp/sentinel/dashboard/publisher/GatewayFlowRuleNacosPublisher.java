package com.alibaba.csp.sentinel.dashboard.publisher;

/**
 * @Author eassen
 * @Create 2023/4/29 22:33
 */

import com.alibaba.csp.sentinel.dashboard.config.nacos.NacosConfigUtil;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.GatewayFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.ConfigType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

// 注意，不一样的名称
@Component("gatewayFlowRuleNacosPublisher")
public class GatewayFlowRuleNacosPublisher implements DynamicRulePublisher<List<GatewayFlowRuleEntity>> {
    @Autowired
    private ConfigService configService;

    @Value("${nacos.group}") // 对应于application.properties里面的配置
    private String group;
    // 使用到上面定义的Converter
    @Autowired
    private Converter<List<GatewayFlowRuleEntity>, String> converter;

    @Override
    public void publish(String app, List<GatewayFlowRuleEntity> rules) throws Exception {
        AssertUtil.notEmpty(app, "app name cannot be empty");
        if (rules == null) {
            return;
        }
        // 通过app服务名+后缀: 还记得我们前面说的，后缀是：-sentinel-flow, 这样就能找到nacos里面的配置信息
        // 如果没有设置group ,缺省就是DEFAULT_GROUP
        // 这儿，实际上是调用NacosConfigService的功能对nacos进行操作
        configService.publishConfig(app + NacosConfigUtil.FLOW_DATA_ID_POSTFIX,
                group == null ? NacosConfigUtil.GROUP_ID : group, converter.convert(rules), ConfigType.JSON.getType());
    }

}
