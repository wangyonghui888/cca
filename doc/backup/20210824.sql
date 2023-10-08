SELECT order_no, count(*), max(change_amount), min(uid)
from t_account_change_history
where create_time >= 1629561600000
  and order_no in (select order_no from t_order_detail where match_id = 1053167)
  and biz_type = 4
  and change_amount > 0
  and order_no not in (SELECT order_no
                       FROM `t_account_change_history`
                       WHERE `order_no` in ('1051382437517020',
                                            '1079462440091438',
                                            '1124522445021927',
                                            '1130342438399102',
                                            '1129262432344695',
                                            '1137518373436512',
                                            '1137182431473073',
                                            '1135202443912181',
                                            '1138562435271800',
                                            '1134758377502360',
                                            '1590713691972518',
                                            '1592573695691008',
                                            '1138658377421018',
                                            '1141104787462132',
                                            '1592772476749820',
                                            '1594918065092924',
                                            '1142882449226738',
                                            '1142882449218302',
                                            '1595472478339875',
                                            '1139942441092364',
                                            '1145042433197404',
                                            '1597378069691373',
                                            '1143722460938074',
                                            '1146602435997138',
                                            '1598693690662634',
                                            '1591572479681270',
                                            '1146038403107357',
                                            '1599290255217629',
                                            '1598758061287829',
                                            '1597670250485148',
                                            '1599470254419826',
                                            '1147622433584325',
                                            '1136822433744461',
                                            '1598750262783567',
                                            '1144802439257999',
                                            '1599713690541357',
                                            '1600858069897785',
                                            '1147022440795728',
                                            '1149182437264787',
                                            '1149482438109485',
                                            '1601578064828247',
                                            '1149242442768064',
                                            '1146002434458940',
                                            '1601698062381089',
                                            '1599410248635546',
                                            '1598938065955297',
                                            '1147802435873820',
                                            '1149062453837338',
                                            '1150898374022430',
                                            '1150178377788153',
                                            '1602650253577961',
                                            '1602298066445286',
                                            '1603010247766526',
                                            '1601938066440249',
                                            '1601472484809091',
                                            '1151402433147105',
                                            '1602353713957748',
                                            '1151162435072195',
                                            '1604090249819817',
                                            '1603790261554793',
                                            '1151822439209549',
                                            '1152662434744249',
                                            '1604450248392093',
                                            '1604570248932170',
                                            '1603850256359254',
                                            '1604630248212738',
                                            '1151664782730829',
                                            '1600812477070484',
                                            '1605170254295839',
                                            '1604758070677690',
                                            '1605598065380331',
                                            '1151198373255462',
                                            '1605598065310300',
                                            '1151642440266015',
                                            '1146242459997712',
                                            '1605650251821739',
                                            '1151402433164444',
                                            '1153562438387419',
                                            '1603798072379966',
                                            '1604158061949786',
                                            '1150478374804690',
                                            '1606018057216616',
                                            '1605470250228805',
                                            '1606193696959548',
                                            '1152302433753794',
                                            '1151858374334857',
                                            '1152564785797109',
                                            '1154042433345291',
                                            '1606258059412053',
                                            '1603632480837565',
                                            '1603858061213508',
                                            '1606318059244621',
                                            '1153898380084991',
                                            '1605372485649580',
                                            '1152698377790527',
                                            '1604690247359010',
                                            '1154064784121062',
                                            '1606130252332351',
                                            '1606018057219817',
                                            '1153958377878255',
                                            '1154378379801503',
                                            '1153898380087968',
                                            '1606850248723075',
                                            '1604578062630953',
                                            '1606632475978509',
                                            '1150442434649496',
                                            '1154762435748770',
                                            '1154378379792604',
                                            '1154762435690645',
                                            '1154244792428397',
                                            '1155062443861783',
                                            '1154702432118912',
                                            '1603392483274743',
                                            '1155182440038085',
                                            '1607352488270419',
                                            '1154498379899496',
                                            '1154738374495414',
                                            '1152924788727782',
                                            '1152482433315001',
                                            '1152362436536647',
                                            '1155602443726550',
                                            '1606678064962634',
                                            '1606913711974872',
                                            '1607930253888280',
                                            '1607518061119477',
                                            '1152962434505045',
                                            '1606918071332900',
                                            '1153658380786792',
                                            '1155542439063170',
                                            '1156022432237103',
                                            '1154942439501626',
                                            '1600752485020497',
                                            '1607592504336765',
                                            '1607578059172403',
                                            '1606730249834083',
                                            '1603498059280388',
                                            '1155938372455586',
                                            '1604633691117818',
                                            '1155602443619534',
                                            '1155422439071619',
                                            '1605432484131314',
                                            '1153838388031868',
                                            '1607892475603165',
                                            '1155998372924561',
                                            '1155902431289979',
                                            '1155518376002657',
                                            '1153502433746768',
                                            '1608178060093790',
                                            '1605413693955909',
                                            '1154342436163842',
                                            '1608012485771143',
                                            '1156164779843275',
                                            '1607892475617761',
                                            '1156464785396479',
                                            '1606970260849534',
                                            '1154342436178005',
                                            '1608372485158763',
                                            '1608410255557096',
                                            '1156898377137750',
                                            '1608838084284575',
                                            '1155038379131276',
                                            '1156622441433899',
                                            '1608658059676391',
                                            '1157042433106592',
                                            '1607976334480879',
                                            '1608418062510855',
                                            '1154882441545688',
                                            '1609250251126111',
                                            '1606618060579662',
                                            '1152962434446400',
                                            '1156824780632517',
                                            '1609378067106170',
                                            '1609498061844218',
                                            '1609670252277258',
                                            '1157484786078074',
                                            '1608072476440032',
                                            '1610172481100695',
                                            '1158182436076025',
                                            '1607652478290899',
                                            '1609850254109147',
                                            '1609318062119367',
                                            '1156322435626573',
                                            '1608972479992086',
                                            '1157558371149496',
                                            '1157762445843420',
                                            '1610218062439020',
                                            '1157162433414618',
                                            '1608238061105223',
                                            '1158482434731627',
                                            '1128518371821180',
                                            '1157198384270993',
                                            '1158302435559658',
                                            '1158242433758354',
                                            '1157102431869186',
                                            '1609970246828702',
                                            '1157798369000221',
                                            '1610398063193064',
                                            '1609692478970401',
                                            '1609373689882514',
                                            '1610578058166213',
                                            '1608478062045259',
                                            '1158242433724102',
                                            '1609673690263479',
                                            '1610873689559234',
                                            '1158482434682282',
                                            '1156622441459013',
                                            '1157402435133303',
                                            '1154558379653365',
                                            '1158602440374194',
                                            '1158324782261161',
                                            '1158542435097313',
                                            '1610518060049698',
                                            '1610952480443137',
                                            '1608432480698787',
                                            '1608838084285066',
                                            '1154162432612704',
                                            '1159082447183399',
                                            '1610892479386598',
                                            '1611298067353902',
                                            '1611192480096784',
                                            '1157664783582909',
                                            '1159363356111337',
                                            '1158984804303795',
                                            '1610998070289519',
                                            '1611238065894537',
                                            '1159562434076467',
                                            '1156322435643344',
                                            '1159082447142924',
                                            '1159022436906284',
                                            '1159622433884516',
                                            '1159322440054563',
                                            '1609253690530290',
                                            '1157018377144242',
                                            '1158924787169804',
                                            '1158242433618169',
                                            '1159682432976667',
                                            '1611898058141618',
                                            '1611650251807257',
                                            '1159644783813852',
                                            '1611418064475732',
                                            '1611178065792439',
                                            '1159524781946409',
                                            '1159562434147393',
                                            '1159622433893102',
                                            '1159718369835763',
                                            '1159598373901656',
                                            '1159922452386213',
                                            '1159903355702074',
                                            '1610472477893762',
                                            '1611598059380818',
                                            '1156562440866666',
                                            '1159322440016457',
                                            '1157582434259740',
                                            '1608838084264689',
                                            '1611658065019287',
                                            '1610398063134161',
                                            '1611538061360755',
                                            '1160018379726254',
                                            '1610153718954798',
                                            '1610518060006473',
                                            '1611778060198929',
                                            '1611838061460108',
                                            '1612318063702650',
                                            '1611732479386809',
                                            '1160162433928794',
                                            '1159224782044711',
                                            '1612018066899304',
                                            '1159022436955814',
                                            '1609190255345259',
                                            '1160402441153021',
                                            '1159104783515903',
                                            '1611612487871250',
                                            '1159322439990958',
                                            '1158698375043775',
                                            '1608238061118931',
                                            '1612152477282246',
                                            '1153344787608859',
                                            '1612490258803988',
                                            '1601938066431707',
                                            '1159862440489878',
                                            '1612498062872943',
                                            '1160342439518876',
                                            '1611658065036348',
                                            '1607998060617648',
                                            '1609872479128182',
                                            '1612558065110789',
                                            '1612572482719377',
                                            '1154522443709504',
                                            '1160402441165267',
                                            '1612078066165938',
                                            '1160558378500560',
                                            '1160342439495669',
                                            '1160582435876503',
                                            '1154402437303345',
                                            '1160762433516393',
                                            '1611792485766907',
                                            '1157803360038804',
                                            '1159404786288196',
                                            '1613028610024659',
                                            '1611778059945536',
                                            '1160462440366914',
                                            '1610270250552478',
                                            '1612692479276520',
                                            '1160138378587119',
                                            '1157064789251341',
                                            '1160798371395514',
                                            '1161182435468603',
                                            '1158338372813989',
                                            '1160124782306739',
                                            '1161098375268299',
                                            '1611778060219003',
                                            '1611238065887846',
                                            '1161242435346309',
                                            '1155002433644303',
                                            '1159322439957485',
                                            '1613052487316676',
                                            '1160743362247690',
                                            '1161662458442942',
                                            '1161602433663167',
                                            '1158864789650283',
                                            '1612613691474436',
                                            '1161302435968740',
                                            '1612428607014012',
                                            '1612848607212679',
                                            '1161542451218418',
                                            '1613998060288502',
                                            '1160942440277265',
                                            '1161422437752511',
                                            '1613758064706179',
                                            '1613952480109211',
                                            '1161962441343806',
                                            '1611492475528212',
                                            '1613693693936793',
                                            '1613652476756795',
                                            '1161122436959777',
                                            '1161122437045214',
                                            '1614110286965724',
                                            '1159802465646604',
                                            '1160402441220122',
                                            '1162142432755049',
                                            '1155084782608864',
                                            '1155722434254442',
                                            '1162058372362333',
                                            '1612332481773985',
                                            '1162298375039990',
                                            '1610458071447766',
                                            '1614492486530081',
                                            '1162598372385010',
                                            '1161362442668581',
                                            '1160042433086735',
                                            '1161242435363163',
                                            '1160498371731308',
                                            '1613938058360427',
                                            '1160162434004106',
                                            '1614598065403768',
                                            '1162718372019113',
                                            '1162478372213285',
                                            '1160762433492521',
                                            '1162862435804252',
                                            '1162262443162866',
                                            '1163042440988217',
                                            '1162838371059650',
                                            '1160604781514471')
                       group by order_no
                       having count(*) = 4)
group by order_no
HAVING COUNT(*) > 1


