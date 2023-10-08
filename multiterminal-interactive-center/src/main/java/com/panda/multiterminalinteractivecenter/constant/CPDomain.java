package com.panda.multiterminalinteractivecenter.constant;

import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CPDomain {

    public static Map<String,List<String>> bwDomains = new HashMap<>();

    public static Map<String,List<String>> topcpDomains = new HashMap<>();

    public static Map<String,List<String>> bwUseDomains = new HashMap<>();

    public static Map<String,List<String>> topcpUseDomains = new HashMap<>();

    private static List<String> bwVip1 = Lists.newArrayList();
    private static List<String> bwVip2 = Lists.newArrayList();
    private static List<String> bwVip3 = Lists.newArrayList();
    private static List<String> bwVip4 = Lists.newArrayList();

    private static List<String> bwBj1 = Lists.newArrayList();
    private static List<String> bwBj2 = Lists.newArrayList();
    private static List<String> bwBj3 = Lists.newArrayList();
    private static List<String> bwBj4 = Lists.newArrayList();

    private static List<String> bwDef1 = Lists.newArrayList();
    private static List<String> bwDef2 = Lists.newArrayList();
    private static List<String> bwDef3 = Lists.newArrayList();
    private static List<String> bwDef4 = Lists.newArrayList();

    private static List<String> topcpVip1 = Lists.newArrayList();
    private static List<String> topcpVip2 = Lists.newArrayList();
    private static List<String> topcpVip3 = Lists.newArrayList();
    private static List<String> topcpVip4 = Lists.newArrayList();

    private static List<String> topcpBj1 = Lists.newArrayList();
    private static List<String> topcpBj2 = Lists.newArrayList();
    private static List<String> topcpBj3 = Lists.newArrayList();
    private static List<String> topcpBj4 = Lists.newArrayList();

    private static List<String> topcpDef1 = Lists.newArrayList();
    private static List<String> topcpDef2 = Lists.newArrayList();
    private static List<String> topcpDef3 = Lists.newArrayList();
    private static List<String> topcpDef4 = Lists.newArrayList();


    public static void init(){
        // ===================================bw===================================
        //vip
        bwVip1.add("https://cuatob-alipc1.3ndfp3ai.com");
        bwVip1.add("https://vip1-cuatob-alipc1.3ndfp3ai.com");
        bwVip1.add("https://vip2-cuatob-alipc1.3ndfp3ai.com");
        bwVip1.add("https://vip3-cuatob-alipc1.3ndfp3ai.com");

        bwVip2.add("https://cuatob-alih1.3ndfp3ai.com");
        bwVip2.add("https://vip1-cuatob-alih1.3ndfp3ai.com");
        bwVip2.add("https://vip2-cuatob-alih1.3ndfp3ai.com");
        bwVip2.add("https://vip3-cuatob-alih1.3ndfp3ai.com");

        bwVip3.add("https://obaliuatapi.x9li4.com");
        bwVip3.add("https://vip1-obaliuatapi.x9li4.com");
        bwVip3.add("https://vip2-obaliuatapi.x9li4.com");
        bwVip3.add("https://vip3-obaliuatapi.x9li4.com");

        bwVip4.add("https://cuattxstat1.6zrome5.com");
        bwVip4.add("https://vip1-cuattxstat1.6zrome5.com");
        bwVip4.add("https://vip2-cuattxstat1.6zrome5.com");
        bwVip4.add("https://vip3-cuattxstat1.6zrome5.com");


        // 北京
        bwBj1.add("https://cuatob-hwpc2.y84ryd8.com");
        bwBj1.add("https://bj1-cuatob-hwpc2.y84ryd8.com");
        bwBj1.add("https://bj2-cuatob-hwpc2.y84ryd8.com");
        bwBj1.add("https://bj3-cuatob-hwpc2.y84ryd8.com");

        bwBj2.add("https://cuatob-hwh2.y84ryd8.com");
        bwBj2.add("https://bj1-cuatob-hwh2.y84ryd8.com");
        bwBj2.add("https://bj2-cuatob-alih1.3ndfp3ai.com");
        bwBj2.add("https://bj3-cuatob-alih1.3ndfp3ai.com");

        bwBj3.add("https://obaliuatapi.x9li4.com");
        bwBj3.add("https://bj1-obaliuatapi.x9li4.com");
        bwBj3.add("https://bj2-obaliuatapi.x9li4.com");
        bwBj3.add("https://bj3-obaliuatapi.x9li4.com");

        bwBj4.add("https://cuatalistat1.22wolhm.com");
        bwBj4.add("https://bj1-cuattxstat1.6zrome5.com");
        bwBj4.add("https://bj2-cuattxstat1.6zrome5.com");
        bwBj4.add("https://bj3-cuattxstat1.6zrome5.com");

        // 默认
        bwDef1.add("https://ty1-cuatob-hwpc2.y84ryd8.com");
        bwDef1.add("https://ty2-cuatob-hwpc2.y84ryd8.com");
        bwDef1.add("https://ty3-cuatob-hwpc2.y84ryd8.com");

        bwDef2.add("https://ty1-cuatob-hwh2.y84ryd8.com");
        bwDef2.add("https://ty2-cuatob-hwh2.y84ryd8.com");
        bwDef2.add("https://ty3-cuatob-hwh2.y84ryd8.com");

        bwDef3.add("https://ty1-obaliuatapi.x9li4.com");
        bwDef3.add("https://ty2-obaliuatapi.x9li4.com");
        bwDef3.add("https://ty3-obaliuatapi.x9li4.com");

        bwDef4.add("https://cuathwstat.7qajtq2w.com");
        bwDef4.add("https://ty1-cuathwstat.7qajtq2w.com");
        bwDef4.add("https://ty2-cuathwstat.7qajtq2w.com");
        bwDef4.add("https://ty3-cuathwstat.7qajtq2w.com");

        // ===================================topcp===================================

        topcpVip1.add("https://cuatbob-txpc1.zeiurexx.com");
        topcpVip1.add("https://vip1-cuatbob-txpc1.zeiurexx.com");
        topcpVip1.add("https://vip2-cuatbob-txpc1.zeiurexx.com");
        topcpVip1.add("https://vip3-cuatbob-txpc1.zeiurexx.com");


        topcpVip2.add("https://cuatbob-txh1.zeiurexx.com");
        topcpVip2.add("https://vip1-cuatbob-txh1.zeiurexx.com");
        topcpVip2.add("https://vip2-cuatbob-txh1.zeiurexx.com");
        topcpVip2.add("https://vip3-cuatbob-txh1.zeiurexx.com");

        topcpVip3.add("https://bobtxuatapi.tpc4zo.com");
        topcpVip3.add("https://vip1-bobtxuatapi.tpc4zo.com");
        topcpVip3.add("https://vip2-bobtxuatapi.tpc4zo.com");
        topcpVip3.add("https://vip3-bobtxuatapi.tpc4zo.com");

        topcpVip4.add("https://cuattxstat1.6zrome5.com");
        topcpVip4.add("https://vip1-cuattxstat1.6zrome5.com");
        topcpVip4.add("https://vip2-cuattxstat1.6zrome5.com");
        topcpVip4.add("https://vip3-cuattxstat1.6zrome5.com");

        // 北京
        topcpBj1.add("https://cuatbob-hwpc2.mej9h44n.com");
        topcpBj1.add("https://bj1-cuatbob-hwpc2.mej9h44n.com");
        topcpBj1.add("https://bj2-cuatbob-hwpc2.mej9h44n.com");
        topcpBj1.add("https://bj3-cuatbob-hwpc2.mej9h44n.com");

        topcpBj2.add("https://cuatbob-hwh2.mej9h44n.com");
        topcpBj2.add("https://bj1-cuatbob-hwh2.mej9h44n.com");
        topcpBj2.add("https://bj2-cuatbob-hwh2.mej9h44n.com");
        topcpBj2.add("https://bj3-cuatbob-hwh2.mej9h44n.com");

        topcpBj3.add("https://bobhwuatapi.x5xd5.com");
        topcpBj3.add("https://bj1-bobhwuatapi.x5xd5.com");
        topcpBj3.add("https://bj2-bobhwuatapi.x5xd5.com");
        topcpBj3.add("https://bj3-bobhwuatapi.x5xd5.com");

        topcpBj4.add("https://cuatalistat1.22wolhm.com");
        topcpBj4.add("https://bj1-cuatalistat1.22wolhm.com");
        topcpBj4.add("https://bj2-cuatalistat1.22wolhm.com");
        topcpBj4.add("https://bj3-cuatalistat1.22wolhm.com");

        // 默认
        topcpDef1.add("https://ty1-cuatbob-hwpc2.mej9h44n.com");
        topcpDef1.add("https://ty2-cuatbob-hwpc2.mej9h44n.com");
        topcpDef1.add("https://ty3-cuatbob-hwpc2.mej9h44n.com");

        topcpDef2.add("https://ty1-cuatbob-hwh2.mej9h44n.com");
        topcpDef2.add("https://ty2-cuatbob-hwh2.mej9h44n.com");
        topcpDef2.add("https://ty3-cuatbob-hwh2.mej9h44n.com");

        topcpDef3.add("https://ty1-bobhwuatapi.x5xd5.com");
        topcpDef3.add("https://ty2-bobhwuatapi.x5xd5.com");
        topcpDef3.add("https://ty3-bobhwuatapi.x5xd5.com");

        topcpDef4.add("https://cuathwstat.7qajtq2w.com");
        topcpDef4.add("https://ty1-cuathwstat.7qajtq2w.com");
        topcpDef4.add("https://ty2-cuathwstat.7qajtq2w.com");
        topcpDef4.add("https://ty3-cuathwstat.7qajtq2w.com");

        bwDomains.put("vip1",bwVip1);
        bwDomains.put("vip2",bwVip2);
        bwDomains.put("vip3",bwVip3);
        bwDomains.put("vip4",bwVip4);

        bwDomains.put("bj1",bwBj1);
        bwDomains.put("bj2",bwBj2);
        bwDomains.put("bj3",bwBj3);
        bwDomains.put("bj4",bwBj4);

        bwDomains.put("def1",bwDef1);
        bwDomains.put("def2",bwDef2);
        bwDomains.put("def3",bwDef3);
        bwDomains.put("def4",bwDef4);

        topcpDomains.put("vip1",topcpVip1);
        topcpDomains.put("vip2",topcpVip2);
        topcpDomains.put("vip3",topcpVip3);
        topcpDomains.put("vip4",topcpVip4);

        topcpDomains.put("bj1",topcpBj1);
        topcpDomains.put("bj2",topcpBj2);
        topcpDomains.put("bj3",topcpBj3);
        topcpDomains.put("bj4",topcpBj4);

        topcpDomains.put("def1",topcpDef1);
        topcpDomains.put("def2",topcpDef2);
        topcpDomains.put("def3",topcpDef3);
        topcpDomains.put("def4",topcpDef4);
    }

    public static List<String> getOtherDomain(int i) {
        return null;
    }
}
