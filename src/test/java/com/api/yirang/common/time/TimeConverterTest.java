package com.api.yirang.common.time;

import com.api.yirang.common.support.time.TimeConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static org.junit.Assert.assertTrue;


@RunWith(JUnit4.class)
public class TimeConverterTest {

    @Test
    public void 시간패턴_제대로_해서_바뀌는지(){
        // variables
        String dateString = "2010-11-12";
        String timeString = "12:00:00";


        String dateTimeString = dateString + " " + timeString;
        LocalDateTime myTime =
                TimeConverter.StringToLocalDateTime(dateTimeString);
        System.out.println(myTime);

        String reProducedString =
                TimeConverter.LocalDateTimeToString(myTime);

        assertTrue(dateTimeString.equals(reProducedString));
    }

    // 24 시간 양식임
    @Test
    public void 시간패턴_어떤양식인지(){
        String dateString = "2010-11-12";
        String timeString = "23:59:59";
        String dateTimeString = dateString + " " + timeString;
        LocalDateTime myTime =
                TimeConverter.StringToLocalDateTime(dateTimeString);
        System.out.println(myTime);

        String reProducedString =
                TimeConverter.LocalDateTimeToString(myTime);

        assertTrue(dateTimeString.equals(reProducedString));
    }

    // 에러가 나옴
    @Test(expected = DateTimeParseException.class)
    public void 시간_범위가_이상해져버렸(){
        String dateString = "2010-11-12";
        String timeString = "25:59:59";
        String dateTimeString = dateString + " " + timeString;
        LocalDateTime myTime =
                TimeConverter.StringToLocalDateTime(dateTimeString);
        System.out.println(myTime);

        String reProducedString =
                TimeConverter.LocalDateTimeToString(myTime);

        assertTrue(dateTimeString.equals(reProducedString));
    }

    // 에러가 나옴
    @Test(expected = DateTimeParseException.class)
    public void 분_범위가_이상해져버렸(){
        String dateString = "2010-11-12";
        String timeString = "23:70:59";
        String dateTimeString = dateString + " " + timeString;
        LocalDateTime myTime =
                TimeConverter.StringToLocalDateTime(dateTimeString);
        System.out.println(myTime);

        String reProducedString =
                TimeConverter.LocalDateTimeToString(myTime);

        assertTrue(dateTimeString.equals(reProducedString));
    }

    @Test(expected = DateTimeParseException.class)
    public void 시간패턴이_이상하면_어떻게_되는가(){
        // variables
        String dateString = "2010-11-1";
        String timeString = "12:00:1";

        String dateTimeString = dateString + " " + timeString;
        LocalDateTime myTime =
                TimeConverter.StringToLocalDateTime(dateTimeString);
        System.out.println(myTime);

        String reProducedString =
                TimeConverter.LocalDateTimeToString(myTime);

        assertTrue(dateTimeString.equals(reProducedString));
    }
}
