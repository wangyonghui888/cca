package com.panda.sport.order.controller;

import com.panda.sport.merchant.common.utils.AesUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.IntStream;

/**
 * @author shaun
 * @version v1.0
 * @date 9/22/23 5:29 pm
 * @description unitTest
 */
@RunWith(SpringRunner.class)
@Slf4j
public class Test {

    @org.junit.Test
    public void dataProviderConvert() {
        String encryptStr = "2gvw8jRTFRALF5sfFwtptA==," +
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
                "MPjbpqntE7ZXadSZdHKm9A==";

        String nickStr = "A01," +
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
                "V11";
        String[] encryptNames = encryptStr.split(",");
        String[] nickNames = nickStr.split(",");
        IntStream.range(0, encryptNames.length).forEach(index -> {
            String encryptName = encryptNames[index];
            String nickName = nickNames[index];
            System.out.printf("%s ----------- %s\n", nickName, AesUtil.decrypt(encryptName));
        });
    }

}