import com.google.common.collect.Lists;
import com.panda.sport.merchant.common.constant.Constant;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringTest {
    public static List<String> SpecialUserNameList = Lists.newArrayList("*", "@", "/", "\\", "&");

    public static void main(String[] args) {
        String a = "*";
        String b = "@";
        String c = "\\";

        System.out.println(SpecialUserNameList.contains(a));
        System.out.println(SpecialUserNameList.contains(b));
        System.out.println(SpecialUserNameList.contains(c));

        String userName = "1_%";
        Pattern p = Pattern.compile(Constant.regEx);
        Matcher m = p.matcher(userName);

        System.out.println("2:" + userName.length());
        System.out.println("1:" + m.find());
        Integer txnid = Integer.parseInt((System.currentTimeMillis() / 10 + "").substring(6));

        System.out.println("txnid=" + txnid);
        String aaa = "usercenter:token:IlYt3fX948J5OYLo6qMiRA==";
        String bbb = "usercenter:token:IlYt3fX948J5OYLo6qMiRA==";
        System.out.println(aaa.equals(bbb));
    }
}
