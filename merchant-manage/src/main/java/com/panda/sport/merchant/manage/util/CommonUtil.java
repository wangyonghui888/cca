package com.panda.sport.merchant.manage.util;

import com.panda.sport.merchant.common.enums.NoticeTypeEnum;

import java.util.regex.Pattern;

/**
 * @auth: YK
 * @Description:TODO
 * @Date:2020/6/25 21:24
 */
public class CommonUtil {


    private static Pattern PATTERN = Pattern.compile("^[-\\+]?[\\d]*$");
    /**
    * @description: 商户的公共类型和赛事赛程的类型转化
    * @Param: [noticeTypeId]
    * @return: int
    * @author: YK
    * @date: 2020/10/31 11:04
    */
    public static int getMatchNoticeType(Integer noticeTypeId) {

        switch (noticeTypeId) {

            case 4:
                return NoticeTypeEnum.SPORT_NOTICE_TYPE.getCode();
            case 5:
                return NoticeTypeEnum.BASKETBALL_NOTICE_TYPE.getCode();
            case 6:
                return NoticeTypeEnum.TENNIS_NOTICE_TYPE.getCode();
            case 7:
                return NoticeTypeEnum.BADMINTON_NOTICE_TYPE.getCode();
            case 8:
                return NoticeTypeEnum.PP_NOTICE_TYPE.getCode();
            case 9:
                return NoticeTypeEnum.SNOOKER_NOTICE_TYPE.getCode();
            case 10:
                return NoticeTypeEnum.ACTIVITY_NOTICE_TYPE.getCode();
            case 11:
                return NoticeTypeEnum.ICEHOCKEY_NOTICE_TYPE.getCode();
            case 12:
                return NoticeTypeEnum.BASEBALL_NOTICE_TYPE.getCode();
            case 13:
                return NoticeTypeEnum.VOLLEYBALL_NOTICE_TYPE.getCode();
            case 14:
                return NoticeTypeEnum.USEFOOTBALL_NOTICE_TYPE.getCode();
            case 15:
                return NoticeTypeEnum.POLITICS_RECREATION_NOTICE_TYPE.getCode();
            case 16:
                return NoticeTypeEnum.HANDBALL_NOTICE_TYPE.getCode();
            case 17:
                return NoticeTypeEnum.BOXING_NOTICE_TYPE.getCode();
            case 18:
                return NoticeTypeEnum.BEACH_VOLLEYBALL_NOTICE_TYPE.getCode();
            case 19:
                return NoticeTypeEnum.WATER_POLE_NOTICE_TYPE.getCode();
            case 20:
                return NoticeTypeEnum.HOCKEY_NOTICE_TYPE.getCode();
            case 21:
                return NoticeTypeEnum.RUGBY_UNION_NOTICE_TYPE.getCode();
            case 22:
                return NoticeTypeEnum.OTHER_MATCTH_NOTICE_TYPE.getCode();
            case 31:
                return NoticeTypeEnum.LOL.getCode();
            case 32:
                return NoticeTypeEnum.DOTA2.getCode();
            case 33:
                return NoticeTypeEnum.KoG.getCode();
            case 34:
                return NoticeTypeEnum.CS_GO.getCode();
            case 35:
                return NoticeTypeEnum.NBA2K.getCode();
            case 36:
                return NoticeTypeEnum.FIFA.getCode();
            case 3:
                return NoticeTypeEnum.MAINTENANCE_NOTICE_TYPE.getCode();
            case 37:
                return NoticeTypeEnum.ABNORMAL_EVENT_USER.getCode();

            default:
                return NoticeTypeEnum.OTHER_MATCTH_NOTICE_TYPE.getCode();
        }
    }

    /**
     * 根据赛种
     * @param sportId
     * @return
     */
    public static int getMatchNoticeTypeBySportId(String sportId) {

        switch (sportId) {

            case "1" :
                return NoticeTypeEnum.SPORT_NOTICE_TYPE.getCode();
            case "2" :
                return NoticeTypeEnum.BASKETBALL_NOTICE_TYPE.getCode();
            case "5" :
                return NoticeTypeEnum.TENNIS_NOTICE_TYPE.getCode();
            case "10":
                return NoticeTypeEnum.BADMINTON_NOTICE_TYPE.getCode();
            case "8" :
                return NoticeTypeEnum.PP_NOTICE_TYPE.getCode();
            case "7" :
                return NoticeTypeEnum.SNOOKER_NOTICE_TYPE.getCode();
            default:
                return NoticeTypeEnum.ACTIVITY_NOTICE_TYPE.getCode();
        }
    }

    /**
     *
     * @param sportId
     * @return
     */
    public static int getMerchantMatchNoticeTypeBySportId(String sportId) {

        switch (sportId) {

            case "1" :
                return 4;
            case "2" :
                return 5;
            case "5" :
                return 6;
            case "10":
                return 7;
            case "8" :
                return 8;
            case "7" :
                return 9;
            default:
                return 1;
        }
    }



    /**
     * 判断是否为整数
     * @param str 传入的字符串
     * @return是整数返回true,否则返回false
     */
    public static boolean isInteger(String str) {
        return PATTERN.matcher(str).matches();
    }

    public static Integer getCancelOrderNoticeType(Integer sportId) {
        switch (sportId) {
            case 1:
                return 4;
            case 2:
                return 5;
            case 3:
                return 12;
            case 4:
                return 11;
            case 5:
                return 6;
            case 6:
                return 14;
            case 7:
                return 9;
            case 8:
                return 8;
            case 9:
                return 13;
            case 10:
                return 7;
            case 11:
                return 16;
            case 12:
                return 17;
            case 13:
                return 18;
            case 14:
                return 21;
            //todo 补下其他球种对应关系
            default:
                return 22;
        }
    }
}
