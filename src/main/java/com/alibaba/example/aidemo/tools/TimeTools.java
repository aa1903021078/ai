package com.alibaba.example.aidemo.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 时间转换工具，供 AI 调用：把"昨天/明天"等相对时间描述转换成具体日期
 */
@Component("timeTools")
public class TimeTools {

    private static final Pattern N_DAYS_PATTERN = Pattern.compile("(\\d+)天[前后]");

    /**
     * 相对天数描述 -> 天数偏移，按长词优先顺序匹配
     */
    private static final Map<String, Long> RELATIVE_DAYS = new LinkedHashMap<>();

    /**
     * 星期描述 -> ISO 星期值(1=周一 ... 7=周日)
     */
    private static final Map<String, Integer> WEEK_DAYS = new LinkedHashMap<>();

    static {
        RELATIVE_DAYS.put("大后天", 3L);
        RELATIVE_DAYS.put("后天", 2L);
        RELATIVE_DAYS.put("明天", 1L);
        RELATIVE_DAYS.put("今天", 0L);
        RELATIVE_DAYS.put("昨天", -1L);
        RELATIVE_DAYS.put("前天", -2L);

        WEEK_DAYS.put("星期一", 1);
        WEEK_DAYS.put("周一", 1);
        WEEK_DAYS.put("星期二", 2);
        WEEK_DAYS.put("周二", 2);
        WEEK_DAYS.put("星期三", 3);
        WEEK_DAYS.put("周三", 3);
        WEEK_DAYS.put("星期四", 4);
        WEEK_DAYS.put("周四", 4);
        WEEK_DAYS.put("星期五", 5);
        WEEK_DAYS.put("周五", 5);
        WEEK_DAYS.put("星期六", 6);
        WEEK_DAYS.put("周六", 6);
        WEEK_DAYS.put("星期日", 7);
        WEEK_DAYS.put("周日", 7);
        WEEK_DAYS.put("周天", 7);
    }

    /**
     * 把相对时间描述转换成具体日期
     */
    @Tool("当用户使用\"今天\"\"明天\"\"后天\"\"大后天\"\"昨天\"\"前天\"\"3天后\"\"下周三\"等相对时间描述时，调用本工具把描述转换为具体日期 yyyy-MM-dd")
    public String getDate(@P("用户的相对时间描述，如：明天、昨天、后天、3天后、下周三") String description) {

        LocalDate today = LocalDate.now();
        LocalDate date = resolve(today, description);

        if (date == null) {
            return "无法识别该时间描述，请以今天(" + today + ")为基准向用户确认具体日期";
        }
        return date.toString();
    }

    private LocalDate resolve(LocalDate today, String description) {
        if (description == null) {
            return null;
        }
        String text = description.trim();

        // 今天/明天/后天/大后天/昨天/前天
        for (Map.Entry<String, Long> entry : RELATIVE_DAYS.entrySet()) {
            if (text.contains(entry.getKey())) {
                return today.plusDays(entry.getValue());
            }
        }

        // N天后 / N天前
        Matcher matcher = N_DAYS_PATTERN.matcher(text);
        if (matcher.find()) {
            int days = Integer.parseInt(matcher.group(1));
            return text.contains("后") ? today.plusDays(days) : today.minusDays(days);
        }

        // 本周X / 下周X
        boolean nextWeek = text.contains("下周");
        for (Map.Entry<String, Integer> entry : WEEK_DAYS.entrySet()) {
            if (text.contains(entry.getKey())) {
                int target = entry.getValue();
                int todayValue = today.getDayOfWeek().getValue();
                int days = (target - todayValue + 7) % 7;
                if (nextWeek) {
                    days += 7;
                }
                return today.plusDays(days);
            }
        }
        return null;
    }
}
