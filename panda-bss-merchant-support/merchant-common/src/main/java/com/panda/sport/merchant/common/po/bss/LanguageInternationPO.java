package com.panda.sport.merchant.common.po.bss;

/**
 * 多语言
 *
 * @pdOid d818d08f-933b-495a-a2f2-67c9df55a8f4
 */
public class LanguageInternationPO {
    /**
     * @pdOid 3dcd6091-e5f0-4d8f-bf64-bcf06aa20e98
     */
    private long id;
    /**
     * 文字对应的编码
     *
     * @pdOid a2ec6de2-2313-4804-a6ea-a61ec074e033
     */
    private long nameCode = 0;
    /**
     * 数据来源编码. SR BC等
     *
     * @pdOid 4e9516d1-4f54-4a77-883f-346377c77659
     */
    private String dataSourceCode;
    /**
     * 语言类型. zh jp en 等
     *
     * @pdOid 2213b744-17ca-421c-86c8-766f0ac7031f
     */
    private String languageType;
    /**
     * 文字内容.  name_code 代表文字在 language_type代表语言下的 具体内容.比如:中国 在 英文的表示  是China.
     *
     * @pdOid 730e8dda-fe6f-4c84-a8d9-927709e90c70
     */
    private String text;
    /**
     * 是否激活.  1: 正常; 0: 不被激活.
     *
     * @pdOid a4b6b957-aa54-4af3-b101-e17f8224bb5e
     */
    private long active = 1;
    /**
     * @pdOid cf341888-4072-4e30-bf10-b151f3c836ec
     */
    private String remark;
    /**
     * @pdOid 6b005cc0-7ec6-4b6e-95c3-733677167733
     */
    private long createTime = 0;
    /**
     * @pdOid 69bddf48-247c-4b18-9366-8fb6152474ac
     */
    private long modifyTime = 0;

}