package cn.windery.sentinel.slots;

import cn.windery.sentinel.slots.degrade.DegradeRuleManager;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class RuleJsonLoaderTest {


    @Test
    void load() throws Exception {

        RuleJsonLoader loader = new RuleJsonLoader();
        loader.load(readFileAsString("sample_rules.json"));
        System.out.println(FlowRuleManager.getInstance().getRules());
        System.out.println(DegradeRuleManager.getInstance().getRules());
    }

    public static String readFileAsString(String filePath) throws Exception {
        InputStream inputStream = RuleJsonLoaderTest.class.getClassLoader().getResourceAsStream(filePath);
        if (inputStream == null) {
            return null; // 或者抛出异常
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }

}