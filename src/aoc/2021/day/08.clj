;; [Day 8](https://adventofcode.com/2021/day/8) was a unique puzzle, and I rose
;; to its occasion by writing some code unfit for human eyes. Read at your peril!

(ns aoc.2021.day.08
  (:require [clojure.string :as str]
            [clojure.math.combinatorics :as combo]
            [hyperfiddle.rcf :as rcf]
            ; Hint to the horrors to come
            [clojure.core.logic.pldb :as pldb]
            [clojure.core.logic :as logic]
            [clojure.core.logic.fd :as fd]))

(rcf/enable!)

;; The task is not easy to explain -- I probably can't do a better job than the
;; [puzzle itself](https://adventofcode.com/2021/day/8), but I'll attempt a summary.

;; Essentially we have an old-school alarm clock with a 4-digit display where each digit
;; is comprised of 7 line segments; when all segments are lit, we have our digital 8:

; Digital 8:               Named segments:
;   ----                     aaaa
;  |    |                   b    c
;  |    |                   b    c
;   ----                     dddd
;  |    |                   e    f
;  |    |                   e    f
;   ----                     gggg

;; The problem is that our alarm clock's wires are crossed; e.g. when the clock wants to
;; display the time 11:11 by illuminating the two right-vertical segments (c and f) of each
;; digit, it might illuminate instead the two bottom-most horizontal segments (d and g).

;; And it's worse than that: we actually have ~200 alarm clocks, each broken in a different
;; way. The first clock may display its ones using segments c and g; the second clock may use
;; a and b; etc.

;; So, the task is to decode out broken alarm clocks. Each line of input corresponds to one
;; of the clocks. A line is comprised of two sections, separated by `|`. The second section
;; gives the four digits shown by the clock, by listing the segments illuminated for each digit,
;; e.g.:
; dfcb gedaf dcfb bcfdea

;; The first section gives the 10 distinct garbled-digits shown by the clock:
; gbcead cfgaeb beadf adb egafd fbeac dbfegca fdaceb dbfc bd

;; Let's get the parsing out of the way:

(def input "gbcead cfgaeb beadf adb egafd fbeac dbfegca fdaceb dbfc bd | dfcb gedaf dcfb bcfdea\ndgfcb gcdeafb eg egb fcdebg cegf becad dfbcag fbgdea gecdb | dbfcge dacbe bge fgdbca\nbgdacf ga egab bfeacdg bfaec agc efbcda agfec cfedg abfceg | bega cga febac aebg\ncbgfad ed ecdgfb dge deabg fgdab geacb gabfced dfae dbgfea | afed dfea de eabgc\nfdaegb fae acedgfb dgbae fedcb daefb gebadc af bcfgae gafd | egadb afdbe eaf af\ncfg cegbdf fecgb fg cdageb fbgcad egfd gdcbe eafcb abdgcfe | gdbace fbgce gecfb bdfcge\nbgcfed aebgdcf gcbd edbcf ecdfag ebc efcdg bc befacg daefb | dfabe edbfc cdebf gbcfea\ndbfge cfebd dega facbge adbgcfe bfage fgdeba gfdbac gd dgb | bgd eabcfg bcdfega egafdcb\nae egca gdeabf gcfba fcdbag dafcbge dbcfe efa ebgcaf fbcae | cdebgfa gecdfba dabcgef bfceag\ndagcfb fdegacb cbgad bedcg cbgeda cfbeg aefgdc abde ed gde | egdbfca gcbdae gde gecdaf\nfcdaeb gecaf eabcdfg efcbgd bge cadeb gbad becga cbeadg bg | ebg beg edabfc cbeda\ned cdbfaeg ecadgf bcfde agcdfb cedbfg gedb cgdbf def acfeb | abfcdge ed bdge ed\ngfdac afg ecgfba af ecadbg bfda adcgb dbcfaeg bdcagf cgfed | gabcef bfad gbecda degbca\nfaedg ba cbdgf ceadgf gdbfea adbgf aecdgb efab agcdebf dba | fedagc bgdfc ab afbdg\neadfb ecbdg ebgadc bfc gcef egbcfd fbcdega bcefd gcbdaf cf | bagdecf bgceda beadcg fecg\nbegdac eabdc cabef de facdbg acegdfb adcgb edcg dbegfa ead | gabfdc gbcfda daegfb ed\ndfgcb ef fce gcdef bcgafe gdaefcb dcgebf dfbe agcde gdafbc | ceagbf fbgdc cfged gecfd\ngefcd fcdeba bf fbgedac egadbc aedcb fcb bdecf fbeagc afbd | fbc fgabdec ebcdf gcdef\ncagdeb bed gcbfe fdae cgdfbea dgfeb eafdgb ed agfbd cgfbda | dgbef gbfdeac fgbdace ed\nbdgea eabcdf efbgc edfgcba fed df ecgdbf bedgf egacbf fdgc | efd fdbaec befcg ebcgfa\nced edgaf gacebd ce fdabc fgcdab cdaef cefgbda fcaebd ebfc | ec fdbcae fedag aecfd\nfcgdb cfbdge agdefb af abgcfed decga cdbfag fcab dfgac dfa | agefdb adgcfb adceg cafb\ncfag fgbedac gedfc cfead cgdafe gef fdebac ebdgc fg agefbd | fg egf egdcb gcaf\nbaecdg gec gefdab gcfead agcb gc acdgfbe edgbc dbcef gbade | cagb acefgd gc cabg\ncgefb bgdface ecgbfd gcebaf bface egbfad ae fea gcea bcdaf | dbcfa fea aedbcfg ae\ndfgabc cdfgeb ce gefcba fcbde degc cfbdg bce feadb defgbac | fdcbe bdfgce cfbdg abefdgc\ndageb dcbfga dg eadcgfb edcg bgcead eafbd gad fceabg egcba | bgaecf acdgbe dg dag\nedfacbg bgcadf adebgf gbdca fb becagd bfgdc cefgd dbf bafc | fb agebfd dfacgeb cbgafd\nbedgafc cbd cefdgb ebca dbeafg dfbea bdafc cb dgfac dbaecf | bfadc adfeb fcabegd fcbda\ngb ebgfdac fbag fbaecd fbdage ecfdg dceabg bdg gdbef afdbe | ecfabd bg gdb bgaf\nebdfcag dbfgae abcf bdegfc bgfdc abd edgca bdacg ba dabcfg | bagcefd gafcdb caefbgd dcefgab\ncgfd fcbae geafd egc cg bgdcea fedbag acedfg fgeca dabegcf | caegf fcgd faegbd ecdgba\ndacefgb gdcab cbeg ge dabecg afcde daecg dfeabg fbdcga gae | egdabc deabcg ebfcdga aeg\nbgaced ebdg cgd abegfc gdfbcae bgace dg cfeda cgdea adfcgb | cfbage bgde dg aecgb\ngfedabc bc gebad facgd ebcg edcgba dfabeg cdb dbagc abdcef | gaedb abdfce gbec gecb\necfgd adgbcfe adgcbe bdfegc gefa dbacf cfgda ga acg fadceg | ag ga adbfc faeg\nced edbgf afebgcd dfegbc bgfead cfge ce gbcde abedfc cbagd | cdbag ecgf edc dce\naefcgb fbdcage bdfe cefdgb bcgde ef fgcad efdgc efc edacgb | dfacg cgabfe ecf dfcga\naed cgbaedf eacdg dfgac dfaebc gbde bcaegd aecgb ed fagbce | ed feadbc dbeafc de\ndecbaf bdeafg acbf gceda bcdgfea af afdce fgbecd fad cfbde | af bagfed eadbfc fdcae\nebacgd fagdb dgcefa ebg fedgcba adbeg eb abce fgdbec cdage | gcdeba ecab bcae ebfdgc\ncfdb daf gacfe dfagbc ecfbagd dcbag fd adecbg fagdc dafgbe | cdfb dfa ceafg fgabed\nbgfedc beadc gecdb dcegafb gb edgacf begcaf fdgb gefcd geb | ecbadgf ebcafg fgedc ebg\nbde afcegd beadgc fcaebd edgbfac aefcd dfabe bfce gafbd be | eafdbc bcef efgdac gfabd\nbdfgae ebfad fbc bdefc afec dbagcf cgefbda acefbd fc cgbde | cebfd fgbaed gfcadb bfdagce\nabgf dabfec ebacg gdeac ebg bface egcabfd fgcbde bg eagfcb | agecd gcaebf beg fabcde\nadecb aedbgc geabc gcabef fabed afbcdg cd dbc dgabfec ecgd | beacfg dcge dceg cgde\nagcdfe egabcd cfgaedb bcgea fcgdeb eac deba agcfb ae cgbde | bfcegd fdacbeg bedcfg cfedag\nfdebagc gdefb dfabg gcabdf ba acegdf acedgb adfcg bad abcf | fgdac fgadc bdgaec abfc\ncbfd bc fdgeb eagcd becgd bce gbfecd dgeabf befgac ecgbdaf | bfgced cedgbf cb cegfbd\nfedbg ebdcf aefgd fcgeda bg ebagcf bge bdefag dbga afegcdb | bfecd gdab gbad debcf\ngcfeab bd dbe ecgabdf edfba gbacde bdfg gbaefd bgaef edfac | cefad db aecdf bfcage\nfcedba edc dc fcda gefcab ebcaf efgbdca cbeda dfbgec gebda | ced dbgfec gdebcf aebcf\nfgce eacdbf cbdfga gbfae deacfgb eagbd gaf feabc fbceag fg | afcgbd cfegba bagef fdcbea\ndfabge dge cbdfea cbgd gd fdceb gfdebac agcef edgfc bfcdeg | fcdbge dg cdefg fcadbe\ngbeca fbg cdfeb dbaceg fdacbg fgae cfegb aegcfb gf dfgbeac | abegfc fecdb efag gbeafcd\ngacfeb cd cdbaegf aebcg dgebf bdcafe adgc bdcge aebgcd bcd | fbceda edcbg edbacg befdca\ndgaeb afegdcb adfgce cabde gb gfaed gebf gbd dbagfe fbgcad | gbd gbdefa cgfadeb becda\nefg gdfa ecadf eagfc cgaeb febdcg gf edcfga befadc gebfcda | gdfa ebdacf abgce fg\ndbg cbadf gefbc egfadb bgcfd efdbgac gd ebfcgd bcgefa ecdg | dbfcg dgb fcgbd dbg\ndbfecg facdeb fbadc gf gbdfa gfb ebdag dbafcg egdfbac gcaf | dcfbag gfca gbf fgcdbe\ncefgab fgdb dge aegbf cbgaed gd bfdgcae dfega abfdeg deafc | feacd bdgf gbdfae eafcd\ndcae ea facbged efa fagbc cefab aefbdc afgebd edbcfg edfcb | fbgdce cbfga efgbcda eadc\ngeafbcd fecbag fba dgbfc dbface bceadg cbdfa fa bdace eadf | af dabcfe cafdb cbaed\nefbgdc dga gcbfd agfb gfcdab dbgfeca fegdca abedc ag badgc | cbdgf febacdg cfdbg gedbfca\nedagfb gcda ebfadgc dgcbef bgc gc gbcadf gabfc agbdf abecf | ebgdcf cdga bafdg afcbg\ndaebg cfaegdb fgabe agedcb bda bd fcdabg aedgc eadcfg cebd | cdeb ecdga db geabf\ndgef fcbged ef bfcage dbfcg cefdb caefbgd daceb fbe gfbadc | gdef dcgbf daefcgb dbgcf\nga bcfeagd afbg fgead fegabd becfdg age edbgf bgdeca dface | eag ga bgfa gfdeab\nadge fbgcead dabcf de cdbfeg fbega fcaebg bed fabde faegbd | abfed gdea fbdae ebd\ngae ae gcefbd bcaefgd edbgc gdfab debag dfecga gbdcea abce | egdbca afgdb ae cdeagb\ncabgef ed fbadg ecbd edcbfa cfdaeg afedb ade ecdgabf afbec | fcgbeda cebd dabfg cafebg\nfdbcae ceadf dcgbfe cad ebdcfag ecfbd eafcg deba ad gcfabd | fecag daeb cad ebcdf\ncf egbfc cfbgeda gfc gbdce begafd facbge afbeg fcea gcdafb | fc fc afce cf\nabdecg deg gcbd efgbda gacdfeb ecagf badce dg dafbce dgcea | dcaeb abcfed efacg gd\ncbgefa dcbage abecgdf cdga fgebcd ca cbdeg eac dbcae eafdb | ace dgecb befcga cea\ngef fg dgaebf bfeac cgfea ecfbgad bgfaec acefbd cgdae bfcg | fdaegcb dceag edbgaf fadebc\ngbafde adefbcg fbaeg ged dg abgd dfbge gfedca caegfb cdebf | dgab baegf agfced bgfae\neb caefdg dacfe baed dgfbce efbdac efcdbag baecf ebc gcbaf | cbe fcbade adcef ecb\ndb deacg fabge bcagde bedc bfdagc adgbe cdeafgb bda cfgead | agdec becd abd bd\nagcdb fcba efdacg gca bgdec fagdb bafgcd beafdg ca caefbdg | dgaebcf acdfeg cedagf fdcgba\nbacdgfe ge dgeca ecdba bdcage cebafd efabdg egbc dfgac egd | egd gbce abecd afbdecg\nbdfa aegcfb gfa daefg fa gaedc dcgebf bacefgd abdefg gebfd | fadb fga dgeabf bafgec\ndfecb adcefgb dcbea bagc dfbeag bea ab degcba efgacd dcaeg | bae cfdgae daceg bfced\nfbcde fdgb deg cdfega dg cageb gbedc afdebcg bcdfeg cebdfa | bfdec dbecf abecfd cegbd\nfagdebc eafgcd gbaefc def abcde fdcgeb afgec fadg df dafce | fdagec deafgc df adceb\nbcfaegd begfda bdaef efbgca cba bdec fgcda dfebac bdcfa bc | bafdc fabdceg cebd eafcbd\nacbeg fgdbca ecdbgf abg eadgc ba adgbfce bcefga cgfeb afbe | fbgdac fadcgb cdfgba gfcbe\nfbdceg af ebadcf aedbf fbcega dfca fea fadcbeg gaedb dbcfe | abgfce dacf fa bfegacd\nged abegfd aegbcd gaec fecbd decbg fgcdba egbdfca dgcab eg | eg gfedba efdcb gebafd\ngfb begdaf cbadf fcgedab fgec fbcedg gebdca fg gcbdf degbc | gcbdf fg bgdfc fgb\ndcgaef gca cgdabf ac gcdab fabc gfbad adefbg bgcde dbeafcg | cgbed dafegb ac ac\nfgeacb bc bfdge bcg dfgcb dacegf badcgf cadb fcbaged gafcd | fcgad cb afgcd bgdcf\nfadecg fb fcb gefb dgfcab adefcgb ebacfg cabfe becad gcafe | fcb cgefba caedgbf gdacfe\nedcab fgce dgfcbea aebfc abgfdc efa fe bcagfe dbgfae abfcg | baedc facgb acgfb ceafb\nbae aefbg dfbag afed bdcgfa fegcb afegbd agcdfbe ae bdcega | fegbc febag acgdbe fgdba\ngea dagebc efadg adcfg cfgdba edacgfb caef ea gdbef geafdc | ea gfade feac fcae\ngadc acfdb ag bfdge fbgad fbdcage abcfed agf bcdgfa cafbge | cdag cagd dbcaf gfedb\ngacedb abfced fdcbe gdfbce ea fcdga eacfd dbcafge afbe aed | edcfbg faced becadg cagdf\ndbcaf bdcfe fdgcab ca agfbd acgbed abgfedc acfg gafdeb bac | dbgecaf bfcda fgac bgdcaf\ngbaef dbagef fab fagd bedgf bdgacfe gfdebc fcdbea af acbeg | gafd cdbgaef eacdbgf fba\ngecd beacg bec badfce gbeacd bgcad gedfcab ce dbacgf fegab | ce acbgd ecabdf afbced\ncfdbae bdfcage ecdfb gfebca cagbd ge cfegdb egcdb gedf geb | ge gbaecf bfcdage cbgedaf\nfag cagefbd agce bcdfge ag fabed cgdef degfac geafd gafdbc | edfcg fcdge cegdbf gfaecdb\ngdf bacgde eabdfcg ebadg fd caegf fecdgb adfb fgade bagefd | adbfeg ceagdb baefgdc gfead\ncadgf cfabdg bfgaedc gbade cgdeaf cgef acdefb efd gaefd fe | def beagcfd deabg gecf\nag aeg bfegacd badec badg gdecaf afbced gcaeb bfegc cbeadg | baced gabd becad egcfad\nbgad eabcdg acedfb gdfecb ab eba bcgeafd bcega edgbc gefca | egcaf gbedc ba ecbgad\ngbacf cegfab fdbcea gaec fgc fdabg cdfbgae cg egbdfc cefab | cfg fcbgdea fabdec fbceag\ndebfg bdefga db gebfa gedcf bcafde edb cfaebg gbda cdbeagf | edafgb aegbdf egdcf bdga\nfabdegc eacdb baceg afdceb bdefc gfbead dab fgecdb cfda da | cbage da ecfdb dceafb\ngcfdba gacdb gabdf efbcdg cafd cbdafge dc aefbgd cdb agecb | cdegbf acbdg bcdag facd\nebd bfacge aegfb de edfa fdgeb aefdgbc abfged gdfbc dgbeca | fbgcd ed ed de\ncdaef acfegd bfce eabfd fgdabec egcbad be gdfba bde eafdbc | be bed eb eb\nde bedf bcgaed fedgbca cabgef ead cadebf cbfea fcead dfacg | geabfc eda eacbdg bgecfa\ndfaec egfcd egc cg egfcad ebcgaf dcfbae gbfde fcebagd acdg | ceg dacfgbe begfd dbcafe\ncdbg gacfd gb ebfad agefcb efacdg cfdeagb gdfba gbdacf bga | bdfga gdcb bfgace dbcafg\nabfdeg fadec dagcbf gecadbf egabfc abefg befda edgb bdf db | dbeg db bdge dbfecag\nebdafc gbdcf geabcfd caedf gdecf fage gce bgeadc ge egfdca | egc gec gfdec adbfce\nafdc fgdaeb dba ad acebf ecagbf daegbcf edacb eadfbc bedgc | cgeafb dgceb dba edbgaf\nebd be cafgde gfcbed gdaef gbcad bdfaeg efab efbgdca ebgda | edcfga dafeg cefbagd gfdae\ncdefa fdceag gabefc afdb caebd ba begcd ebdcgaf dcebfa acb | cadfbe dacbe bca fabd\nedga fcbade fea egfab gebfc ae dfcagb dgbaf adgfbec dfbgea | bdfagc fbceagd fae gbcfaed\nebgaf fgadc becfdg caeb dbfage cb gbc gadfbce bgacf gbacef | abdfeg bc begcdf ceba\ndfgac feda ecabfgd eacfg fbdgc gad aefgdc cegbaf ad egdcba | febdgca aefd fdcga cfdag\ndaecb gdcabe bgdcfae decbfa efdcg cdbge dabg bg bcg bgfcea | ebcgd aecbfd debcfa bfeagc\ngf dbfcg acbdf fdbeacg dgebac gedf aefcgb cfg fgcdeb cgbed | dgfe efdg defg bedgc\nafgbdce fgcbe dgbe bcfdea gbcfd dbc febcga gecbdf dgcfa db | gacebf ebdg bcd dgeb\nfcadge geadfbc db gdcae gebcd bdg gafbde gcbfe dbgcae abdc | abegdc gfecb begafdc bd\nfc dacebf befcd fbegdca feca dafeb fedbag dbcge fadbgc fdc | ecfdb cfd acef edagbf\naeg gfebac dbcfea fgde gadcb ge fecdag egcad cfead bgedfca | fdgcae edfg dafec dfcae\ngedca fcabdg fcabeg dgfbae eb becf fcbga eacgb bae faecgbd | fgbac fdcbgea aeb fgacb\nfbcegd badfg fdeab agdefc abecgfd dgfbc adg ga bagc gbfcad | ga abgc efacbgd agfbd\nacfebdg gbc gc cgbfae dgbea adgbc bdafc dgbeca bfgade gdce | dabfecg dagbe ebgad egcd\ncgfe gdbfae bfaecd adgcbfe bgdca fg deacf gcfad fcgead dfg | faebdg fdcaeb fg gfce\ncdebgf gb gfbe dgcbf bagced fcdge bdg bdfac fdcega dbagcfe | fgedc gdcef bcdegf cdfgae\nfgcd cfa edcfab dafcge efgab gbceda daebcfg agfce cf adecg | gafce fcgaed fac afc\ncedgba ac cabf egadf fbegcd aebfcd ecdbf dca cgadfbe dcefa | ac bgfced fecbd cbfadge\ndagb ebcgd ad bcegdfa dbcea edgfac dgceab cgbfed bcfea acd | bdfgce bcaef da bgedfc\nfgdea fgcade ebcagf cg eagbfd gce ecfgd cdebagf dcga cdebf | gc gc bfadge adcg\nfbe debg fgced fcbdae gcfab dcgfae egcbf cgabedf cdefbg eb | ebf debacf be cafbg\nfcegba cebadf efgd gaefc adfceg ade fcegabd aedgc dgabc de | aegbfc faegdbc egfd dgfe\ndf bfcdag dacbefg fagdbe begaf ceadb adf afbde gdef ecbfga | geafb faebcg ebadf df\necfdab dcfba debfa ecfd dbacegf bcf fgcbae cbdga agedbf cf | dcbag eagbdfc edbgfa cafdb\nfed bgdfac edbafc egfab gdcfb de fgedbac gced bgedf bdcefg | defcgb gbfdec cedg ed\ngbfcdae dcab ba bgfedc ecbafd agfedb fcebd abfce cfega baf | bfa gebcdaf bfedag ebfgdc\nafdceg eacfg cbgfaed gc dceg fgdcab egfda gbeadf abcfe fgc | gdcaef gfc fdgaec gc\nbfdaegc bcaefd edfgcb edfgc fdgac faeg af cfa afgdce bagcd | geadcf fdabec acf agdcb\nebfdc dbag eabcdfg gcd gfbaec agcfbd dagfec abfcg gd cgbfd | fgabc bgad adgb fbdgc\nefgdcb dacbg afcbe cfd fd gadceb agfd acdfb ebagfcd dgcbaf | cebgdf gdaf bfcdeg afbdcg\ndefgb bfce dgeab ef dcbgf gcefad bfecgd dfgabc eacdbfg def | bfdgc fgcbeda ecafgd fdcbge\nedg bfdgeac bagce gbdae egadfb ed dfbe cbgfad dgfab gfcade | ed dcgaef acbeg ged\nabedc gafbed bgaecd beadfgc bf dfb gdcfa abfcd ecfabd ebcf | dcafg fceb dbacfe fbdgace\neagfc acebg abedfgc feadbg adgef dfca edgacf fcg cf dfgebc | fcdgeb fgc cdbfge gdcbafe\nbcde dcafgbe ecf debfa ec bgfca bcfae bcdfae gbfdea caegdf | cef edfab cfbdae cfgab\nfdecag cd cbfage aedbcg gbcaedf cabdg dgc bagec bgfad dbce | fcbgae gbafd cgd bcfgae\nfeba agfdceb gbfedc gbfca ab bca cadgf gbcfe debagc cgbaef | cadgf cfaegb gbacf dcabeg\nadceb cdgbaef cefdab ec edc dafbeg ecfbgd gdacb feca aedfb | bdfegac eafc badfe ec\nbacdfg fcbeag afdc fabegcd acgbf agbdf dag ebdfg ad edbgca | gedbf bgdcaf fdac dcfa\naefg dgecb eafgdc gefdc afcdbe befcdga cdfag cef gbcadf fe | ebadfc efcbda efgcda bfgadce\ndecf fbgdca ecbgfd gbdae df bcfeg dgbef dbf becgfad acgebf | fdb fbgec dbgfac fcgdbea\ngcaefd decab cbfea egfdbca dafb efgcb bcdega ebacfd afc fa | ecbfad caf dbeca caf\nabfdgc gebaf cb bca fdabceg becf ecadg bacge afedgb agcefb | daceg dgfeba bc cgdea\ncefgdb edfagc gc fdgbc aebcdf ebcdf fgc gbadf deagcfb gecb | gfc efacdb cg bacefd\ngbead cgdeb cg fbegcad bcefd cbfdeg ecfbga gec dgcf fecbad | cg geadb gce gc\nagebf cafeg dbfagc gecd cg gfceadb cga gafcde defac fbdcea | edcaf efdac eagcf cagfbd\ngbdefa eb cbfdag cdeb bcaefdg dcbfae febac feb gcafe adfbc | eafgc beacf gadfeb ebf\nabcfe ecgbfad ebda eca gfacb aecfgd dgfceb ea fbcde fdabec | acdbef deab ebcdf dbae\ncdaf abgfed ebcdgaf deabc fbcea cefabd edgbac fa afe fgbce | acbfe bcgeda acbef abdcge\neabcf cd gdbecf gfcaedb dcfg gafdeb fdbge cadebg edbfc dcb | bdcegf badcegf dbc gfecbd\ndagfb bfecga efdcb ebfcagd cg cbg ebcdaf cdgbf dcbgef egcd | agfdbec gc ebfcad edcg\ncfaeg fd bgead efcbgd gdefa fcda cgeadbf gfd gfbeac adegfc | facd dfgae fd degba\negfbd cagde fbeagcd fa aedfg ceagfd daf cfag cfedba abgdce | afcg af af fdega\ngcdfae aebdfg gfc abgec dcbf abfcg fc bdfceag fbdag bacgfd | afbged cf cfabg gfc\nfcbaged bafce cfag gbcdfe abecg cfb gbafce dgcabe dbefa cf | cf cgfa bcf fc\ncfed bdgfc bcd fcbegd eagcfb bdfag cd efcgb bdcgae dfcaegb | cbfge dcb gcdabe gcdbf\ncdg afbcg adec adgefb gebdfc dc gafde adgcf fgdeac gdfbcea | caed fadge ecbdfg ceda\nba gfbca fdcgba egdcabf agbdce fgeacd agb cfadg fecgb fdab | gcdbfa fadb agb ba\ncbfda ga cfbag adbcge acg aefg abfgce gefbc gdbecf cadebgf | ag dbfac agc bcgeaf\nbgcdea fgdce cg cfga defcb gadfeb gacfedb gcd edagf gafdce | fagecdb gedfab gbecda facg\nfcbeg gcabe bcfed gf gfbedc cfabed bfcgead gefd fgb fgcbad | acbeg adebfc bgf bgf\nbgcfed gedbf gf egf bgeacd decbg bcfgea cdgf bfcgade bafed | bgfdce dbaef gebcd bfcgae\nabecd bfcda gcdeba dfbe efgbac fcagd bcf fgebdac fdbace fb | fgcbea cfb bfc decab\nfc cadebf acgfb dfgabc bfgae gcdf fbceagd adcgb cbf adecgb | fdbagec fc aedcgb cf\nfe abgdef daefg bgcadfe dagbf dfbegc bfae gdbafc acedg gef | dagcfb baef cgedfb cabfgd\ngef fegacd cbefd daecbg agfd cdefg fg gecbafd dceag cfebag | ecgad acbdeg dcfbe gdecba\ncae fdeag gdcbae cagdfbe ce daefcg fgce acfdb egafbd acfde | gbdaec gafdce ce eac\nfdaegc dbefgca dcfa fedag fdbegc fd dcagbe edf fgeba cedag | efdcag fed dcgbfea df\nafcegb cadbef efb beadc fcdgb ef fbdce dfea agdcbfe gceabd | dcafbe eadf fcebga baced\ncbdfgae cfaeg ebgdfa eg gbafc decg gea cbafed afgdce fcade | dceafg bdaefc ge faced\nacbdfg bfc bc dbcg afbcd bfcdgea abdgf cfeagb fcead dfabeg | egcbaf egafdb agfbd egafcb\naefgdc bcfde gacb cgf bacefgd baegf ebfdga agcbfe cg ecgfb | fecadg bgfae aegdcf egfcba\nfdgcaeb facdbg dfc dgcab cf caefbd cebdga edgfa afcdg fbgc | dfc cf dfcga bfgc\ncdgebf fbegc cgfdea ecagfb gfbac aedbfcg ac fbagd eabc cga | faegcb ca abgcf ebfcg\nacbgfd fecgdb bcagf abeg ecdfa gcfae ge ecg cgbadfe fgbace | gec ge cgfba dfcae\naef efadgc cagedb cfde dagef ef aedcg ecbagf dcfabeg afdbg | ceagd afe fae gefad\ndbacfe cbf acefg abecdg fdbgcae bdfe bfeca bf fgdacb bdcae | cedab cefadb edfb fcbgaed\nebgf acdbfe adfgc baf fb fgeabc adfbecg abecg bgafc cedabg | fb bf gceadb dafgc\nfbcead gadb adebgfc ba aegcb caegf cab ecgfdb cbdeg aegbdc | decabg edcgb cgbfde dcgeb\nbafegdc becgdf cdebfa ecg bfedc gc fcbge eafgdc cdbg aefgb | fbecd acfedb gbcd cfagde\n")

(def t-in-med "be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe\nedbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc\nfgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg\nfbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb\naecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea\nfgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb\ndbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe\nbdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef\negadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb\ngcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce")

(defn parse-entries [input]
  (->> (str/split-lines input)
       (map #(str/split % #" "))
       (map #(partition-by #{"|"} %))
       (map (fn [[signal-patterns _ output-val]]
              [signal-patterns output-val]))))

(def readings (parse-entries input))

(def t-readings-med (parse-entries t-in-med))

;; Part 1 starts us off easy: just consider the digits that can be trivially decoded.
;; Since "1" is the only two-segment digit, we know what "1" is when we see just two segments
;; illuminated. The same logic applies for the numbers 4, 7 and 8.

(defn decode-reading [[_ output-val]]
  (map (fn [seg]
         (case (count seg)
           2 "1"
           3 "7"
           4 "4"
           7 "8"
           seg))
       output-val))

(defn part-1 [readings]
  (->> readings
       (mapcat decode-reading)
       (filter #{"1" "4" "7" "8"})
       (count)))

(rcf/tests
  (part-1 t-readings-med) := 26
  (part-1 readings) := 344)

;; Now, part 2 asks us to decode all digits of all clocks; so we need to use some
;; logical inference to determine, for example, which five-segment garbled-digits
;; correspond to the numbers 2, 3 and 5.

;; Here's where I made the rash decision to try out some logic programming (which I've
;; encountered previously only during the AoC, with either limited or no success). Hours
;; of ~~doc reading~~ key smashing later, and I have a program that works, despite being
;; absolute toxic to human eyes.

;; I hope to come back and find a cleaner, faster way to write this, but for now I'll
;; explain how I abuse core.logic and be on with my day.

;; First, I describe the real world of facts as regards working alarm clocks. A "1"
;; is made up of segments c and f, a seven by segments a, c, and f, etc.


(pldb/db-rel zero a b c e f g)
(pldb/db-rel one c f)
(pldb/db-rel two a c d e g)
(pldb/db-rel three a c d f g)
(pldb/db-rel four b c d f)
(pldb/db-rel five a b d f g)
(pldb/db-rel six a b d e f g)
(pldb/db-rel seven a c f)
(pldb/db-rel eight a b c d e f g)
(pldb/db-rel nine a b c d f g)
(def facts
  (pldb/db
    [one   'c 'f]
    [seven 'a 'c 'f]
    [four  'b 'c 'd 'f]
    [two   'a 'c 'd 'e 'g]
    [three 'a 'c 'd 'f 'g]
    [five  'a 'b 'd 'f 'g]
    [zero  'a 'b 'c 'e 'f 'g]
    [six   'a 'b 'd 'e 'f 'g]
    [nine  'a 'b 'c 'd 'f 'g]
    [eight 'a 'b 'c 'd 'e 'f 'g]))

;; I index my facts by the number of segments to which they correspond:

(def digs-by-num-segs {2 [one]
                       3 [seven]
                       4 [four]
                       5 [two three five]
                       6 [zero six nine]
                       7 [eight]})

;; And now the grand monstrosity, where I decode each clock.

(defn decode-out-val [[patterns out-val]]
  (let [
        ; Put the unambiguous patterns at the front, to constrain
        ; our solution space right off the bat
        patterns (reverse (sort-by (comp #{2 3 4 7} count) patterns))

        [result-symbols]
        (pldb/with-db facts
          ; Run until the first (only) solution is found
          (logic/run 1 [q]
           ; These lvars, once solved for, will mean: when I see 'a'
           ; in the output, the segment that's meant to be illuminated
           ; is actually that bound to a
           (logic/fresh [a b c d e f g]
            (let [lvar-by-char {\a a \b b \c c \d d \e e \f f \g g}]
              (logic/and*
                (for [[count chars] (map (juxt count seq) patterns)
                      :let [; Possible digits intended by the segment-pattern
                            cands (digs-by-num-segs count)
                            ; Lvars indicated by the pattern
                            lvars (map lvar-by-char chars)
                            ; Permutations of the segments; e.g. the number
                            ; one is represented by segments c and f, and c
                            ; and f may be mapped to a and b, or b and a,
                            ; respectively.
                            perms (combo/permutations lvars)
                            test-cand (fn [cand]
                                        (map #(apply cand %) perms))]]
                  ; The observed pattern must represent some number, somehow
                  (logic/or* (mapcat test-cand cands)))))
            (logic/== q [a b c d e f g]))))

        ; Lvars are bound to symbols (e.g. 'a); remap these to chars (e.g. a)
        [a b c d e f g] (->> result-symbols
                             (map str)
                             (map first))
        parse-dig (fn [segs]
                    (let [digs (->> segs
                                    ; Map the bogus segments to their
                                    ; intended segments
                                    (map {\a a \b b \c c \d d \e e \f f \g g})
                                    ; Sort the segments alphabetically for
                                    ; easy lookup
                                    (sort)
                                    (apply str))]
                      ({"abcefg"  0
                        "cf"      1
                        "acdeg"   2
                        "acdfg"   3
                        "bcdf"    4
                        "abdfg"   5
                        "abdefg"  6
                        "acf"     7
                        "abcdefg" 8
                        "abcdfg"  9} digs)))
        digs (reduce str (map parse-dig out-val))]
    (Integer/parseInt digs)))

(defn part-2 [readings]
  (reduce + (map decode-out-val readings)))

(rcf/tests
  (part-2 t-readings-med) := 61229
  (time (part-2 readings)) := 1048410)  ; => 35141.999 ms

;;

;; ⭐️⭐️
