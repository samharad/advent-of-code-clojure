(ns aoc.2022.day.07
  (:require [clojure.string :as str]))

(def input "$ cd /\n$ ls\ndir cmwrq\ndir ftrccld\ndir jjlbmtw\ndir jpncfpb\ndir mddr\ndir mthvntdd\n55644 pjts.dzh\ndir ptzsl\ndir wmqc\n$ cd cmwrq\n$ ls\ndir dtbzzl\ndir pjnghbm\n16144 rvs\n50956 swngfrsj.pcj\ndir vhvn\ndir vrt\ndir zgrjmtcq\n$ cd dtbzzl\n$ ls\n42503 ljhpmvd.zqf\ndir wwpnn\n$ cd wwpnn\n$ ls\n58541 jjdgzwnq\ndir lwqgsbg\ndir nztw\ndir rdtjztmt\n101609 sqqpcvq.llm\ndir ssdlqcrw\n$ cd lwqgsbg\n$ ls\n207528 cpqhb.jsf\n38543 cqjgspw\ndir dtbzzl\n106337 dtbzzl.njz\n302201 pdv.ppg\ndir pjts\n175215 pvczm.cfw\ndir sbvljdh\n$ cd dtbzzl\n$ ls\n252091 vhvn.zqv\n$ cd ..\n$ cd pjts\n$ ls\n155681 bdbfjbgt.rwg\n219192 dtcz.gqt\n$ cd ..\n$ cd sbvljdh\n$ ls\ndir rdrqc\ndir rtfpcswj\n$ cd rdrqc\n$ ls\n242263 pjts.mbt\n$ cd ..\n$ cd rtfpcswj\n$ ls\n228044 ssgcjt.twr\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd nztw\n$ ls\n30777 vqfsh.smp\n$ cd ..\n$ cd rdtjztmt\n$ ls\n276602 pvczm.cfw\ndir rzbb\n305089 ssdlqcrw.dgb\n$ cd rzbb\n$ ls\n155253 pvczm.cfw\n$ cd ..\n$ cd ..\n$ cd ssdlqcrw\n$ ls\n22423 vqfsh.smp\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd pjnghbm\n$ ls\n189296 ctqfg.ljd\ndir dtbzzl\ndir pjts\n205394 ssdlqcrw.lgv\n$ cd dtbzzl\n$ ls\n239152 fbb.gtn\ndir hlw\n39308 hsnbffzf.qvc\n211316 nhm.zhz\ndir nztw\ndir pvsjpn\n230237 twjq\n$ cd hlw\n$ ls\ndir lfqqrp\ndir nztw\n$ cd lfqqrp\n$ ls\ndir mbmfpz\ndir mdhfdlw\ndir pjts\ndir qzs\ndir ssdlqcrw\n$ cd mbmfpz\n$ ls\ndir fsrbwl\ndir lsmpw\n$ cd fsrbwl\n$ ls\n154657 ftlc.zbr\ndir ltsj\n228929 pvczm.cfw\ndir ssdlqcrw\n234216 tdl\n$ cd ltsj\n$ ls\n51204 vmq.sjg\n$ cd ..\n$ cd ssdlqcrw\n$ ls\n64928 nztw.gpn\n$ cd ..\n$ cd ..\n$ cd lsmpw\n$ ls\n61867 dtbzzl.dgj\n$ cd ..\n$ cd ..\n$ cd mdhfdlw\n$ ls\n92462 dtbzzl.jmq\n239442 tczcgf.zwj\n$ cd ..\n$ cd pjts\n$ ls\n144464 dtbzzl.lnz\ndir pjts\n118500 swgt.smz\n$ cd pjts\n$ ls\n173783 dvztnn\n103088 jlv.pgh\n39332 nhm.zhz\n266947 pppfcg\n$ cd ..\n$ cd ..\n$ cd qzs\n$ ls\n11155 cpqhb.jsf\n$ cd ..\n$ cd ssdlqcrw\n$ ls\n192414 gcwqcwrf.vmb\n$ cd ..\n$ cd ..\n$ cd nztw\n$ ls\n313009 nwt\n$ cd ..\n$ cd ..\n$ cd nztw\n$ ls\n280535 dtbzzl.grj\n269725 ssdlqcrw.tqs\n$ cd ..\n$ cd pvsjpn\n$ ls\n105150 jvjb.mdd\n142501 nztw.cvp\n$ cd ..\n$ cd ..\n$ cd pjts\n$ ls\ndir btc\ndir tpwcmvch\n259357 vqfsh.smp\n$ cd btc\n$ ls\n5264 gdjpql.wqr\n$ cd ..\n$ cd tpwcmvch\n$ ls\n141657 jjdgzwnq\n15650 nhm.zhz\ndir nlrq\n182100 qgf.qgj\n302332 qshf\n244799 vhvn\ndir wvnqzjf\n$ cd nlrq\n$ ls\ndir dtbzzl\n207207 gnd.vmb\n$ cd dtbzzl\n$ ls\n271143 wjbzmc\n$ cd ..\n$ cd ..\n$ cd wvnqzjf\n$ ls\n64128 mtzc.rqb\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd vhvn\n$ ls\n187526 vqfsh.smp\n$ cd ..\n$ cd vrt\n$ ls\ndir drrnm\ndir fqr\n270995 nztw.mfg\n137476 vqfsh.smp\n$ cd drrnm\n$ ls\n250912 pvczm.cfw\n$ cd ..\n$ cd fqr\n$ ls\n229272 nszfcq\ndir nztw\n170643 phh.pdl\n$ cd nztw\n$ ls\ndir bqf\n$ cd bqf\n$ ls\n9998 vqfsh.smp\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd zgrjmtcq\n$ ls\n109025 vhvn\n$ cd ..\n$ cd ..\n$ cd ftrccld\n$ ls\ndir dtbzzl\ndir fvmh\ndir fwztt\n22306 jngjc.mpd\n190320 lnr.jhn\ndir lsvvn\n295676 nztw\n135025 nztw.ssc\ndir pjts\ndir qglhlggq\ndir rslphgp\n247764 ssdlqcrw.jnm\ndir vhvn\n$ cd dtbzzl\n$ ls\ndir fgwh\n$ cd fgwh\n$ ls\ndir dpdvswq\n$ cd dpdvswq\n$ ls\ndir jsstq\n248465 vhvn\n$ cd jsstq\n$ ls\n252517 nztw\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd fvmh\n$ ls\ndir djcn\ndir dtbzzl\n303052 fbnnfsbp.zzg\n77238 mdpcghq.nls\ndir mvppnhr\n238683 ptw\ndir zdqlwnc\n$ cd djcn\n$ ls\n8600 jjdgzwnq\n$ cd ..\n$ cd dtbzzl\n$ ls\ndir sppdjcm\ndir vtnzqtvj\n$ cd sppdjcm\n$ ls\n237925 dvfctpg.zbn\ndir fghb\ndir pfjdsm\ndir pjts\n314661 zfchfq\n$ cd fghb\n$ ls\n280081 cpqhb.jsf\n88448 wbcpnnvs.sjc\n$ cd ..\n$ cd pfjdsm\n$ ls\n256877 bssmgf\n127978 drwttw\n103674 hznr.hjg\n$ cd ..\n$ cd pjts\n$ ls\n191709 qhwwpzn.dsc\n$ cd ..\n$ cd ..\n$ cd vtnzqtvj\n$ ls\ndir rrl\n$ cd rrl\n$ ls\n281036 jjdgzwnq\ndir lzlswv\ndir sjsqnvq\n245082 ssdlqcrw.smq\n$ cd lzlswv\n$ ls\ndir dmh\n$ cd dmh\n$ ls\n41234 hlhgn.mvr\n233542 tgv.csn\n$ cd ..\n$ cd ..\n$ cd sjsqnvq\n$ ls\n221327 qjncmbn\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd mvppnhr\n$ ls\ndir ldwv\n176153 nztw\ndir rmdjdqvl\ndir tmj\ndir vhvn\n$ cd ldwv\n$ ls\n161179 mjsm\n$ cd ..\n$ cd rmdjdqvl\n$ ls\ndir gnztqmhv\ndir lpmhfr\ndir tphjm\n$ cd gnztqmhv\n$ ls\n176043 qlds.mpq\n$ cd ..\n$ cd lpmhfr\n$ ls\ndir jrrdsd\n$ cd jrrdsd\n$ ls\n114477 vqfsh.smp\n$ cd ..\n$ cd ..\n$ cd tphjm\n$ ls\n74809 dcfmjn\n$ cd ..\n$ cd ..\n$ cd tmj\n$ ls\n252001 cpqhb.jsf\n49666 pqpq\n139885 qpj.wpb\n116339 vqfsh.smp\n$ cd ..\n$ cd vhvn\n$ ls\n89397 dtbzzl.hvp\n105454 pvczm.cfw\n280352 zdzm\n$ cd ..\n$ cd ..\n$ cd zdqlwnc\n$ ls\ndir fbhcv\n8676 jjdgzwnq\n99885 nhm.zhz\n234563 pjts.gdj\ndir rsdltnvc\n$ cd fbhcv\n$ ls\n71695 hrzzgwqt\n296401 vqfsh.smp\n$ cd ..\n$ cd rsdltnvc\n$ ls\n41623 gcvtqf\n233747 wdcssvgh.vfs\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd fwztt\n$ ls\n96594 jjdgzwnq\n245415 mtp.szl\n129782 pjts.jjr\n308104 pvczm.cfw\ndir ssdlqcrw\n155109 vhvn.smj\ndir vvzsr\n$ cd ssdlqcrw\n$ ls\ndir bzd\n292228 dtbzzl.tdb\n107505 ssdlqcrw\n181384 tfnrpsd\n$ cd bzd\n$ ls\n84648 brdc\n171457 vhvn\n$ cd ..\n$ cd ..\n$ cd vvzsr\n$ ls\ndir bcdqrs\n147437 jjdgzwnq\ndir ssdlqcrw\n197054 ssdlqcrw.dpz\ndir vhvn\ndir wthshgg\n$ cd bcdqrs\n$ ls\n297401 pspd.dlq\n136072 pvczm.cfw\n$ cd ..\n$ cd ssdlqcrw\n$ ls\n293104 dtbzzl.pdh\n$ cd ..\n$ cd vhvn\n$ ls\n178932 gvrht.cbm\n$ cd ..\n$ cd wthshgg\n$ ls\ndir dppwvtmp\ndir ljgszd\n88822 pcmw.bbq\n255776 pvczm.cfw\n163501 ssdlqcrw\ndir vbjsmgp\ndir vzqc\ndir zmpdrpd\n$ cd dppwvtmp\n$ ls\n45608 dtbzzl.lfq\n164648 gdch.bzp\n65225 nhm.zhz\n$ cd ..\n$ cd ljgszd\n$ ls\n125627 vqfsh.smp\n$ cd ..\n$ cd vbjsmgp\n$ ls\n236951 zpbgb.zmv\n$ cd ..\n$ cd vzqc\n$ ls\n234565 fjfpbjjp\n254986 jjdgzwnq\n164495 nztw.qhz\ndir vhvn\n$ cd vhvn\n$ ls\n199196 nztw\n$ cd ..\n$ cd ..\n$ cd zmpdrpd\n$ ls\n123210 bznqq.dbv\n141163 jjdgzwnq\n302352 wjf.tdv\n92016 wljnwsh\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd lsvvn\n$ ls\n282867 phv.ncc\n$ cd ..\n$ cd pjts\n$ ls\n40866 jjdgzwnq\n$ cd ..\n$ cd qglhlggq\n$ ls\n19577 dtbzzl.ngb\n21171 jjdgzwnq\n136074 pvczm.cfw\n212428 rlpjjf.lvh\ndir vhvn\n274669 wcqlws.ndv\ndir wpvq\n$ cd vhvn\n$ ls\n183301 cbppfp.vbc\n84069 cqnz\ndir dtbzzl\ndir mdng\n126627 pjts.pvp\ndir ptqq\n47594 pvczm.cfw\n154978 qlnnfbvd\n$ cd dtbzzl\n$ ls\n50385 ccgbrdmb.hrr\n22427 rzlwl.jbt\n$ cd ..\n$ cd mdng\n$ ls\ndir gdqqtvnp\n224013 gtv.tbz\n121884 jjdgzwnq\ndir nrmhpblm\n142950 nztw\n9710 pvczm.cfw\ndir vhvn\n$ cd gdqqtvnp\n$ ls\n292349 vhvn.nfr\n$ cd ..\n$ cd nrmhpblm\n$ ls\n52703 jbvd.mlc\n78268 pfns.lpr\n$ cd ..\n$ cd vhvn\n$ ls\n274549 pjts\n$ cd ..\n$ cd ..\n$ cd ptqq\n$ ls\n257967 jqppq.lgb\n166450 nhm.zhz\n$ cd ..\n$ cd ..\n$ cd wpvq\n$ ls\n173437 vqfsh.smp\n$ cd ..\n$ cd ..\n$ cd rslphgp\n$ ls\n29192 pvczm.cfw\n18984 ttpfnqvn.cdr\n302301 vqfsh.smp\n291211 vsvtc.wwf\n$ cd ..\n$ cd vhvn\n$ ls\ndir ssdlqcrw\n$ cd ssdlqcrw\n$ ls\n76864 jpwvws.fwv\n26365 nztw.css\n185966 vqfsh.smp\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd jjlbmtw\n$ ls\n211239 ctfhmm.ssv\n230020 nztw\n109641 sqtjn\n$ cd ..\n$ cd jpncfpb\n$ ls\ndir hjgwcmh\n286054 pcffhsw.bdm\n260831 pvczm.cfw\ndir vhvn\n$ cd hjgwcmh\n$ ls\n92277 bbjhc\ndir fmst\ndir gzjq\n$ cd fmst\n$ ls\n105833 cpqhb.jsf\n315858 nhm.zhz\n233459 nztw\n$ cd ..\n$ cd gzjq\n$ ls\ndir prjqfwf\ndir ssdlqcrw\n$ cd prjqfwf\n$ ls\n151003 jnmgdb.rhn\n$ cd ..\n$ cd ssdlqcrw\n$ ls\n103688 cpqhb.jsf\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd vhvn\n$ ls\n14901 cpqhb.jsf\n98212 tztzq\n$ cd ..\n$ cd ..\n$ cd mddr\n$ ls\ndir qpfjp\n$ cd qpfjp\n$ ls\ndir cfhv\n$ cd cfhv\n$ ls\ndir ssdlqcrw\n$ cd ssdlqcrw\n$ ls\n134280 vvnpvrqb.hdv\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd mthvntdd\n$ ls\ndir bcdcz\ndir cngbf\n62389 cwtvl\ndir mqjjbq\ndir nhblb\n6743 pvczm.cfw\ndir ssdlqcrw\ndir ttvgr\ndir vdmm\ndir wnhnwjm\ndir zdvbsb\n$ cd bcdcz\n$ ls\n213688 dtbzzl.hsv\ndir lbvbc\n100222 nndbhrf\n115627 rqnsfbz.rmf\ndir tvgclpsc\n258672 vqfsh.smp\n163927 whgmd\n$ cd lbvbc\n$ ls\n224836 fpfpwtf.zfz\n103806 nztw\n$ cd ..\n$ cd tvgclpsc\n$ ls\n76900 cpqhb.jsf\n282820 qtffdmsg\n$ cd ..\n$ cd ..\n$ cd cngbf\n$ ls\ndir hstph\n12089 jqvnttq.dsh\n38052 nztw.sqj\ndir qrnpjz\n$ cd hstph\n$ ls\n172788 pjts.qmt\n$ cd ..\n$ cd qrnpjz\n$ ls\ndir blzc\ndir rvl\ndir zvhtzqqc\n$ cd blzc\n$ ls\n108342 nhm.zhz\n$ cd ..\n$ cd rvl\n$ ls\ndir bcrf\ndir sjbr\n$ cd bcrf\n$ ls\n182498 cpqhb.jsf\ndir dcb\n14228 ggsq\ndir gnhvtgm\n$ cd dcb\n$ ls\ndir zlgjzcjv\n$ cd zlgjzcjv\n$ ls\n18316 cpqhb.jsf\n$ cd ..\n$ cd ..\n$ cd gnhvtgm\n$ ls\n110236 nhm.zhz\n$ cd ..\n$ cd ..\n$ cd sjbr\n$ ls\n133009 cscbp\n315907 vtpmnwt\n$ cd ..\n$ cd ..\n$ cd zvhtzqqc\n$ ls\ndir fglfpn\ndir gtzrq\ndir hfgdcf\n274977 ltbzhjn\ndir msc\ndir ssdlqcrw\n$ cd fglfpn\n$ ls\n39153 dvhjpfc\n$ cd ..\n$ cd gtzrq\n$ ls\n60625 sqljdlpz.wpw\n$ cd ..\n$ cd hfgdcf\n$ ls\n36016 qdvnn.pbt\n$ cd ..\n$ cd msc\n$ ls\n56601 cpqhb.jsf\ndir hrz\ndir vlhllqz\n$ cd hrz\n$ ls\n241511 fhngt.mlb\n286505 nhm.zhz\n$ cd ..\n$ cd vlhllqz\n$ ls\n157880 nhm.zhz\n$ cd ..\n$ cd ..\n$ cd ssdlqcrw\n$ ls\n121507 dssrvr\n295897 lvtwlb.whn\n12047 pjts.gqc\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd mqjjbq\n$ ls\n157818 blbmb.fcv\n119103 ccppbmqb.pbt\n141463 cpqhb.jsf\n197900 drhmws.fdd\ndir fmvp\ndir rhldnjlt\n175029 vqfsh.smp\n$ cd fmvp\n$ ls\ndir dhnn\ndir dlcvwqw\n131432 hnv.tlr\ndir jzqt\n98127 nhm.zhz\ndir nvsdbjj\ndir pjts\n9179 pvczm.cfw\n121310 vqfsh.smp\n$ cd dhnn\n$ ls\n173921 qcjsdg.zfg\n58654 vhvn.csb\n$ cd ..\n$ cd dlcvwqw\n$ ls\n285116 zjb\n$ cd ..\n$ cd jzqt\n$ ls\n104478 clmzwnf\n299622 cpqhb.jsf\n301236 jjdgzwnq\ndir nsvlqq\n136737 vhvn\ndir vmp\n12932 wrd.jsz\n$ cd nsvlqq\n$ ls\n111712 dtbzzl.htn\n213593 hvzlmtj.ztr\n$ cd ..\n$ cd vmp\n$ ls\n104275 jjdgzwnq\n$ cd ..\n$ cd ..\n$ cd nvsdbjj\n$ ls\n180999 jjdgzwnq\n219819 vhvn\n$ cd ..\n$ cd pjts\n$ ls\n111715 npzn\n$ cd ..\n$ cd ..\n$ cd rhldnjlt\n$ ls\ndir ffhcbvmf\ndir vprlq\n$ cd ffhcbvmf\n$ ls\n247668 cpqhb.jsf\n$ cd ..\n$ cd vprlq\n$ ls\n168090 jmmtz.fzt\n68360 nhm.zhz\n304580 vqfsh.smp\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd nhblb\n$ ls\n154794 hrgsrbnj.tch\ndir nfwl\ndir ptc\ndir rng\n50110 swtt.tct\ndir vhvn\ndir vlj\n$ cd nfwl\n$ ls\ndir lqs\ndir mlvnlz\n$ cd lqs\n$ ls\ndir mbcft\ndir ntmvt\ndir nztw\n$ cd mbcft\n$ ls\n78188 bdnr\n194668 pjts\n$ cd ..\n$ cd ntmvt\n$ ls\n75647 nhm.zhz\n186651 scsvrqpf.jhb\n$ cd ..\n$ cd nztw\n$ ls\n164920 vqfsh.smp\n$ cd ..\n$ cd ..\n$ cd mlvnlz\n$ ls\n289891 wjf\n$ cd ..\n$ cd ..\n$ cd ptc\n$ ls\n190002 pjts.vmh\n$ cd ..\n$ cd rng\n$ ls\n39093 nhm.zhz\n$ cd ..\n$ cd vhvn\n$ ls\n275854 hbv\n$ cd ..\n$ cd vlj\n$ ls\ndir qqqrm\n203390 ssdlqcrw\n$ cd qqqrm\n$ ls\ndir wcpllh\n$ cd wcpllh\n$ ls\ndir pwg\n$ cd pwg\n$ ls\n19102 dtbzzl.qvp\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd ssdlqcrw\n$ ls\n181610 vqfsh.smp\n$ cd ..\n$ cd ttvgr\n$ ls\ndir vpcpd\n$ cd vpcpd\n$ ls\n28102 mbb.szv\n304017 rshrzjhn\n$ cd ..\n$ cd ..\n$ cd vdmm\n$ ls\n95079 tssjcd.lfg\n$ cd ..\n$ cd wnhnwjm\n$ ls\n67931 mmhcgsc.zjf\n22062 nqpzsf.ccc\n219285 trr.vcn\n$ cd ..\n$ cd zdvbsb\n$ ls\n293736 dtbzzl.ftj\n$ cd ..\n$ cd ..\n$ cd ptzsl\n$ ls\n26404 jnsdzmbd\n$ cd ..\n$ cd wmqc\n$ ls\ndir dtbzzl\ndir hdzmzc\ndir nmmpwqvz\ndir qjnm\n$ cd dtbzzl\n$ ls\ndir hpzgnb\n$ cd hpzgnb\n$ ls\n189696 sbmdrbm\n$ cd ..\n$ cd ..\n$ cd hdzmzc\n$ ls\n143510 dtbzzl.dmp\n$ cd ..\n$ cd nmmpwqvz\n$ ls\n276725 nhm.zhz\n$ cd ..\n$ cd qjnm\n$ ls\n202264 cpqhb.jsf\n")

(defrecord Interaction [command output-lines])

(defn command? [line]
  (str/starts-with? line "$"))

(defn parse-interaction [lines]
  (when (seq lines)
    (let [trimmed (drop-while (complement command?) lines)
          [command & more] trimmed
          command (rest (str/split command #" "))
          [output more] (split-with (complement command?) more)
          interaction (->Interaction command output)]
      [interaction more])))

(defn parse-terminal-session [input]
  (let [lines (str/split-lines input)]
    (loop [interactions []
           lines lines]
      (if (empty? lines)
        interactions
        (let [[interaction lines] (parse-interaction lines)]
          (recur (conj interactions interaction) lines))))))

(def terminal-session (parse-terminal-session input))

(defn cd [{:keys [command]} tree path]
  (let [[_ target] command
        path' (case target
                ".." (pop path)
                "/" ["/"]
                (conj path target))]
    [tree path']))

(defn ls [{:keys [output-lines]} tree path]
  (let [children (->> output-lines
                      (map #(str/split % #" "))
                      (map (fn [[l r]]
                             (if (= l "dir")
                               [r nil]
                               [r (parse-long l)])))
                      (into {}))
        tree' (assoc-in tree path children)]
    [tree' path]))

(defn infer-file-tree [session]
  (loop [tree {"/" nil}
         path nil
         [interaction & more-interactions] session]
    (if (nil? interaction)
      tree
      (let [[tree' path']
            (case (first (:command interaction))
              "cd" (cd interaction tree path)
              "ls" (ls interaction tree path)
              (throw (ex-info "unknown command"
                              {:command interaction})))]
        (recur tree' path' more-interactions)))))

(def file-tree (infer-file-tree terminal-session))

(defn dir? [[_name v]] (map? v))
(def file? (complement dir?))

(defn node-size [[_name v :as node]]
  (if (file? node)
    v
    (->> v
         (map node-size)
         (reduce +))))

(defn children [[_name children :as dir]]
  children)

(def all-nodes
  (tree-seq dir? children (first file-tree)))

(->> all-nodes
     (filter dir?)
     (map node-size)
     (filter #(<= % 100000))
     (reduce +))

(def disk-size 70000000)
(def update-requires-size 30000000)
(def currently-used-size (node-size (first file-tree)))
(def currently-free-size (- disk-size currently-used-size))
(def need-delete-size (- update-requires-size currently-free-size))

(->> all-nodes
     (filter dir?)
     (map node-size)
     sort
     (some #(when (>= % need-delete-size)
              %)))
