package com.zzc.baselib.util;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.format.Time;

import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Formatter;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static final String[] constellationArray = {"水瓶座", "双鱼座", "牡羊座",
            "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座"};
    public static final int[] constellationEdgeDay = {20, 19, 21, 21, 21, 22,
            23, 23, 23, 23, 22, 22};
    private static DecimalFormat df = new DecimalFormat("0.00");

    public static String getTimeStr(int time) {
        int second = time / 1000;
        int h = second / 3600;
        int m = (second - h * 3600) / 60;
        int s = second % 60;
        return getFillTenPos(h) + ":" + getFillTenPos(m) + ":" + getFillTenPos(s);
    }

    public static String getFillTenPos(int val) {
        return (val < 10 ? "0" : "") + val;
    }

    public static String getMd5Str(String key) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(key.getBytes());
            StringBuffer buf = new StringBuffer();
            int i;
            for (int offset = 0; offset < bs.length; offset++) {
                i = bs[offset];
                if (i < 0) i += 256;
                if (i < 16) buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "not_valid";
    }

    public static String changeVipUrl(String normalUrl) {
        if (TextUtils.isEmpty(normalUrl)) return "";
        int index = normalUrl.lastIndexOf(".");
        if (index != -1) {
            return normalUrl.substring(0, index) + "_vip" + normalUrl.substring(index);
        } else {
            return normalUrl;
        }
    }

    public static boolean hasGif(String url) {
        if (!TextUtils.isEmpty(url) && url.contains(".gif")) {
            return true;
        }
        return false;
    }

    public static String getSizeDesc(long size) {
        float k = (float) size / 1024;
        float m = k / 1024;
        float g = m / 1024;
        if (m < 1 && g < 1) {
            return df.format(k) + "K";
        } else if (g < 1) {
            return df.format(m) + "M";
        } else {
            return df.format(g) + "G";
        }
    }

    public static String getSpeedDesc(long size, float diffTime) {
        if (diffTime != 0) {
            float k = (float) size / (1024 * diffTime);
            float m = k / (1024 * diffTime);
            float g = m / (1024 * diffTime);
            if (m < 1 && g < 1) {
                return df.format(k) + "Kb/s";
            } else if (g < 1) {
                return df.format(m) + "Mb/s";
            } else {
                return df.format(g) + "Gb/s";
            }
        } else {
            return "0Kb/s";
        }
    }

    public static String getPercentDesc(long val, long total) {
        if (total == 0) {
            return "0%";
        }
        return val * 100 / total + "%";
    }

    public static String match(String input, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "";
        }
    }

    /**
     * 判断给定字符串是否空白串。
     * 空白串是指由空格、制表符、回车符、换行符组成的字符串
     * 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    public static String passedTime(long timeMs) {
//        long newTime = System.currentTimeMillis() - timeMs;
//        long totalSeconds = newTime / 1000;
//        int minutes = (int) Math.ceil(totalSeconds / 60);
//        int hours = (int) Math.ceil(totalSeconds / 3600);
//        int day = (int) Math.ceil(hours / 24);
//        int yue = (int) Math.ceil(day / 30);
//        if(totalSeconds <= 10){
//            return "刚刚";
//        }else if (totalSeconds < 60) {
//            return totalSeconds + "秒前";
//        } else if (minutes < 60) {
//            return minutes + "分钟前";
//        } else if (hours < 24) {
//            return hours + "小时前";
//        } else if (day < 30) {
//            return day + "天前";
//        } else {
//            return yue + "个月前";
//        }
        return formatTimeStampString(AppBase.app, timeMs, false);
    }

    public static String formatTimeStampString(Context context, long when, boolean fullFormat) {
        Time then = new Time();
        then.set(when);
        Time now = new Time();
        now.setToNow();
        // Basic settings for formatDateTime() we want for all cases.
        int format_flags = DateUtils.FORMAT_NO_NOON_MIDNIGHT |
                DateUtils.FORMAT_ABBREV_ALL |
                DateUtils.FORMAT_CAP_AMPM;
        // If the message is from a different year, show the date and year.
        if (then.year != now.year) {
            format_flags |= DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME;
        } else if (then.yearDay != now.yearDay) {
            // If it is from a different day than today, show only the date.
            format_flags |= DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME;
        } else {
            // Otherwise, if the message is from today, show the time.
            format_flags |= DateUtils.FORMAT_SHOW_TIME;
        }
        // If the caller has asked for full details, make sure to show the date
        // and time no matter what we've determined above (but still make showing
        // the year only happen if it is a different year from today).
        if (fullFormat) {
            format_flags |= (DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME);
        }
        return DateUtils.formatDateTime(context, when, format_flags);
    }

    public static String durationTime(long timeMs) {
        long totalSeconds = timeMs / 1000;

        StringBuilder mFormatBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;

        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    //18:34 hh:mm
    public static String hmTime(long timeMs) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMs);
        return String.format("%d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    }

    //7:15 mm-dd
    public static String mdTime(long timeMs) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMs);
        return String.format("%d-%02d", calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    public static String shortNumStr(long num) {
        if (num < 10000) {
            return String.valueOf(num);
        } else {
            return String.format("%.1f万", (float) num / 10000);
        }
    }

    public static String defStr(String str) {
        return defStr(str, "");
    }

    public static String defStr(String str, String def) {
        return str == null ? def : str;
    }

    public static String join(Collection<String> collection, String separator) {
        if (collection == null || collection.isEmpty())
            return "";
        Iterator<String> it = collection.iterator();
        StringBuilder builder = new StringBuilder(it.next());
        while (it.hasNext()) {
            String string = it.next();
            if (!TextUtils.isEmpty(string))
                builder.append(separator).append(string);
        }
        return builder.toString();
    }

    public static int getAgeFromBirthTime(String birthTimeString) {
        // 先截取到字符串中的年、月、日
        String strs[] = birthTimeString.trim().split("-");
        int selectYear = Integer.parseInt(strs[0]);
        int selectMonth = Integer.parseInt(strs[1]);
        int selectDay = Integer.parseInt(strs[2]);
        // 得到当前时间的年、月、日
        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayNow = cal.get(Calendar.DATE);

        // 用当前年月日减去生日年月日
        int yearMinus = yearNow - selectYear;
        int monthMinus = monthNow - selectMonth;
        int dayMinus = dayNow - selectDay;

        int age = yearMinus;// 先大致赋值
        if (yearMinus < 0) {// 选了未来的年份
            age = 0;
        } else if (yearMinus == 0) {// 同年的，要么为1，要么为0
            if (monthMinus < 0) {// 选了未来的月份
                age = 0;
            } else if (monthMinus == 0) {// 同月份的
                if (dayMinus < 0) {// 选了未来的日期
                    age = 0;
                } else if (dayMinus >= 0) {
                    age = 1;
                }
            } else if (monthMinus > 0) {
                age = 1;
            }
        } else if (yearMinus > 0) {
            if (monthMinus < 0) {// 当前月>生日月
            } else if (monthMinus == 0) {// 同月份的，再根据日期计算年龄
                if (dayMinus < 0) {
                } else if (dayMinus >= 0) {
                    age = age + 1;
                }
            } else if (monthMinus > 0) {
                age = age + 1;
            }
        }
        return age;
    }

    public static String date2Constellation(Calendar time) {
        int month = time.get(Calendar.MONTH);
        int day = time.get(Calendar.DAY_OF_MONTH);
        if (day < constellationEdgeDay[month]) {
            month = month - 1;
        }
        if (month >= 0) {
            return constellationArray[month];
        }
        // default to return 魔羯
        return constellationArray[11];
    }

}