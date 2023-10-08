import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.*;
import java.time.temporal.IsoFields;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class DateTest {




    private static LocalDate getDateByYearAndWeekNumAndDayOfWeek(Integer year, Integer num, DayOfWeek dayOfWeek) {
        //周数小于10在前面补个0
        String numStr = num < 10 ? "0" + String.valueOf(num) : String.valueOf(num);
        //2019-W01-01获取第一周的周一日期，2019-W02-07获取第二周的周日日期
        String weekDate = String.format("%s-W%s-%s", year, numStr, dayOfWeek.getValue());
        return LocalDate.parse(weekDate, DateTimeFormatter.ISO_WEEK_DATE);
    }

}
