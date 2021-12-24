(ns aoc.2021.day.22
  (:require [clojure.string :as str]
            [hyperfiddle.rcf :as rcf]
            [clojure.math.combinatorics :as combo]
            [clojure.pprint :as pp]
            [aoc.2021.cuboid :as cub]))

(rcf/enable!)

(def t-input "on x=10..12,y=10..12,z=10..12\non x=11..13,y=11..13,z=11..13\noff x=9..11,y=9..11,z=9..11\non x=10..10,y=10..10,z=10..10")
(def t-input-large "on x=-20..26,y=-36..17,z=-47..7\non x=-20..33,y=-21..23,z=-26..28\non x=-22..28,y=-29..23,z=-38..16\non x=-46..7,y=-6..46,z=-50..-1\non x=-49..1,y=-3..46,z=-24..28\non x=2..47,y=-22..22,z=-23..27\non x=-27..23,y=-28..26,z=-21..29\non x=-39..5,y=-6..47,z=-3..44\non x=-30..21,y=-8..43,z=-13..34\non x=-22..26,y=-27..20,z=-29..19\noff x=-48..-32,y=26..41,z=-47..-37\non x=-12..35,y=6..50,z=-50..-2\noff x=-48..-32,y=-32..-16,z=-15..-5\non x=-18..26,y=-33..15,z=-7..46\noff x=-40..-22,y=-38..-28,z=23..41\non x=-16..35,y=-41..10,z=-47..6\noff x=-32..-23,y=11..30,z=-14..3\non x=-49..-5,y=-3..45,z=-29..18\noff x=18..30,y=-20..-8,z=-3..13\non x=-41..9,y=-7..43,z=-33..15\non x=-54112..-39298,y=-85059..-49293,z=-27449..7877\non x=967..23432,y=45373..81175,z=27513..53682")
(def input "on x=-25..19,y=-17..32,z=-18..27\non x=-16..34,y=-36..15,z=-4..44\non x=-18..33,y=-44..10,z=-1..48\non x=-10..44,y=-24..25,z=-45..3\non x=-49..-2,y=-44..3,z=-35..11\non x=-5..42,y=-48..1,z=-15..35\non x=-26..27,y=-4..49,z=-8..44\non x=-47..-1,y=-40..6,z=-11..40\non x=-41..12,y=-41..9,z=-12..38\non x=-49..4,y=-27..18,z=-12..38\noff x=11..25,y=1..10,z=-6..7\non x=-33..17,y=-22..29,z=-8..37\noff x=-39..-27,y=11..24,z=-16..-1\non x=-28..18,y=-39..6,z=-39..13\noff x=27..41,y=-23..-10,z=35..44\non x=-4..40,y=1..45,z=-21..25\noff x=36..49,y=16..33,z=-47..-31\non x=-42..5,y=-43..8,z=-35..14\noff x=-39..-22,y=20..29,z=19..30\non x=-10..34,y=-45..5,z=-30..16\non x=58130..74792,y=4830..35228,z=39318..63561\non x=-40466..-17822,y=-77868..-64431,z=-3886..11187\non x=-19073..-1463,y=65422..82885,z=23304..43978\non x=46738..71241,y=39352..59200,z=-5917..-3830\non x=-19010..13307,y=-96005..-74582,z=-19665..-2574\non x=-60112..-47300,y=-46572..-26967,z=-59253..-29957\non x=-66448..-41722,y=35529..57095,z=24647..44817\non x=-32187..-7009,y=-58802..-52885,z=-59593..-37103\non x=-90935..-64508,y=15951..29237,z=7318..29845\non x=7010..16589,y=65579..81354,z=-39391..-8720\non x=38716..51593,y=-22916..9050,z=51191..72341\non x=41708..72592,y=21789..55040,z=16830..37085\non x=-86698..-50596,y=-14192..-2839,z=35619..46625\non x=-46830..-27930,y=-81448..-59759,z=-6028..21764\non x=-44016..-36639,y=-61840..-28937,z=36509..67942\non x=13310..31955,y=-68626..-49294,z=-46784..-39318\non x=38860..64742,y=38732..58725,z=-43510..-29002\non x=-90461..-67594,y=-53950..-29966,z=-9548..3630\non x=-67554..-57558,y=-54779..-44189,z=21159..31677\non x=3757..32872,y=-80070..-60915,z=5277..16947\non x=-72838..-48066,y=-5015..17491,z=25977..48301\non x=23477..41778,y=15763..42256,z=-82192..-56024\non x=52710..75131,y=-29088..-1205,z=-41381..-37580\non x=38409..57853,y=-50857..-31314,z=-59158..-52612\non x=-80116..-48960,y=-39594..-13419,z=-45425..-15108\non x=65797..83032,y=-51087..-29660,z=-23996..1975\non x=53353..80001,y=12347..28197,z=-23588..-1312\non x=-71662..-65247,y=-53225..-30719,z=-9866..11030\non x=49873..73770,y=-48745..-15568,z=28177..52885\non x=69904..79221,y=26365..47073,z=-29095..-16378\non x=70750..92569,y=-22117..-3579,z=-16440..-10396\non x=58085..80322,y=28177..67663,z=9272..17316\non x=9120..29041,y=35569..52486,z=49397..69227\non x=7148..32565,y=-88490..-60413,z=-4685..13676\non x=-4357..13624,y=57740..73053,z=-66175..-35343\non x=-72404..-50117,y=-18666..17680,z=-52896..-39914\non x=-9567..9523,y=-15111..8108,z=-90208..-79702\non x=-10948..12900,y=-92952..-61380,z=8585..35744\non x=-39691..-33001,y=-481..16835,z=-85521..-64353\non x=-41700..-38162,y=58196..66356,z=-54670..-34351\non x=-41782..-26325,y=30554..48802,z=43336..61539\non x=7603..24698,y=-58399..-42038,z=-54977..-50149\non x=-74963..-67013,y=-38190..-18104,z=-48442..-23429\non x=-63905..-48685,y=46845..71472,z=-18004..2350\non x=14437..49543,y=-62065..-41585,z=-49774..-37768\non x=-5517..15643,y=-16972..20782,z=60593..96969\non x=-92280..-58006,y=7885..39177,z=449..35282\non x=-4290..17666,y=-90595..-63406,z=8750..23433\non x=6979..28454,y=-22944..-3734,z=-77888..-74927\non x=24287..50795,y=-68103..-32134,z=46841..62225\non x=-18963..-7266,y=-68315..-53968,z=-67632..-35819\non x=-29535..-12178,y=67827..82507,z=-29427..-28303\non x=-15190..10688,y=62804..80708,z=37673..50924\non x=51637..82062,y=-23425..-13849,z=25263..44367\non x=-57999..-42079,y=-61465..-54845,z=7054..41290\non x=61972..71946,y=-6088..14399,z=39883..57439\non x=47066..73652,y=3289..26576,z=39216..58947\non x=-82190..-50896,y=14389..24693,z=27922..44002\non x=24953..52534,y=-73272..-59039,z=-19032..-8246\non x=3134..24046,y=31174..51696,z=-66891..-61000\non x=-79297..-65977,y=10686..30665,z=-19492..-8395\non x=18022..42598,y=-23168..-4267,z=57820..78601\non x=61665..84231,y=-19314..2438,z=1604..18531\non x=-21091..-7592,y=-3117..7700,z=-78569..-65004\non x=62684..81399,y=-35984..-5816,z=14750..34173\non x=-26998..-6601,y=-5986..14501,z=77097..95680\non x=-72600..-53340,y=-38496..-21227,z=-62500..-45403\non x=31773..45502,y=-11640..4290,z=-75810..-59658\non x=-80495..-43424,y=28216..47742,z=-46020..-19497\non x=33917..56972,y=-71159..-50513,z=33699..53033\non x=-4895..18133,y=-93735..-60850,z=-26226..-16640\non x=-28630..-5186,y=-37588..-29317,z=56086..73887\non x=-31062..-21579,y=33327..60798,z=49401..73175\non x=-10957..1264,y=-47922..-25717,z=56267..76856\non x=-26519..-19291,y=-80269..-58812,z=4739..21059\non x=-73392..-68495,y=-53325..-36377,z=-22576..-10983\non x=-57251..-42571,y=-55067..-50711,z=21956..28445\non x=-21760..3615,y=-30637..-8269,z=-92600..-72783\non x=-78712..-68343,y=-35631..-8253,z=-49432..-32291\non x=-98297..-62132,y=-10182..17937,z=-21663..-2939\non x=-49619..-20903,y=-81950..-49731,z=14331..36557\non x=-77719..-54696,y=-4933..5387,z=-54375..-38489\non x=18952..30044,y=-5948..8556,z=71419..79100\non x=-14443..6073,y=9664..24826,z=65287..92698\non x=19757..49423,y=9775..23431,z=53476..87732\non x=-390..13958,y=78578..83672,z=-14643..5068\non x=-17569..1231,y=-52196..-24285,z=66712..88356\non x=22102..26555,y=-77462..-58381,z=22142..32757\non x=22592..52007,y=29247..32200,z=54297..72364\non x=-77059..-51695,y=-30316..-16003,z=25367..39748\non x=42986..64693,y=35661..68372,z=-29918..-11953\non x=-34041..-4237,y=-88022..-67620,z=-26207..-3610\non x=-49834..-37233,y=-59504..-34261,z=34131..66365\non x=65349..94319,y=-19835..-6545,z=-24667..-708\non x=13050..44190,y=5167..32154,z=-89339..-58446\non x=45597..62970,y=-9451..11044,z=-56915..-40832\non x=-9732..11852,y=9820..30333,z=-92396..-67585\non x=38976..54998,y=-22749..1348,z=51283..63636\non x=73378..96435,y=-12058..18745,z=-30246..-4493\non x=-89828..-69363,y=10042..23961,z=12175..29039\non x=2941..12201,y=-78520..-66567,z=-26924..-14825\non x=-68528..-60099,y=9887..25885,z=29052..59211\non x=9315..24935,y=-88183..-57281,z=-15962..21546\non x=-85263..-72812,y=-34542..-23564,z=-24492..-5477\non x=-95590..-58694,y=15268..16414,z=-20338..12400\non x=47161..61390,y=-51497..-46140,z=-54082..-41603\non x=-24948..-860,y=65273..85686,z=3751..18779\non x=-74872..-70466,y=-10445..20449,z=-42187..-23559\non x=15395..36309,y=51601..79496,z=-55445..-38989\non x=-6788..-2487,y=-88523..-71818,z=-38861..-15152\non x=-81443..-68997,y=4713..18849,z=-2294..25453\non x=-27684..-12475,y=-78207..-52418,z=24610..51493\non x=-45965..-22263,y=18486..35080,z=52396..77570\non x=-16449..-6272,y=-81117..-56749,z=-35606..-21607\non x=10951..31839,y=55087..74608,z=-47000..-28178\non x=-84540..-51147,y=28945..62947,z=-4032..18597\non x=36357..50129,y=16893..23034,z=64171..74913\non x=-3998..20566,y=59440..93316,z=22696..25488\non x=-54915..-23612,y=-68560..-45524,z=-58054..-37867\non x=-35598..-25037,y=-43854..-18452,z=54139..77736\non x=-64988..-54266,y=-48957..-21857,z=28252..32986\non x=-95466..-72491,y=-36404..-11874,z=-4259..8571\non x=-81482..-72858,y=4863..36596,z=3393..17002\non x=-14906..-9140,y=75824..91655,z=14158..31682\non x=-56168..-38184,y=68009..72189,z=1057..5047\non x=-89689..-62813,y=-13369..18363,z=9600..42828\non x=-51440..-32934,y=31208..42008,z=-68421..-53583\non x=-6288..16856,y=-42060..-29202,z=68124..80036\non x=-83895..-61949,y=3336..25725,z=9552..19154\non x=-61288..-51950,y=-55736..-44850,z=-1436..30098\non x=-23437..-115,y=-48046..-32435,z=59063..81720\non x=-85940..-65530,y=2023..13769,z=12538..35840\non x=-17034..642,y=72777..94279,z=4086..24430\non x=-29254..-7953,y=-92196..-72024,z=5829..40481\non x=19780..33000,y=42445..63200,z=-53200..-41275\non x=-12441..16072,y=18286..40034,z=68730..94707\non x=-16418..4994,y=-26517..6769,z=72160..92524\non x=-19492..1556,y=47218..72884,z=-67974..-48038\non x=26010..60405,y=-79985..-56817,z=-10117..20209\non x=55184..79442,y=-35184..-11981,z=27930..47203\non x=-44505..-14453,y=69915..76169,z=-36486..-15678\non x=47765..71553,y=-16894..8462,z=-77595..-43228\non x=20787..35894,y=41719..63073,z=-71757..-51052\non x=1043..34518,y=-80401..-65743,z=-12161..22280\non x=-6882..20242,y=27697..41680,z=55075..91076\non x=35532..49684,y=65792..80184,z=-17848..6540\non x=58693..84924,y=15648..25639,z=-45401..-19689\non x=-85098..-78038,y=-10846..-134,z=-5850..15444\non x=-2730..12605,y=12851..43889,z=67815..81883\non x=-29043..-9448,y=48093..78524,z=50467..63696\non x=-38250..-17112,y=1181..30921,z=67073..85611\non x=18694..45770,y=-63824..-33467,z=-65348..-32736\non x=-48050..-12127,y=-36602..-23731,z=64057..84572\non x=-92730..-66419,y=-15873..4455,z=-37327..-8638\non x=-77520..-47498,y=-31489..-17959,z=38570..55019\non x=-35101..-28002,y=-81178..-65021,z=-43410..-13765\non x=-81154..-45690,y=3545..24492,z=-44708..-26663\non x=-4366..11895,y=64672..90379,z=30279..50474\non x=2570..35120,y=-72500..-66047,z=-48806..-29387\non x=-17836..3311,y=-37959..-15427,z=58018..83171\non x=-41222..-23220,y=-83264..-48361,z=-28535..-9809\non x=-32657..-6854,y=-63857..-33169,z=50784..74629\non x=31759..58009,y=58130..81062,z=-12947..19610\non x=-77910..-70462,y=-25392..-8935,z=21699..28460\non x=-74375..-69543,y=-39903..-11033,z=556..12066\non x=-96260..-69319,y=-13239..15749,z=-5861..32245\non x=-89059..-57112,y=-29085..-10293,z=4791..37832\non x=-53908..-32723,y=41908..48893,z=43394..62135\non x=-56878..-35991,y=-16413..16902,z=49630..72926\non x=-23547..5628,y=62602..94384,z=-21369..6727\non x=-53850..-37537,y=-13642..4380,z=52105..72940\non x=31257..52545,y=-52332..-26349,z=42207..66321\non x=2684..30781,y=28321..51558,z=-64015..-48974\non x=-3053..917,y=60451..96694,z=5038..23673\non x=-8930..1238,y=-80903..-64783,z=23996..37917\non x=32035..39229,y=-63892..-44117,z=-54484..-29812\non x=-19633..5010,y=70766..81566,z=257..11177\non x=2204..32041,y=-13135..1289,z=58814..96086\non x=32579..57092,y=50782..69021,z=16603..44417\non x=24380..52350,y=-74864..-64943,z=16359..37394\non x=5080..16681,y=34283..58295,z=-71141..-48408\non x=65497..86700,y=-43687..-21647,z=-29471..-22694\non x=-78966..-55978,y=-51271..-18249,z=7472..26951\non x=-3302..20676,y=-99123..-73526,z=-4992..26620\non x=-4361..21592,y=-63639..-52408,z=-61167..-52807\non x=12881..29105,y=55833..65015,z=25636..50553\non x=-22870..7514,y=44949..67069,z=-68698..-54147\non x=4489..23016,y=46700..73193,z=42992..54291\non x=33906..55000,y=-84469..-54728,z=-27445..-3981\non x=-4535..23944,y=29291..58817,z=50920..67512\non x=74404..93376,y=-28674..-4357,z=1529..21378\non x=-42096..-39330,y=53218..68450,z=21313..49413\non x=-79424..-46791,y=8607..30438,z=-46626..-37391\non x=-77257..-57467,y=8975..41058,z=31151..63104\non x=-36397..-6018,y=65533..81964,z=8100..27782\non x=1772..13114,y=-45452..-24542,z=51315..72370\non x=-56414..-43858,y=-15760..-1511,z=53056..65252\non x=5178..19230,y=66264..77905,z=-37387..-20995\non x=-44447..-28857,y=-7474..1043,z=-79436..-63272\non x=31312..61933,y=45570..59005,z=-50556..-35448\non x=-54070..-25768,y=-49818..-25232,z=-61393..-41248\noff x=-23531..-8752,y=-12082..15447,z=-98148..-77085\noff x=-26458..-11303,y=-14757..19158,z=-84941..-70774\noff x=67054..69860,y=10769..36987,z=-48226..-15571\non x=48596..79742,y=43523..54624,z=1937..19705\non x=21840..45194,y=-88151..-63952,z=23272..43757\noff x=-56180..-30105,y=-23034..-10177,z=45362..82434\noff x=-30367..-12221,y=-56677..-45797,z=41015..77005\noff x=3957..24789,y=-33453..-16504,z=75407..93827\non x=16677..53031,y=-81001..-63422,z=-26709..5644\non x=3897..26268,y=-80809..-58412,z=-53456..-31096\noff x=-53935..-34004,y=-64717..-30773,z=-63167..-51325\non x=-51734..-42462,y=-31476..-29266,z=42510..61608\non x=41714..67356,y=-56338..-39534,z=-57329..-34656\non x=-29703..102,y=-48375..-32386,z=50956..78822\noff x=18805..29389,y=55214..61118,z=-54668..-36938\noff x=-43634..-27449,y=-44154..-40158,z=-64794..-39780\noff x=26340..46631,y=32695..56730,z=-72694..-49637\non x=-67221..-33213,y=42396..68108,z=6296..35814\noff x=47145..74448,y=16794..27177,z=-53775..-38461\noff x=-71161..-35857,y=11495..38517,z=-69460..-49322\noff x=-51569..-34369,y=-57058..-40466,z=-48249..-35893\non x=76969..94122,y=-9622..4260,z=-11777..9827\noff x=-25468..-9830,y=-89766..-62827,z=-23077..3267\non x=-34914..-16785,y=43235..66914,z=47390..70799\non x=-43047..-25405,y=29640..46573,z=58243..79904\non x=-65414..-55209,y=30985..66345,z=-40780..-23211\non x=-56019..-47580,y=2182..7294,z=48708..59721\non x=30197..41111,y=54185..76003,z=-45138..-33569\noff x=30206..46343,y=-48504..-25452,z=-64948..-61437\noff x=46610..52577,y=-53600..-35507,z=36685..63416\non x=-67663..-48131,y=-9151..14784,z=-65365..-50610\non x=-84458..-60979,y=2763..20965,z=-50046..-31834\noff x=44885..70482,y=22096..53275,z=-47233..-35548\noff x=33735..54437,y=-64543..-50572,z=18089..42914\non x=49649..55370,y=-59126..-51117,z=15176..38972\non x=34525..49133,y=-74322..-51979,z=6510..45365\noff x=-67081..-43347,y=-16984..-2265,z=-54556..-44149\non x=-18962..6882,y=-21555..-1276,z=-79408..-74695\non x=-85431..-67417,y=20207..37574,z=-24683..1131\non x=-87398..-57912,y=-46757..-20616,z=-1696..35333\noff x=-38761..-24011,y=56096..77032,z=-8538..7289\noff x=66762..83683,y=-26879..-2102,z=13838..46715\non x=47061..74282,y=49221..66218,z=4113..26350\non x=4777..21230,y=-33897..-5656,z=-87420..-71323\non x=30099..57071,y=-21885..5161,z=-79787..-52376\noff x=-44596..-31390,y=-46512..-37910,z=-62932..-56097\noff x=-14054..12636,y=-31346..-16168,z=67008..87013\non x=30408..66968,y=23194..37084,z=-69204..-39793\non x=22564..36076,y=-37139..-18687,z=50359..66811\non x=-61861..-46587,y=28526..48242,z=-32086..-24184\non x=-82280..-50593,y=2728..22792,z=-60682..-29871\non x=-783..16463,y=-47282..-16939,z=64502..74420\noff x=-47520..-14633,y=40759..47312,z=46161..76712\non x=-69277..-54302,y=24768..49368,z=15566..35795\noff x=12622..41719,y=35874..58381,z=43857..67791\noff x=-799..16093,y=-1219..20716,z=67589..87424\non x=35940..51327,y=30163..46992,z=-59947..-50999\noff x=-9321..2965,y=7853..36841,z=68030..81454\noff x=-37124..-30202,y=-80579..-51201,z=-40645..-18050\noff x=28596..52892,y=47368..72821,z=9780..31479\non x=8990..18432,y=-36443..-12728,z=-95059..-69642\noff x=-77654..-67477,y=-2706..4112,z=28813..58150\non x=-51071..-36543,y=41044..49939,z=42109..59713\non x=-10079..19481,y=62783..91315,z=184..34000\non x=-2397..3530,y=-11556..-706,z=76963..86864\non x=6108..21196,y=-44694..-17116,z=61649..84795\non x=46991..61627,y=-75243..-47914,z=7700..32503\noff x=59336..64609,y=-8125..11873,z=33615..71285\noff x=-85488..-61850,y=-6467..26523,z=6343..10566\noff x=-84524..-67431,y=-11010..9472,z=-22622..-2724\noff x=57508..72883,y=-50521..-39545,z=2734..6998\noff x=53809..64551,y=-35985..-23521,z=50129..54626\non x=31383..51927,y=-47560..-26142,z=56108..59189\non x=64838..70463,y=18960..46107,z=-5709..5891\noff x=-31932..-10900,y=62729..84704,z=-37694..-13759\noff x=40118..68033,y=-41588..-36259,z=34880..60200\non x=39381..48090,y=-68538..-40574,z=-52761..-43347\noff x=64765..72987,y=-14028..16757,z=36141..47632\noff x=5855..25039,y=-85798..-60707,z=-30495..-20735\non x=57912..77906,y=42168..50409,z=-28700..2962\non x=19631..35834,y=28921..54740,z=51647..77378\noff x=-82481..-71581,y=21190..40353,z=-24926..-2552\noff x=-46450..-22899,y=-68165..-38827,z=-54999..-42378\non x=-54128..-36413,y=8300..40253,z=48335..77869\non x=34740..55414,y=5155..8343,z=-72542..-54001\non x=31990..55541,y=-84291..-50415,z=-38724..-8673\non x=-48741..-26045,y=3191..16457,z=-73525..-68070\noff x=-33831..-29433,y=-1626..17107,z=-79534..-68499\non x=-42587..-15988,y=-83266..-62879,z=3424..23280\non x=-38590..-25325,y=-64154..-45384,z=-54835..-40345\non x=-13947..-1435,y=-92195..-76444,z=5699..36092\noff x=45044..57694,y=-14002..7161,z=-61158..-45716\noff x=38275..46353,y=-67689..-52479,z=-14487..1146\non x=32016..45320,y=43682..68546,z=23400..42946\noff x=-3019..17260,y=63031..81988,z=32500..57080\non x=-217..18895,y=-30559..-6455,z=-83475..-70541\noff x=-44798..-20533,y=-14628..4328,z=63367..89914\non x=-66126..-51005,y=-48724..-27264,z=9877..18967\noff x=4493..39312,y=-92170..-64063,z=-32182..-4856\non x=-12782..-1953,y=44313..64242,z=55493..60786\noff x=-83865..-61599,y=20014..32460,z=-32367..-2728\noff x=-44145..-28037,y=-74041..-63219,z=-31805..-9417\noff x=-45440..-16291,y=-58154..-37500,z=49909..61326\non x=-52220..-20559,y=-15232..-8220,z=-71909..-62090\non x=42800..57928,y=41072..73675,z=-12386..12198\noff x=64013..66218,y=23975..41145,z=-32405..-19771\noff x=-48747..-17911,y=59950..77703,z=-13769..7741\noff x=-63221..-42119,y=-55919..-46750,z=-41421..-40955\noff x=-48375..-12160,y=58420..80105,z=12920..24208\non x=71072..79759,y=21125..45199,z=-4705..13837\non x=-31352..3765,y=-94595..-58769,z=11911..23915\noff x=373..23352,y=-50895..-14246,z=-80217..-59767\noff x=7066..8657,y=-96706..-68072,z=-7088..16509\non x=-51232..-33172,y=-40791..-25520,z=-62034..-40752\non x=14275..34925,y=70729..81904,z=-11351..8996\noff x=-89880..-63344,y=14455..21987,z=10249..18184\noff x=-76665..-73466,y=12389..29268,z=-29705..-3064\non x=50578..55235,y=-58508..-27331,z=-54406..-27657\non x=67746..78075,y=3684..23596,z=-21969..2968\non x=55755..66688,y=-60536..-37192,z=22915..44014\non x=8038..40360,y=24131..50984,z=68348..69876\noff x=-17128..2366,y=27772..43497,z=70368..79928\noff x=54479..74773,y=38588..57296,z=2414..16691\noff x=-66541..-48861,y=-68223..-45601,z=-21915..-3036\noff x=-86928..-67200,y=-16408..13564,z=-33939..-13500\noff x=-13912..2218,y=-17671..6914,z=63164..82953\noff x=22139..35022,y=54579..61399,z=-55684..-38673\noff x=27878..48984,y=62441..82349,z=-22618..14260\non x=-2195..21083,y=-25991..-2907,z=-96165..-70720\noff x=-38732..-21837,y=34725..51044,z=45008..70383\non x=3848..34698,y=-60068..-41642,z=41254..65901\non x=-61361..-44786,y=-28125..-2043,z=31526..57293\noff x=27201..34733,y=52189..89851,z=-42123..-20394\non x=18085..42407,y=1757..15913,z=56541..85108\non x=70893..77546,y=-20167..-13209,z=-30405..-18024\non x=4454..28933,y=62353..92256,z=2598..34484\noff x=-30932..1089,y=-11002..-5461,z=-92865..-61938\noff x=-11090..-863,y=-45056..-29336,z=-79612..-59805\non x=69347..90731,y=-5338..17953,z=23178..51736\non x=-26888..2250,y=-17399..3480,z=-95553..-77039\non x=13758..21604,y=-86651..-55244,z=-31610..-12946\noff x=-2310..22325,y=-73076..-38378,z=47549..72016\noff x=-64760..-34824,y=50715..69108,z=2880..17133\non x=8293..37626,y=209..32795,z=69510..77903\non x=26569..43319,y=-28837..-10605,z=-86283..-46966\non x=15189..48460,y=56103..70924,z=-41507..-18695\non x=50472..60820,y=36325..65141,z=-26465..3805\noff x=71475..84634,y=26761..52226,z=-18788..8211\non x=-15498..2082,y=57975..85738,z=10032..37898\noff x=64853..80096,y=18049..39945,z=-22719..-3021\non x=-89021..-56923,y=-37013..-5501,z=-37752..-3744\noff x=-53563..-34043,y=-13571..7215,z=51654..69601\noff x=73603..95599,y=-29185..-6592,z=-1547..7830\noff x=-89899..-58785,y=-37642..-22122,z=2100..15738\noff x=43149..48403,y=58070..61555,z=12040..21277\noff x=-77570..-44604,y=-38604..-21364,z=-49649..-30121\noff x=-6505..6293,y=-9490..20632,z=67506..99213\non x=-26630..-3114,y=39428..53919,z=-75858..-48317\non x=65580..89172,y=19400..27712,z=-32985..-20804\noff x=3210..27477,y=64531..97346,z=-22032..-12913\non x=-12572..7541,y=-74421..-44890,z=-67529..-47072\noff x=-38799..-33254,y=55740..82233,z=530..24464\noff x=-3034..17204,y=-52009..-41850,z=53550..72208\non x=75041..88551,y=-2920..16778,z=-12397..6561\noff x=-57828..-45910,y=33391..50062,z=42181..58694\noff x=64086..79980,y=-35062..-6655,z=-43005..-19072\non x=46293..78226,y=15332..31724,z=20486..49133\non x=10114..27622,y=-42356..-32617,z=55094..85456\non x=-23675..-15335,y=-96639..-64457,z=-9611..8841\noff x=-66057..-61691,y=29422..39444,z=-52445..-17174\noff x=-2306..21587,y=-70513..-56859,z=48314..55486\non x=-7693..3070,y=-67695..-61179,z=31517..61993\non x=-42386..-29382,y=42216..64356,z=-62841..-35339\noff x=66402..74832,y=-4752..14537,z=-46085..-28352\noff x=55950..79164,y=-33399..-13600,z=-41795..-12050\non x=-39692..-34786,y=-29052..-9990,z=-80147..-56930\noff x=-31315..-9184,y=-14651..13406,z=-91739..-78572\non x=6656..15557,y=48620..68978,z=40125..51661\noff x=-65883..-38294,y=7846..15268,z=-75714..-60704\noff x=-48084..-17849,y=-80014..-57655,z=-14004..20143\noff x=-67339..-29231,y=51852..56222,z=-45946..-29021\noff x=33224..49365,y=-12256..9048,z=-71923..-43893\noff x=11578..19032,y=67746..88200,z=3562..28538\non x=-33943..3274,y=-3303..6385,z=64558..82042\noff x=37371..56283,y=-71559..-59397,z=10223..38124\noff x=-20468..-3133,y=-85638..-66116,z=18723..33699\noff x=-21553..-6096,y=52051..71487,z=-60241..-44696\non x=2119..38578,y=-55284..-16851,z=-81488..-48856\noff x=4121..23554,y=70827..83659,z=13022..29851\noff x=-7658..30282,y=32804..49109,z=-84735..-65886\non x=42254..58740,y=-80327..-54242,z=-2455..13934\non x=36999..55943,y=36056..71034,z=4631..30064\non x=-16744..1244,y=60903..82252,z=-22510..-1874\non x=-47833..-35095,y=62424..78564,z=-43716..-17319\noff x=-13531..-2144,y=-47211..-31550,z=61823..69864\noff x=33638..66759,y=-56079..-22500,z=27700..62584\noff x=17484..53809,y=-82382..-61322,z=-7780..23179\non x=36303..56592,y=-37680..-17563,z=51742..69493\non x=-39105..-21357,y=-20555..-8374,z=65002..74328\n")

(defn parse-instructions [input]
  (let [lines (str/split-lines input)
        parse-line
        (fn [line]
          (let [[op _ & more] (str/split line #"[ =,]|\.\.")
                [x1 x2 _ y1 y2 _ z1 z2] more
                bounds (->> [x1 x2 y1 y2 z1 z2]
                            (map parse-long)
                            (partition 2))]
            (cons op bounds)))]
    (map parse-line lines)))

(def t-instr (parse-instructions t-input))
(def t-instr-large (parse-instructions t-input-large))
(def instr (parse-instructions input))

(defn cells [[x1 x2] [y1 y2] [z1 z2]]
  (for [x (range x1 (inc x2))
        y (range y1 (inc y2))
        z (range z1 (inc z2))]
    [x y z]))

(defn exec-instructions [instructions]
  (reduce (fn [acc [op & bounds]]
            (let [cells (apply cells bounds)
                  f (if (= "on" op) conj disj)]
              (reduce f acc cells)))
          #{}
          instructions))

(defn within-bound? [instruction bound]
  (let [[_ & bounds] instruction]
    (->> (flatten bounds)
         (every? #(<= (- bound) % bound)))))

(defn part-1 [instructions]
  (->> instructions
       (filter #(within-bound? % 50))
       (exec-instructions)
       (count)))

(part-1 instr)






(defn segments-disjoint? [a b]
  (let [[ax1 ax2] a
        [bx1 bx2] b]
    (or (> bx1 ax2) (> ax1 bx2))))

(rcf/tests
  (segments-disjoint? [0 1] [2 3]) := true
  (segments-disjoint? [2 3] [0 1]) := true
  (segments-disjoint? [0 2] [2 3]) := false
  (segments-disjoint? [2 3] [0 2]) := false)

(defn cuboids-disjoint? [a b]
  (boolean
    (some (partial apply segments-disjoint?)
          (map vector a b))))

(rcf/tests
  (cuboids-disjoint? [[0 1] [0 1] [0 1]]
                     [[2 3] [2 3] [2 3]]) := true
  (cuboids-disjoint? [[0 1] [0 1] [0 1]]
                     [[0 3] [0 3] [0 3]]) := false)

(defn sub-segs [a b]
  ;{:post [(every? (fn [[a b]] (<= a b)) %)]}
  (let [[a b] (sort (map vec [a b]))
        [ax1 ax2] a
        [bx1 bx2] b]
    (cond
      (= a b) [a]
      (= ax1 bx1) [[ax1 ax2] [(inc ax2) bx2]]
      ;; Know that ax1 < bx1
      (= ax2 bx2) [[ax1 (dec bx1)] [bx1 bx2]]
      (= ax2 bx1) [[ax1 (dec ax2)] [ax2 ax2] [(inc bx1) bx2]]
      (> ax2 bx2) [[ax1 (dec bx1)] [bx1 bx2] [(inc bx2) ax2]]
      (> ax2 bx1) [[ax1 (dec bx1)] [bx1 ax2] [(inc ax2) bx2]]
      :else (throw (ex-info "No such!" {})))))

(rcf/tests
  (sub-segs [0 1] [0 1]) := [[0 1]]
  (sub-segs [0 1] [0 2]) := [[0 1] [2 2]]
  (sub-segs [0 2] [1 2]) := [[0 0] [1 2]]
  (sub-segs [0 3] [1 2]) := [[0 0] [1 2] [3 3]]
  (sub-segs [0 2] [1 3]) := [[0 0] [1 2] [3 3]]
  (sub-segs [0 2] [2 3]) := [[0 1] [2 2] [3 3]]
  (sub-segs [0 2] [2 2]) := [[0 1] [2 2]]
  ,)

(defn seg-contains? [[ax1 ax2] [bx1 bx2]]
  (and (<= ax1 bx1)
       (>= ax2 bx2)))

(defn cuboid-contains? [a b]
  (every? (partial apply seg-contains?)
          (map vector a b)))

(rcf/tests
  (cuboid-contains? [[0 3] [0 3] [0 3]]
                    [[0 1] [0 1] [0 1]]) := true
  (cuboid-contains? [[0 1] [0 1] [0 1]]
                    [[0 1] [0 1] [0 1]]) := true
  (cuboid-contains? [[0 3] [0 3] [0 3]]
                    [[-1 1] [0 1] [0 1]]) := false)

(defn sub-cuboids [a b]
  (if (cuboids-disjoint? a b)
    [a b]
    (let [[ax ay az] a
          [bx by bz] b
          xs (sub-segs ax bx)
          ys (sub-segs ay by)
          zs (sub-segs az bz)
          boxes (->> (combo/cartesian-product xs ys zs)
                     (filter #(or (cuboid-contains? a %)
                                  (cuboid-contains? b %))))]
      (->> boxes))))
           ;(sort-by first)))))
      ;    perms (combo/permutations boxes)]
      ;(apply min-key
      ;       count
      ;       (->> perms
      ;            (map (partial partition 2))
      ;            (map (fn [partitioned-perm]
      ;                   (mapcat (fn [[a b]]
      ;                             (merge-cuboids a b))
      ;                           partitioned-perm))))))))
      ;(reduce (partial min-key count)
      ;        (->> (combo/permutations boxes)
      ;             (reduce merge-cuboids))))))

(rcf/tests
  (sub-cuboids [[0 1] [0 1] [0 1]]
               [[2 3] [2 3] [2 3]]) := [[[0 1] [0 1] [0 1]]
                                        [[2 3] [2 3] [2 3]]]
  (count (sub-cuboids [[0 1] [0 1] [0 1]]
                      [[0 3] [0 3] [0 3]])) := 8
  (count (sub-cuboids [[-1 1] [0 1] [0 1]]
                      [[0 3] [0 3] [0 3]])) := 9
  (count (sub-cuboids [[-1 1] [-1 1] [0 1]]
                      [[0 3] [0 3] [0 3]])) := 11
  (count (sub-cuboids [[-1 1] [-1 1] [-1 1]]
                      [[0 3] [0 3] [0 3]])) := 15
  "Done"
  ,)

(defn cuboid-on [a b]
  (sub-cuboids a b))

(defn cuboid-off [a b]
  (let [subs (sub-cuboids a b)]
    (filter #(cuboid-contains? b %)
            subs)))


(defn remove-cuboid [cuboids remove-me]
  ;{:post (>= (cub/volume remove-me)
  ;           (- (reduce + (map cub/volume cuboids))
  ;              (reduce + (map cub/volume %))))}
  ;(filter #(cuboids-disjoint? remove-me %)
  ;        cuboids))
  (filter #(do
             (when-not (or (cub/cuboid-contains? remove-me %)
                           (cub/cuboids-disjoint? remove-me %))
               (prn remove-me %)
               (assert false))
             (not (cuboid-contains? remove-me %)))
          cuboids))


(defn execute-instructions [instructions]
  (let [[[on & cuboid] & instructions] (->> instructions
                                            (drop-while (fn [[op]]
                                                          (= "off" op))))]
    (reduce (fn [acc [op & cuboid]]
              (assert (set? acc))
              (let [f (if (= op "off")
                        cub/slow-union
                        cub/fast-union)
                    cs (f (conj acc cuboid))
                    cs (if (= op "off")
                         (remove-cuboid cs cuboid)
                         cs)
                    cs (reduce (fn [acc c]
                                 (let [[old merged] (some #(let [merged (cub/merge-cuboids % c)]
                                                             (when (not= [% c] merged)
                                                               [% merged]))
                                                          acc)]
                                   (if merged
                                     (into (disj acc old) merged)
                                     (conj acc c))))
                               #{}
                               (set cs))]
                (println op cuboid (count cs))
                cs))
            #{cuboid}
            instructions)))
1311612259117092

(execute-instructions [["on" [0 1] [0 1] [0 1]]
                       ["off" [1 1] [1 1] [1 1]]])

(def t-instr-large (parse-instructions "on x=-5..47,y=-31..22,z=-19..33\non x=-44..5,y=-27..21,z=-14..35\non x=-49..-1,y=-11..42,z=-10..38\non x=-20..34,y=-40..6,z=-44..1\noff x=26..39,y=40..50,z=-2..11\non x=-41..5,y=-41..6,z=-36..8\noff x=-43..-33,y=-45..-28,z=7..25\non x=-33..15,y=-32..19,z=-34..11\noff x=35..47,y=-46..-34,z=-11..5\non x=-14..36,y=-6..44,z=-16..29\non x=-57795..-6158,y=29564..72030,z=20435..90618\non x=36731..105352,y=-21140..28532,z=16094..90401\non x=30999..107136,y=-53464..15513,z=8553..71215\non x=13528..83982,y=-99403..-27377,z=-24141..23996\non x=-72682..-12347,y=18159..111354,z=7391..80950\non x=-1060..80757,y=-65301..-20884,z=-103788..-16709\non x=-83015..-9461,y=-72160..-8347,z=-81239..-26856\non x=-52752..22273,y=-49450..9096,z=54442..119054\non x=-29982..40483,y=-108474..-28371,z=-24328..38471\non x=-4958..62750,y=40422..118853,z=-7672..65583\non x=55694..108686,y=-43367..46958,z=-26781..48729\non x=-98497..-18186,y=-63569..3412,z=1232..88485\non x=-726..56291,y=-62629..13224,z=18033..85226\non x=-110886..-34664,y=-81338..-8658,z=8914..63723\non x=-55829..24974,y=-16897..54165,z=-121762..-28058\non x=-65152..-11147,y=22489..91432,z=-58782..1780\non x=-120100..-32970,y=-46592..27473,z=-11695..61039\non x=-18631..37533,y=-124565..-50804,z=-35667..28308\non x=-57817..18248,y=49321..117703,z=5745..55881\non x=14781..98692,y=-1341..70827,z=15753..70151\non x=-34419..55919,y=-19626..40991,z=39015..114138\non x=-60785..11593,y=-56135..2999,z=-95368..-26915\non x=-32178..58085,y=17647..101866,z=-91405..-8878\non x=-53655..12091,y=50097..105568,z=-75335..-4862\non x=-111166..-40997,y=-71714..2688,z=5609..50954\non x=-16602..70118,y=-98693..-44401,z=5197..76897\non x=16383..101554,y=4615..83635,z=-44907..18747\noff x=-95822..-15171,y=-19987..48940,z=10804..104439\non x=-89813..-14614,y=16069..88491,z=-3297..45228\non x=41075..99376,y=-20427..49978,z=-52012..13762\non x=-21330..50085,y=-17944..62733,z=-112280..-30197\non x=-16478..35915,y=36008..118594,z=-7885..47086\noff x=-98156..-27851,y=-49952..43171,z=-99005..-8456\noff x=2032..69770,y=-71013..4824,z=7471..94418\non x=43670..120875,y=-42068..12382,z=-24787..38892\noff x=37514..111226,y=-45862..25743,z=-16714..54663\noff x=25699..97951,y=-30668..59918,z=-15349..69697\noff x=-44271..17935,y=-9516..60759,z=49131..112598\non x=-61695..-5813,y=40978..94975,z=8655..80240\noff x=-101086..-9439,y=-7088..67543,z=33935..83858\noff x=18020..114017,y=-48931..32606,z=21474..89843\noff x=-77139..10506,y=-89994..-18797,z=-80..59318\noff x=8476..79288,y=-75520..11602,z=-96624..-24783\non x=-47488..-1262,y=24338..100707,z=16292..72967\noff x=-84341..13987,y=2429..92914,z=-90671..-1318\noff x=-37810..49457,y=-71013..-7894,z=-105357..-13188\noff x=-27365..46395,y=31009..98017,z=15428..76570\noff x=-70369..-16548,y=22648..78696,z=-1892..86821\non x=-53470..21291,y=-120233..-33476,z=-44150..38147\noff x=-93533..-4276,y=-16170..68771,z=-104985..-24507"))

(prn
  (->> instr
       ;(filter #(within-bound? % 50))
       (execute-instructions)
       (map cub/volume)
       (reduce +)))