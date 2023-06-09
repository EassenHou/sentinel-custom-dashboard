/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.csp.sentinel.dashboard.config.degrde;

import com.alibaba.csp.sentinel.dashboard.config.nacos.NacosConfigUtil;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.ConfigType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author
 * @since 1.8.0
 */
@Component("degradeRuleNacosPublisher")
public class DegradeRuleNacosPublisher implements DynamicRulePublisher<List<DegradeRuleEntity>> {

    @Autowired
    private ConfigService configService;
    @Value("${nacos.group}") // 对应于application.properties里面的配置
    private String group;
    @Autowired
    private Converter<List<DegradeRuleEntity>, String> converter;

    @Override
    public void publish(String appName, List<DegradeRuleEntity> rules) throws Exception {
        AssertUtil.notEmpty(appName, "appName cannot be empty");
        if (rules == null) {
            return;
        }
//        String dataId = new StringBuffer(appName).append(NacosConfigUtil.DEGRADE_DATA_ID_POSTFIX).toString();
//        configService.publishConfig(dataId, NacosConfigUtil.GROUP_ID, converter.convert(rules));
        configService.publishConfig(appName + NacosConfigUtil.DEGRADE_DATA_ID_POSTFIX,
                group == null ? NacosConfigUtil.GROUP_ID : group, converter.convert(rules), ConfigType.JSON.getType());


    }
}
