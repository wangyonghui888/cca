package com.panda.sport.merchant.common.bo.tournament;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.panda.sport.merchant.common.utils.AesUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Shaun
 * @version v1.0
 * @date 2023/8/27 14:12
 * @description 数据商BO
 */
@ConfigurationProperties(prefix = "tournament")
@RefreshScope
@Component
@Data
public class DataProviderBO {

    private final Set<String> dataProviderSet = new HashSet<>();
    private final Map<String, String> dataProviderMap = new LinkedHashMap<>();
    private final Map<String, String> reverseDataProviderMap = new LinkedHashMap<>();
    /**
     * 加密数据商
     */
    @NacosValue(value = "${tournament.encryptDataProvider:" +
            "2gvw8jRTFRALF5sfFwtptA==," +
            "9lNOu0e0Igm5GBVLcFSdfw==," +
            "hHl8QzC8h/MZx21CzLRC3w==," +
            "+smzOkJ5YQsYdTHjb8yjAw==," +
            "Zo9d+bxqoUQwjRnWAK0XVw==," +
            "1V7SN7imrFr3E0eqpzq5eQ==," +
            "NoWAjrIPhw1XYf5OSURHYg==," +
            "pMicIYCAHFXDlk/ePpffJA==," +
            "36zBpeKLlYgFQmDsXCZGFg==," +
            "dCJ4fuE8kPHCGvEhm2AOxw==," +
            "yes5+iqfFiZ3ivSKLAavnQ==," +
            "QN15yaaVTfRtgGqiINZqqQ==," +
            "In4rlZPwcDV9FOmzitdSog==," +
            "91bxEBg1Z01ecFjrQD65eA==," +
            "Du2S5YXCtepg5Vpe0slamw==," +
            "znwyFOuGMPJepdi0UCAjaw==," +
            "7e2gGIbBYpSBexpIYn7wVw==," +
            "NnYqcNelu9TW7S4XxE21Bw==," +
            "mvNUX5wtDZUl6dcWDcwRwQ==," +
            "jPge5oiD2X47UHDmwcudmw==," +
            "doWY7oFGWlSMMoU4VnAR/A==," +
            "yD6UqJ0zpa5tf+w0/2nXJw==," +
            "MPjbpqntE7ZXadSZdHKm9A==}", autoRefreshed = true)
    private String strEncryptDataProvider;
    /**
     * 数据商昵称
     */
    @NacosValue(value = "${tournament.nickDataProvider:" +
            "A01," +
            "B02," +
            "B03," +
            "B04," +
            "C01," +
            "DJ," +
            "G01," +
            "G02," +
            "H01," +
            "I01," +
            "K01," +
            "L01," +
            "M01," +
            "M02," +
            "O01," +
            "PD," +
            "R01," +
            "S01," +
            "T01," +
            "U01," +
            "V01," +
            "V02," +
            "V11}", autoRefreshed = true)
    private String strNickDataProvider;

    @EventListener(ContextRefreshedEvent.class)
    protected void refresh() {
        this.loadData();
    }

    @PostConstruct
    public void loadData() {
        String[] nickDataProviders = strNickDataProvider.split(",");
        String[] encryptDataProviders = strEncryptDataProvider.split(",");
        int providerLength = encryptDataProviders.length > nickDataProviders.length ? nickDataProviders.length : encryptDataProviders.length;
        dataProviderMap.clear();
        reverseDataProviderMap.clear();
        dataProviderSet.clear();
        for (int i = 0; i < providerLength; i++) {
            String name = nickDataProviders[i];
            String code = AesUtil.decrypt(encryptDataProviders[i]);
            dataProviderMap.put(name, code);
            reverseDataProviderMap.put(code, name);
            dataProviderSet.add(name);
        }
    }
}