import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import com.panda.sport.merchant.api.util.Md5Util;
import com.panda.sport.merchant.common.constant.Constant;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecurityTest {
    public static List<String> SpecialUserNameList = Lists.newArrayList("*", "@", "/", "\\", "&");
/*
    public static void main(String[] args) {
        String merchantCode = "oubao";
        String timestamp = new Date().getTime() + "";

        String signature = Md5Util.getMD5(merchantCode + "&" + timestamp,
                "sdfsdfdsfsd");

    }*/
/*
    public static void main(String[] args) {
        String oldDomain = "23424";
        String newDomain ="www.yahoo.com";
        String techSupport = "FDC67978BEC0E0CA84C9FA819470E092";
        String signature = Md5Util.getMD5(oldDomain + "&" + newDomain,
                techSupport);

        System.out.println("查询最新域名:" + signature);
    }

*/

    //  public static void main(String[] args) {


    //BW   el_1W!4z+<x_K1SY+prYi#C~iUCxX$

    //BW Product 472028  &uwQ9kinclnHYiED6&cdEKB$IuCH81

    //test  I%xs>byi3U&@pDZ~0>nH6k<f_1Ho+z
    //oubao  sdfsdfdsfsd

/*

    public static void main(String[] args) {
        String startTime = "1634743440000";
        String endTime = "1640931988913";
        String merchantCode = "111111";
        String timestamp = new Date().getTime() + "";

        String signature = Md5Util.getMD5(merchantCode + "&" + startTime + "&" + endTime + "&" + timestamp, "Q4uGGwP97hYU?@x~wH7~@Mm1da0qNr");

        System.out.println("查询订单:" + signature + "  timestamp:" + timestamp);
    }

*/



/*        String orderNo = "10633096937472";
        String merchantCode = "test";

        String timestamp = "1587819730000";
        String signature = Md5Util.getMD5(merchantCode + "&" + orderNo + "&" + timestamp, "I%xs>byi3U&@pDZ~0>nH6k<f_1Ho+z");
        System.out.println(signature);*/


/*
        String merchantCode = "602625";
        String timestamp = new Date().getTime() + "";
        String transferType = "2";
        String amount = "11";
        String userName = "kokalongalong";

        String transferId = "2021213088771088192";
        String signature = Md5Util.getMD5(merchantCode + "&" + userName + "&" + transferType + "&" + amount + "&" + transferId + "&"
                + timestamp, "&uwQ9kinclnHYiED6&cdEKB$IuCH81");
        System.out.println("商户上下分:signature:" + signature + " 时间戳:" + timestamp);


*/




/*
        String transferId = "341234666662143123";

        String merchantCode = "105627";

        String timestamp = new Date().getTime() + "";

        String signature = Md5Util.getMD5(merchantCode + "&" + transferId + "&" + timestamp, "&uwQ9kinclnHYiED6&cdEKB$IuCH81");

        System.out.println("查询转账记录:signature:" + signature + " 时间戳:" + timestamp);
*/


/*    public static void main(String[] args) {

        String userName = "htf13520";
        Integer bizType = 2;
        String merchantCode = "TX323";
        Long transferId = 2330066356583807533L;
        String amountStr = "0.0";
        Integer transferType = 1;
        Long now = 1650331700000L;

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(userName).append("&")
                .append(bizType).append("&")
                .append(merchantCode).append("&")
                .append(transferId).append("&")
                .append(amountStr).append("&")
                .append(transferType).append("&")
                .append(now);

        String signature = Md5Util.getMD5(stringBuilder.toString(),
                "WiF$OMG<GZQ#K0lJvzZPQ$Yzt!g6zM");

        System.out.println(signature);

    }*/


    public static void main(String[] args) {
        String merchantCode = "784778";
        //String merchantCode = "526016";
        String userName = "junkaitest";
        // String uid = "165334513317519360";
        String terminal = "mobile";
        String timestamp = new Date().getTime() + "";
/*
        String signature = Md5Util.getMD5(merchantCode + "&" + userName + "&" + terminal + "&" + timestamp,
                "GfM0yQDm46WjX@h7AR!iF&S@auSM>2");*/
        String signature = Md5Util.getMD5(merchantCode + "&" + userName + "&" + terminal + "&" + timestamp,
                "&uwQ9kinclnHYiED6&cdEKB$IuCH81");


/*        String signature = Md5Util.getMD5(merchantCode + "&" + userName + "&" + terminal + "&" + timestamp,
                "sdfsdfdsfsd");*/

/*

        String signature = Md5Util.getMD5(merchantCode + "&" + uid + "&" + timestamp,
                "Q4uGGwP97hYU?@x~wH7~@Mm1da0qNr"); sdfsdfdsfsd

*/


        System.out.println("登录:" + signature + "  时间戳:" + timestamp);
    }

/*

    public static void main(String[] args) {
        String merchantCode = "379916";
        String userName = "k05victory888";
        String timestamp = new Date().getTime() + "";

        String signature = Md5Util.getMD5(merchantCode + "&" + userName + "&" + timestamp,
                "el_1W!4z+<x_K1SY+prYi#C~iUCxX$");

        System.out.println("查询用户在线:" + signature + " timestamp:" + timestamp);
    }

*/








/*        String userName = "valar1234";
        String merchantCode = "oubao";
        String timestamp = new Date().getTime() + "";
        String signature = Md5Util.getMD5(userName + "&" + merchantCode + "&" + timestamp,
                "sdfsdfdsfsd");
        System.out.println("注册:" + signature + " 时间戳:" + timestamp);*/

        /*String agentId = "43252435235";
       String agentName = "valar1234";
        String merchantCode = "oubao";
        String timestamp = new Date().getTime() + "";
        String signature = Md5Util.getMD5(merchantCode + "&" + agentId + "&" +agentName+"&"+ timestamp,
                "sdfsdfdsfsd");
        System.out.println("注册:" + signature + " 时间戳:" + timestamp);*/

/*    public static void main(String[] args) {
        String domain = "https://www,baidu.com";


        String merchantCode = "218825";
        String signStr = domain + "&" + merchantCode;

        String signature = Md5Util.getMD5(signStr,
                "el_1W!4z+<x_K1SY+prYi#C~iUCxX$");
        System.out.println("域名验签:" + signature + " 时间戳:");

    }*/

  /*      String userName = "valar";
        String merchantCode = "oubao";

        String timestamp = new Date().getTime() + "";
        String signature = Md5Util.getMD5(userName + "&" + merchantCode + "&" + timestamp,
                "sdfsdfdsfsd");
        System.out.println("查询余额:" + signature + " 时间戳:" + timestamp);*/


/*        String merchantCode = "oubao";

        String timestamp = new Date().getTime() + "";
        String signature = Md5Util.getMD5(merchantCode + "&" + timestamp,
                "sdfsdfdsfsd");
        System.out.println("查询余额:" + signature + " 时间戳:" + timestamp);*/


    /*        try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw

     *//* 读入TXT文件 *//*
            long start = System.currentTimeMillis();
            String pathname = "D:\\download\\mango\\ip_ip138.txt"; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径
            File filename = new File(pathname); // 要读取以上路径的input。txt文件
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename)); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            String line = "";
            line = br.readLine();
            while (line != null) {
                line = br.readLine(); // 一次读入一行数据
                if(StringUtils.isNotEmpty(line)){
                    String[] address = line.split(",");
                    String ipStart = address[2];
                    String ipEnd = address[3];
                    if (address.length >= 5) {
                        String country = address[4];
                        if (address.length >= 7) {
                            String province = address[5];
                            String city = address[6];
                        }
                    }
                }
               // log.info(line);
            }
            reader.close();
            log.info("共花费:" + (System.currentTimeMillis() - start));
            *//* 写入Txt文件 *//*
            // File writename = new File(".\\result\\en\\output.txt"); // 相对路径，如果没有则要建立一个新的output。txt文件
            // writename.createNewFile(); // 创建新文件
            // BufferedWriter out = new BufferedWriter(new FileWriter(writename));
*//*
            out.write("我会写入文件啦\r\n"); // \r\n即为换行
            out.flush(); // 把缓存区内容压入文件
            out.close(); // 最后记得关闭文件
*//*

        } catch (Exception e) {
            log.info("读取异常!", e);
            e.printStackTrace();
        }*/

    // }


    /*        List<String> userList = new ArrayList<>(Arrays.asList(
                    "Meson", "Ray", "OT", "Crystal", "Erin", "Watson", "Jump", "Earth", "Fullank", "Kaiens", "Zaky", "carver", "Enzo",
                    "KB", "Vector", "Toney", "Tell", "Sean", "Samuel", "Dorf", "Miller", "Butr", "David", "Kerr", "Top", "Crazy", "Lithan",
                    "Fymen", "Noah", "Riben", "Raulvii", "Idol", "Antonio", "Kepa", "Paca", "Duwan", "Jesson", "Baylee", "Bevan", "Waldkir", "carson",
                    "zerone", "voot", "Nonhung", "Joken", "valar", "Alic", "Jeffrey", "Yida", "Casino", "Forward", "Black", "Gallen", "abner", "Star",
                    "YK", "Router", "Nice", "Nati", "Cronus", "Amor", "Pasta", "Sword", "Yellow", "Eisha", "Upper", "Zada", "Cable", "Supermark", "Darwin",
                    "Rank", "Downey", "Ledron", "Wushuang", "Echo", "Success", "jinnian", "Aison", "christion", "Arsenal", "Levi", "Mongo", "Owen",
                    "Tower", "thesea", "KJ", "marshall", "pi", "MK", "ninja", "royee", "Jiusi", "grant", "Bonus", "Yati", "Mizuki", "Jani", "Friend",
                    "Glenn", "skrillex", "Asher", "Heider", "benz", "alva", "Veigar", "Steven", "Zet", "aden", "Sivan", "Fashion", "LeBron", "Phoebe",
                    "Wisk", "Colonel", "October", "Dio", "Michael", "beta", "Yunshu", "LaoMike", "Cash", "Eighteen", "math", "Fuka", "Slander", "Orson",
                    "Eunice", "Eight", "Natasha", "Bronny", "Dustin", "Balwin", "Eldo", "Mumu", "Think", "Matter", "Step", "Dony", "Skywin", "Cox", "Acrid",
                    "Rage", "Jerick", "Pure", "Amiao", "Charlotte", "Daren", "Dyson", "Asa", "Newman", "Sabrina", "Ronaldo", "Rezzer", "Dilwyn", "Bank",
                    "Soon", "Aex", "Zeif", "Tobin", "Ferdinand", "BobbyH", "Skylam", "August", "Veegee", "Copy", "Flower", "Alfin", "Choloe", "Rona",
                    "Miky", "Willow", "Vera", "Marven", "Lssac", "Ryanne", "Kylie", "Berlin", "Kivi", "Klopp", "Wendy", "Oscar", "Joe", "Shark", "Mint",
                    "Laien", "Uly", "Flash", "Hope", "Julius", "Leo", "Pele", "Byron", "Wyatt", "Cloudy", "Nail", "Jun", "Mini", "Tea", "Linken", "Legolas",
                    "Oreo", "Abui", "Blaze", "Dabai", "Tree", "Rolin", "Lili", "Lynne", "Junyu", "Bowyer", "Fanta", "Jayway", "Better", "Leehom", "Ramirez",
                    "Bossku", "Mcrae", "Orlando", "Century", "Laster", "Revenge", "Diving", "Dracula", "Spencer", "Dexter", "Nako", "Vow", "Diana", "Churk",
                    "Defy", "Aatrox", "Gaoge", "Goodwin", "Molri", "Juju", "Dachuan", "Idea", "Lola", "Wood", "Serrano", "Comet", "Jaybee", "Happy", "Devil",
                    "William", "Sheldon", "Desu", "Young", "Richie", "Andric", "Xiaoyu", "Xiaojuan", "Niall", "Keep", "JJ", "Rain", "Strawberry", "Hans",
                    "Lightning", "Tark", "Benchan", "Beyond", "Timo", "Tian", "Hanks", "Track", "Drake", "Brody", "Gary", "Moonlight", "Andreaw", "Kamelo",
                    "Ivory", "Maggie", "Charlie", "Paris", "Lana", "Dowei", "Franky", "Zedny", "Bay", "Time", "Nemo", "Sinner", "Sande", "Jay", "Teddy",
                    "Nineteen", "kaka", "Fanny", "Wilson", "Samsung", "Subai", "Sweet", "Brekend", "Joshua", "Gates", "Brook", "Base", "Fukes", "Barret",
                    "Photo", "Sprit", "Kitty", "Grass", "Cartoon", "PQ", "White", "Shng", "Akira", "Norm", "Yiwan", "Khan", "Konrad", "Peanut", "Elliot",
                    "Otto", "Meisiu", "Poem", "AlBie", "Healer", "Ellon", "Ericson", "Moke", "Note", "Most", "AJ", "Future", "Bold", "Timber", "Soccor",
                    "Lable", "Tennis", "Annxu", "Crayton", "Pipa", "Mountain", "Stock", "Polar", "Himo", "Rissan", "Keanu", "Tag", "Bully", "Ball", "Franco",
                    "Strive", "Junkeen", "Casey", "Kimura", "Maze", "Jiawai", "Ozil", "Nono", "Yves", "Lido", "Liew", "Iphone", "Zach", "AD", "Royco", "Autumn",
                    "SV", "Bind", "Kingsley", "Yihan", "Muyi", "Huang", "Wei", "Acai", "Jayden", "Aroha", "Ericlu", "Shayne", "Zoro", "Magee", "Trade", "Dota",
                    "Diego", "Jerui", "Toby", "Red", "Monica", "Verse", "Flee", "Hugh", "beverley"));*/
/*        List<String> userList = new ArrayList<>(Arrays.asList("Dominic Allen Lanuzo", "Van Alen Bondoc", "Paul Nikolo Milo Oropesa", "Manuel Archie Guinto",
                "Jessie  Santiago", "Claudine Bellon", "Katrina Nicole Regala", "Jeffrey Comiso", "Rafael Ortiz", "John Subong", "Bernard Linda", "Sharhan Jakariya",
                "Mark John Manalili", "Jubhoy Nunez", "Rowena Cloma", "Jocel Malacas", "Christian Botin", "Maria Rizalyn Alvarez", "Joerveth Castro", "Clint Alair",
                "Jayson Leysa", "Lucky Elmer Lerios", "Catherine De Mesa", "Perly Palacio", "Marinell Montemayor", "Charles Dref Umali", "Janina Mencero", "Christian Paul Sison",
                "Emaerson Salcedo", "Frederick Faustino", "Mary Grace Vince Cruz", "Marjorie Gadia", "Ronald Roslinda", "Christopher Lizarondo", "Darlene Fontillas",
                "April Joy Lallen", "Reyladd Santos", "Vince Carlo  Bulaong", "Reginald Calma", "Kimberly Tabada", "Christopher Esma, Jr.", "Jessa Guevarra",
                "Charmaine Sangalang", "Ma. Christina Joyce Pastor", "Jessa Mae Salvador", "Michael Anthony Sy", "Jomari  Francisco", "John Andrew Policarpio",
                "Jay-Ar Pepito Aguirre", "Learni Millares", "Mary Grace D. Quanico", "Ericka Palmiano", "Micowin Llena Delos Santos", "Joel Cena", "Jeffrey Mendoza",
                "Regina Mary Cruz Dionisio", "Merry-Ann Questin", "John Renus Jaring", "Anne Margarette Loterte", "Rose marie Garcia", "Lester Gervacio Bautista",
                "Jerry Moldez Molera", "Mark Aljon Caya", "Paul John Mitchell", "Jamycha S. Domanico", "Jomarie  Escalera", "Irian C. Paguiligan",
                "Melvin Coquilla Lucenecio", "Riana Charisse Reveche", "Joshua Bicocho", "Brian  Jalocon", "Merjen B. Guevarra", "Erick Vladimir Abu",
                "Frednie Mark Lavador", "Vincent Serrano", "Annalynn Bacani", "Eric Magabilin", "Robinson Solomon Panes", "John Joseph  Magat", "Angelie  Lopez",
                "Julius Pagyunan", "Kien Alwyn Timola", "Almigel Anguay", "Joemark Ellazar", "Erika Joy Quilapio", "Beatrice Anne Nicole Villaluna",
                "Mary Grace Garces", "Melvin Dulay", "Jurel Arcega", "Weichen", "Cheong", "Jimmy"));*/
    //List<String> userList = new ArrayList<>(Arrays.asList("Tark888","Ivory888"));
    // List<String> userList = new ArrayList<>(Arrays.asList("Maze888"));
/*   public static void main(String[] args) {

       List<String> userList = new ArrayList<>(Arrays.asList("Beta","Lebron888"));
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        for (String userName : userList) {
            try {
                UserPO userPO = new UserPO();
                userPO.setMerchantCode("oubao");
                userName = userName.replaceAll(" ", "");
                userPO.setUsername(userName);
                userPO.setPassword(userName);
                HttpEntity<UserPO> request = new HttpEntity<UserPO>(userPO, headers);
                ResponseEntity<String> response1 = restTemplate.postForEntity("https://neibu.sportxxx1zx.com/yewu6/user/register", request, String.class);
                System.out.println(userName + "返回结果1:" + response1.getBody());
                HttpEntity<Object> formEntity = new HttpEntity<Object>(headers);
                ResponseEntity<String> responseEntity = restTemplate.exchange("https://neibu.sportxxx1zx.com/yewu6/account/changeCredit?userName=" + userName + "&type=1&merchantCode=oubao&credit=1000",
                        HttpMethod.GET, formEntity, String.class);
                System.out.println("返回结果2:" + responseEntity.getBody());
                HttpEntity<Object> formEntity1 = new HttpEntity<Object>(headers);
                ResponseEntity<String> responseEntity2 = restTemplate.exchange("https://neibu.sportxxx1zx.com/yewu6/user/loginPanda?userName=" + userName + "&terminal=pc&merchantCode=oubao",
                        HttpMethod.GET, formEntity1, String.class);
                System.out.println("返回结果3:" + responseEntity2.getBody());
                HttpEntity<Object> formEntity3 = new HttpEntity<Object>(headers);
                ResponseEntity<String> responseEntity3 = restTemplate.exchange("https://neibu.sportxxx1zx.com/yewu6/account/transferPandaCredit?userName=" + userName + "&transferType=1&merchantCode=oubao&amount=1000",
                        HttpMethod.GET, formEntity3, String.class);
                System.out.println("返回结果4:" + responseEntity3.getBody());
            } catch (Exception e) {
                log.info(userName + "失败!", e);
            }
        }

    }*/
}
