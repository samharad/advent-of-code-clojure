;; Here it is, March 2022, and I'm working on Day 19
;; of last year's AoC.

(ns aoc.2021.day.19
  (:require [clojure.spec.alpha :as s]
            [hyperfiddle.rcf :as rcf]
            [orchestra.spec.test :as st]
            [clojure.string :as str]
            [clojure.math.combinatorics :as combo]
            [clojure.set :as set]))

(rcf/enable!)

;; The problem deals with Beacons and Scanners. Beacons are points in 3D cartesian space that
;; emit a signal; a Scanner is a fixed point which detects these Beacon signals within a 500-unit
;; "Manhattan radius" and records their locations relative to the Scanner.

;; Our input is a list of Scanners and a locations of the Beacons which each detects:

(def test-input "--- scanner 0 ---
404,-588,-901
528,-643,409
-838,591,734
390,-675,-793
-537,-823,-458
-485,-357,347
-345,-311,381
-661,-816,-575
-876,649,763
-618,-824,-621
553,345,-567
474,580,667
-447,-329,318
-584,868,-557
544,-627,-890
564,392,-477
455,729,728
-892,524,684
-689,845,-530
423,-701,434
7,-33,-71
630,319,-379
443,580,662
-789,900,-551
459,-707,401

--- scanner 1 ---\n686,422,578\n605,423,415\n515,917,-361\n-336,658,858\n95,138,22\n-476,619,847\n-340,-569,-846\n567,-361,727\n-460,603,-452\n669,-402,600\n729,430,532\n-500,-761,534\n-322,571,750\n-466,-666,-811\n-429,-592,574\n-355,545,-477\n703,-491,-529\n-328,-685,520\n413,935,-424\n-391,539,-444\n586,-435,557\n-364,-763,-893\n807,-499,-711\n755,-354,-619\n553,889,-390\n\n--- scanner 2 ---\n649,640,665\n682,-795,504\n-784,533,-524\n-644,584,-595\n-588,-843,648\n-30,6,44\n-674,560,763\n500,723,-460\n609,671,-379\n-555,-800,653\n-675,-892,-343\n697,-426,-610\n578,704,681\n493,664,-388\n-671,-858,530\n-667,343,800\n571,-461,-707\n-138,-166,112\n-889,563,-600\n646,-828,498\n640,759,510\n-630,509,768\n-681,-892,-333\n673,-379,-804\n-742,-814,-386\n577,-820,562\n\n--- scanner 3 ---\n-589,542,597\n605,-692,669\n-500,565,-823\n-660,373,557\n-458,-679,-417\n-488,449,543\n-626,468,-788\n338,-750,-386\n528,-832,-391\n562,-778,733\n-938,-730,414\n543,643,-506\n-524,371,-870\n407,773,750\n-104,29,83\n378,-903,-323\n-778,-728,485\n426,699,580\n-438,-605,-362\n-469,-447,-387\n509,732,623\n647,635,-688\n-868,-804,481\n614,-800,639\n595,780,-596\n\n--- scanner 4 ---\n727,592,562\n-293,-554,779\n441,611,-461\n-714,465,-776\n-743,427,-804\n-660,-479,-426\n832,-632,460\n927,-485,-438\n408,393,-506\n466,436,-512\n110,16,151\n-258,-428,682\n-393,719,612\n-211,-452,876\n808,-476,-593\n-575,615,604\n-485,667,467\n-680,325,-822\n-627,-443,-432\n872,-547,-609\n833,512,582\n807,604,487\n839,-516,451\n891,-625,532\n-652,-548,-490\n30,-46,-14\n")

;; Feeling more comfortable with spec now, I thought I'd make some quick specs
;; if only to give my program some structure off the bat.

(s/def ::coord (s/coll-of integer? :kind vector? :count 3))
(s/def ::beacon ::coord)
(s/def ::scanner (s/coll-of ::beacon))

;; We have coordinates, beacons (just coordinates), and scanners (collections of beacons);
;; the very minimal data useful to represent our domain. (Later, I'll wish I modeled Scanners
;; as proper objects, because I want to track not only a Scanner's detected beacons but also
;; its inferred location.)

(s/fdef parse-scanner
  :args (s/cat :input string?)
  :ret ::scanner)
(defn parse-scanner [input]
  (let [parse-beacon (fn [line]
                       (->> (str/split line #",")
                            (mapv parse-long)))]
    (->> input
         (str/split-lines)
         (rest)
         (map parse-beacon))))

(defn parse-scanners [input]
  (let [scanners (str/split input #"\n\n")]
    (map parse-scanner scanners)))

(def test-scanners (parse-scanners test-input))
test-scanners

(def input "--- scanner 0 ---\n-383,494,-722\n416,680,-782\n702,-466,-370\n-497,489,-749\n393,371,887\n346,-812,668\n-331,-537,-634\n449,549,858\n-694,757,676\n437,492,926\n-449,-615,-701\n32,70,131\n-588,-759,491\n-386,-503,-743\n652,-482,-490\n390,-826,736\n430,721,-642\n285,-788,616\n-468,470,-620\n-613,-685,381\n-660,-625,438\n-630,787,857\n-557,827,723\n863,-442,-469\n-28,-51,-26\n408,557,-671\n\n--- scanner 1 ---\n677,636,670\n656,-350,616\n-728,663,-297\n-32,3,107\n841,-531,-758\n-580,681,526\n459,754,-538\n-553,719,488\n-782,-365,-466\n-606,-593,510\n-649,-711,555\n670,-414,-760\n600,620,815\n-729,-408,-463\n557,763,718\n-706,534,-323\n-599,589,-299\n352,693,-536\n-528,-692,530\n742,-264,668\n383,795,-651\n-558,700,724\n-733,-555,-430\n729,-403,-704\n629,-304,760\n\n--- scanner 2 ---\n845,-525,424\n-297,-724,-404\n705,475,441\n-528,746,327\n-492,-491,586\n947,-315,-543\n-461,981,-665\n791,897,-789\n3,172,-165\n874,-473,313\n620,428,554\n832,-294,-425\n975,-455,373\n-441,924,-720\n-324,782,279\n702,845,-727\n-359,747,386\n-520,983,-860\n725,-283,-536\n-352,-703,-475\n-225,-758,-558\n-520,-616,579\n147,56,-44\n741,756,-805\n-495,-606,635\n596,442,457\n\n--- scanner 3 ---\n622,-463,380\n-743,811,559\n-553,-625,-725\n-712,-775,791\n-606,741,606\n308,825,-732\n559,396,526\n-116,97,136\n391,880,-859\n693,-861,-564\n724,-486,471\n804,-521,414\n237,846,-787\n553,-802,-643\n-3,62,-58\n498,-859,-623\n-746,738,-564\n-802,728,-589\n-640,-641,844\n-753,613,-592\n-670,771,425\n583,406,434\n486,438,391\n-446,-660,-760\n-687,-701,901\n-494,-624,-556\n\n--- scanner 4 ---\n567,-413,291\n37,98,16\n-800,-787,481\n526,657,578\n477,542,575\n-470,737,-438\n-431,-442,-685\n785,-491,-858\n-684,-728,597\n673,529,-620\n815,-484,-858\n-639,695,814\n-451,-451,-797\n533,532,550\n-432,-316,-808\n-458,794,-370\n-831,-821,647\n-79,12,-95\n732,-440,275\n-414,915,-409\n616,-441,274\n-638,738,727\n781,-444,-808\n536,459,-573\n609,499,-439\n-561,620,712\n\n--- scanner 5 ---\n-140,-57,-54\n425,-555,-443\n310,409,-652\n-517,-532,-583\n572,-681,406\n396,402,737\n-609,570,-783\n-529,449,793\n-473,499,737\n-811,550,-772\n-801,-733,410\n591,-703,604\n567,408,750\n-474,-479,-586\n351,534,-724\n-702,-655,390\n610,-708,368\n480,-662,-419\n406,436,710\n363,489,-606\n-729,494,-777\n-388,408,788\n-664,-528,-550\n554,-533,-417\n-820,-612,435\n\n--- scanner 6 ---\n-616,792,-391\n782,634,-497\n502,520,554\n-321,668,572\n-562,-519,481\n694,-617,472\n464,493,516\n-32,76,-91\n-609,792,-421\n-347,646,701\n-660,-341,-474\n726,731,-470\n646,-737,599\n558,-608,-547\n615,-553,-532\n-656,742,-425\n-560,-412,542\n28,-105,80\n502,-626,-642\n791,-660,615\n791,611,-361\n-659,-340,-590\n700,498,535\n-323,539,646\n-687,-357,-639\n-675,-443,440\n161,-73,-77\n\n--- scanner 7 ---\n-109,-33,-138\n-446,-451,-456\n620,685,-876\n598,908,-874\n519,424,832\n-433,-622,-462\n-777,-627,627\n-395,333,-462\n670,-664,-450\n-446,418,-553\n-390,474,-503\n376,380,825\n-30,102,33\n431,512,825\n-815,-665,654\n-473,-461,-490\n-579,-646,649\n-748,765,558\n526,-619,-524\n601,-645,-659\n500,-484,650\n626,863,-810\n82,-87,20\n-793,647,601\n-773,746,679\n459,-435,585\n462,-323,552\n\n--- scanner 8 ---\n618,-575,-405\n-381,637,514\n491,606,-350\n562,358,799\n-451,500,561\n616,-514,-512\n519,557,-333\n-421,564,380\n569,388,799\n-454,640,-609\n-312,-497,-904\n-786,-689,389\n-367,-414,-777\n522,392,749\n-754,-592,346\n518,441,-334\n503,-473,-436\n855,-679,546\n-796,-665,481\n857,-605,675\n-346,-343,-832\n831,-734,653\n-41,-90,6\n-579,647,-637\n109,51,-42\n-464,696,-634\n\n--- scanner 9 ---\n-314,556,-748\n-267,471,-635\n686,696,-765\n-645,-822,801\n7,-157,-17\n616,826,-816\n614,-405,562\n571,-492,561\n689,-507,484\n891,372,738\n-317,645,635\n-585,-677,-862\n-621,-473,-910\n634,725,-876\n767,-644,-554\n861,513,736\n652,-641,-603\n777,-669,-610\n-312,560,656\n943,354,759\n145,-57,-102\n-446,592,673\n-281,447,-732\n-524,-869,808\n-586,-508,-803\n-600,-860,658\n\n--- scanner 10 ---\n-988,-745,421\n-980,-744,-811\n-665,492,-838\n-542,366,-837\n492,695,687\n530,683,606\n-954,-791,561\n588,-515,-818\n586,-670,669\n-553,501,-818\n-912,-687,-799\n-116,-67,-160\n350,448,-681\n538,647,514\n-905,-659,547\n728,-556,-832\n-78,92,-55\n454,466,-522\n617,-679,-840\n-595,824,551\n-941,-865,-855\n555,497,-680\n-585,760,507\n630,-709,574\n-599,694,575\n447,-716,575\n\n--- scanner 11 ---\n-711,-575,-794\n-156,149,-57\n-743,741,442\n-458,462,-501\n-868,-585,847\n729,801,780\n-604,-630,-770\n360,-745,-561\n-508,496,-461\n-458,492,-508\n-776,-561,857\n-696,604,512\n435,687,-637\n440,-692,-607\n485,-704,-489\n702,915,775\n401,652,-520\n714,-335,660\n600,830,799\n793,-255,639\n-911,-488,815\n-582,-501,-796\n332,649,-669\n739,-314,471\n34,49,76\n-877,668,482\n\n--- scanner 12 ---\n-838,565,701\n-546,-597,465\n-740,412,-705\n-108,-126,-79\n560,-903,832\n-727,442,709\n-629,-889,-525\n488,-523,-674\n-696,451,-800\n-525,-557,254\n-397,-903,-512\n-585,-928,-532\n-557,405,-706\n870,377,680\n844,330,-692\n448,-573,-796\n878,376,-552\n38,11,-147\n795,462,-678\n612,-927,771\n802,434,668\n-426,-581,415\n831,256,724\n405,-519,-855\n680,-941,727\n-698,606,687\n\n--- scanner 13 ---\n587,-791,-805\n-809,359,-688\n804,480,332\n663,835,-671\n790,548,288\n-139,-63,-131\n-570,-807,559\n22,15,-23\n646,-477,799\n622,-826,-912\n-688,-792,482\n-776,-630,-612\n-766,-655,-556\n766,600,227\n601,-439,656\n-849,493,-615\n-393,329,335\n-613,-894,560\n-571,358,391\n-779,540,-752\n-514,290,379\n737,725,-646\n525,-430,806\n586,-622,-864\n-651,-658,-605\n603,778,-718\n\n--- scanner 14 ---\n777,-631,-530\n404,638,-524\n-355,-863,668\n-342,372,-583\n-324,-897,502\n-516,619,335\n418,679,-439\n-594,544,437\n-340,342,-442\n495,-488,720\n654,571,716\n-562,-473,-754\n404,670,-474\n535,-507,799\n-319,290,-424\n-471,-804,552\n935,-755,-518\n-651,-409,-634\n734,633,703\n812,-740,-434\n507,-601,636\n745,545,618\n-517,662,422\n-599,-478,-698\n58,-27,-84\n\n--- scanner 15 ---\n-419,-744,-730\n315,366,792\n401,-845,-723\n336,391,603\n331,-885,845\n582,345,-350\n-525,-726,-681\n-966,491,-543\n-468,-630,898\n-556,-680,911\n618,491,-413\n646,489,-360\n-533,-660,-814\n-431,-739,840\n-911,382,-509\n256,312,624\n336,-902,616\n-848,424,-621\n-102,17,130\n-829,505,464\n472,-754,-723\n354,-914,713\n-166,-96,-55\n-914,403,488\n574,-860,-793\n-800,376,483\n\n--- scanner 16 ---\n492,867,644\n-590,684,-533\n594,898,529\n617,-476,-857\n541,-359,-848\n-422,969,629\n596,-320,-953\n-538,970,628\n107,80,-148\n-809,-707,-763\n-415,-598,524\n-578,-746,-743\n818,-651,584\n-441,-733,513\n-480,757,-646\n655,-683,527\n-587,953,656\n-600,-626,-766\n-45,118,-24\n-508,-693,464\n524,693,-725\n746,-605,412\n651,573,-677\n-495,702,-589\n533,791,531\n667,650,-822\n\n--- scanner 17 ---\n-622,-716,-525\n640,427,420\n9,33,65\n-729,-671,-517\n886,473,-533\n604,573,483\n-321,880,679\n891,-803,-775\n-568,-638,-457\n868,-832,-643\n-395,858,651\n-385,906,587\n-544,895,-607\n-492,892,-694\n-476,919,-493\n576,571,430\n179,-49,119\n103,144,-33\n821,-763,793\n-491,-309,453\n907,448,-722\n-453,-372,499\n-511,-339,461\n886,-820,-546\n966,394,-554\n648,-778,863\n750,-672,793\n\n--- scanner 18 ---\n865,-600,535\n376,624,313\n-691,-811,-859\n-638,-448,652\n493,-761,-450\n366,-678,-395\n834,-702,420\n-503,513,-828\n758,-661,533\n-716,-539,537\n688,630,-470\n-596,795,771\n547,507,311\n488,496,367\n92,90,-142\n658,517,-488\n-29,-25,-50\n-542,499,-626\n831,572,-510\n-569,405,-789\n-579,856,677\n-806,-693,-838\n-584,-700,-870\n438,-832,-429\n-684,-376,480\n-549,693,709\n\n--- scanner 19 ---\n675,-900,-912\n576,258,461\n-899,-562,-885\n455,437,-847\n402,308,-809\n-944,-505,641\n-926,-448,649\n-682,-563,-880\n-857,-416,731\n693,-952,-933\n-162,-132,-84\n-677,486,-526\n534,-870,-910\n487,303,-830\n351,-695,591\n221,-625,642\n600,272,558\n-675,381,-672\n-606,303,477\n-772,-583,-734\n491,305,574\n-29,-7,-148\n-803,353,513\n-817,299,557\n-729,412,-553\n322,-643,702\n\n--- scanner 20 ---\n-613,561,508\n-100,88,2\n340,478,-606\n-853,596,-397\n539,926,512\n-804,618,-339\n778,-305,-582\n374,-449,416\n-598,-568,835\n642,809,433\n-740,-602,772\n-735,-611,732\n-817,-723,-368\n328,-416,462\n7,-51,101\n-843,743,-319\n-497,516,601\n613,726,532\n698,-450,-647\n260,619,-552\n359,-510,505\n-851,-705,-456\n329,556,-657\n-568,618,535\n743,-393,-475\n-826,-788,-374\n\n--- scanner 21 ---\n375,419,-292\n674,-791,526\n391,714,549\n-817,441,446\n-787,582,448\n386,629,528\n-869,423,425\n598,-800,376\n-615,-438,700\n536,491,-321\n-489,473,-298\n-526,-704,-628\n-540,-336,622\n-33,90,152\n819,-619,-675\n863,-590,-709\n-480,438,-312\n520,661,511\n-588,-717,-548\n-521,523,-298\n860,-747,-757\n-497,-423,623\n-614,-619,-639\n719,-753,383\n84,-46,-4\n394,426,-413\n\n--- scanner 22 ---\n-642,-484,-438\n-402,772,487\n-436,614,-512\n601,-445,511\n783,-655,-548\n715,606,791\n862,-639,-392\n-362,569,-656\n673,798,-445\n-767,-340,-444\n640,910,-448\n504,-418,567\n-374,611,447\n742,568,829\n737,587,593\n-376,-647,582\n-368,782,460\n624,-309,502\n-338,-708,581\n-571,588,-587\n-33,-8,-3\n-548,-398,-462\n134,-52,125\n839,-612,-550\n109,106,-60\n-337,-640,744\n738,749,-486\n\n--- scanner 23 ---\n635,-783,-873\n675,453,-812\n-765,-843,-692\n686,-730,260\n339,802,466\n398,686,582\n-700,-734,-812\n-532,-657,527\n-647,599,374\n685,-614,259\n-587,661,-647\n642,-813,-873\n-756,-707,-715\n-567,556,-786\n-694,550,423\n-476,-562,545\n810,434,-823\n-14,-82,-128\n-666,557,-677\n682,-779,-823\n305,833,632\n713,-565,276\n-790,463,379\n-589,-672,472\n684,433,-700\n\n--- scanner 24 ---\n-392,386,774\n801,578,-529\n795,458,-453\n-775,658,-408\n-695,-515,475\n481,-728,-398\n-372,485,696\n601,670,573\n44,12,93\n496,-789,-309\n-613,-627,-625\n450,-822,-355\n900,-933,689\n865,-845,582\n568,523,569\n-729,-533,563\n-618,-636,-665\n-704,-450,481\n604,713,520\n820,-788,754\n790,376,-509\n-476,-670,-728\n-719,714,-458\n-770,799,-367\n-417,367,653\n\n--- scanner 25 ---\n-422,723,-670\n-270,-861,454\n908,-516,-528\n789,426,539\n-231,-790,395\n-569,724,716\n723,355,437\n862,-470,-495\n552,598,-681\n-516,-733,-761\n-630,-734,-906\n-402,646,-597\n-590,-802,-814\n-464,682,738\n792,-755,647\n-578,621,690\n126,-69,31\n-22,-130,-131\n891,-784,730\n-335,619,-755\n816,471,482\n-261,-763,486\n592,593,-690\n489,456,-647\n812,-681,847\n819,-501,-589\n\n--- scanner 26 ---\n808,541,-518\n-571,511,-513\n861,587,-438\n617,352,504\n580,-669,674\n-533,479,-410\n-654,-680,-439\n765,-775,-465\n-316,-760,561\n109,-91,-38\n-380,687,430\n604,-815,-437\n-452,-751,538\n-346,647,380\n-715,-681,-401\n-647,-518,-400\n-56,3,-86\n775,-802,-431\n-425,-695,441\n62,63,74\n746,378,526\n599,-471,652\n561,-433,679\n820,659,-563\n-536,628,-479\n691,354,633\n-376,640,380\n\n--- scanner 27 ---\n431,368,347\n508,-632,432\n-18,-140,-167\n439,615,379\n913,-774,-641\n-466,-506,-752\n-709,486,769\n877,788,-560\n-267,703,-717\n932,724,-481\n-280,709,-511\n-391,-754,822\n-662,407,691\n-540,455,807\n-369,777,-653\n-427,-601,-796\n840,844,-476\n504,-451,349\n874,-781,-685\n-420,-600,-613\n558,-510,392\n-345,-614,788\n888,-794,-453\n-477,-628,739\n38,-45,21\n424,444,395\n\n--- scanner 28 ---\n-112,40,77\n513,-741,-476\n64,-38,169\n-738,576,870\n366,442,769\n-560,-628,610\n-863,821,-269\n75,100,2\n-615,-685,625\n-796,375,863\n555,495,-668\n-667,-452,-537\n491,-543,-457\n-877,799,-375\n-633,-741,720\n-758,480,737\n-744,-597,-521\n724,-517,548\n389,600,742\n588,498,-780\n604,-452,439\n490,536,-619\n445,598,785\n486,-779,-438\n-624,-549,-394\n627,-461,575\n-778,827,-282\n\n--- scanner 29 ---\n722,-392,-617\n-814,-432,-624\n837,504,801\n-760,437,-585\n810,509,915\n-803,469,-488\n708,485,830\n-668,427,-561\n722,-522,967\n-769,802,668\n-723,-455,-726\n868,-329,-581\n901,645,-255\n-403,-744,710\n847,813,-314\n-293,-670,685\n831,-488,912\n-635,842,741\n-588,772,604\n-780,-490,-806\n96,8,173\n724,-339,-645\n789,-600,849\n15,128,70\n822,624,-335\n-350,-610,638\n\n--- scanner 30 ---\n813,401,806\n740,-455,648\n-654,-721,505\n-751,433,-565\n-153,37,-24\n733,-608,-632\n-858,494,-567\n-827,392,-452\n-629,-777,515\n-930,-638,-468\n691,718,-512\n-66,-64,95\n638,-515,-572\n698,283,827\n-486,333,418\n-359,416,363\n-913,-687,-440\n-626,-728,630\n773,-541,-583\n-929,-522,-467\n645,646,-684\n591,394,838\n-431,262,397\n841,-515,666\n711,-566,596\n723,555,-569\n")
(def scanners (parse-scanners input))

;; Now, the problem is twofold:
;; * The locations of the Scanners are not known.
;; * The Scanners are oriented in different directions;
;;   they don't agree which way is up or forward.

;; We're told that if 12 Beacons are 'shared' between two Scanners --
;; that is, if two Scanners can be oriented in such a way that they detect
;; what seem to be the same 12 Beacons -- we should assume that those
;; 12 Beacons are indeed identical.

;; The question is: how many distinct Beacons are there?

;; So -- we need to be able to 'reorient' a Scanner in different directions,
;; namely, in the 6 directions that it can face and, in each of those directions,
;; the 4 directions that can be 'up'. This yields 24 orientations.

;; There must be a slick way to do this, involving a subset of combinations
;; of `[x y z -x -y -z]`, but I couldn't figure it; aided by a couple cardboard
;; boxes, I ended up enumerating the ways which a coordinate could be 'reoriented':

(s/fdef orientations
  :args (s/cat :coord ::coord)
  :ret (s/coll-of ::coord :count 24))
(defn orientations [coord]
  (let [[x y z] coord
        [-x -y -z] [(- x) (- y) (- z)]]
    [; Y facing Y
     [x y z]
     [z y -x]
     [-x y -z]
     [-z y x]
     ; X facing Y
     [y -x z]
     [y -z -x]
     [y x -z]
     [y z x]
     ; -Y facing Y
     [-x -y z]
     [-z -y -x]
     [x -y -z]
     [z -y x]
     ; -X facing Y
     [-y x z]
     [-y z -x]
     [-y -x -z]
     [-y -z x]
     ; Z facing Y
     [z x y]
     [x -z y]
     [-z -x y]
     [-x z y]
     ; -Z facing Y
     [z -x -y]
     [-x -z -y]
     [-z x -y]
     [x z -y]]))

(st/instrument)
(rcf/tests
  (count (distinct (orientations [1 2 3]))) := 24)

(s/fdef scanner-orientations
  :args (s/cat :coords ::scanner)
  :ret (s/coll-of ::scanner
                  :count 24)
  :fn (fn [{:keys [args ret]}]
        (every? #(= (count (:coords args))
                    (count %))
                ret)))
(defn scanner-orientations [coords]
  (->> coords
       (map orientations)
       (apply map vector)))

(st/instrument)
(rcf/tests
  (sequential? (scanner-orientations [[1 2 3] [4 5 6]])) := true)

;; Now for a few helper functions:

(defn dist [coord-a coord-b]
  (mapv - coord-b coord-a))

(defn translate [coord ds]
  (mapv + coord ds))

(defn manhattan-distance [a b]
  (->> (map - a b)
       (map abs)
       (reduce +)))

(defn num-shared [scanner-a scanner-b]
  (count (set/intersection (set scanner-a) (set scanner-b))))

(rcf/tests
  (num-shared [[1 1 1] [3 3 3] [4 4 4]] [[4 4 4] [1 1 1] [2 2 2]]) := 2)


;; Now to solve the problem. First, let's write a function that takes two
;; scanners and, assuming that those scanners are oriented the same way,
;; returns their distance if they can be found to overlap.

;; The approach will be to brute-forcedly attempt to align each pair of
;; beacons and see if at least 11 other pairs fall in line.

(defn scanner-dist [scanner-a scanner-b]
  (let [[sorted-a sorted-b] (map sort [scanner-a scanner-b])
        candidate-distances (->> (combo/cartesian-product sorted-a sorted-b)
                                 (map (fn [[a b]] (dist b a)))
                                 (distinct))]
    (first (for [d candidate-distances
                 :let [translated-b (map #(translate % d) sorted-b)
                       do-overlap (>= (num-shared sorted-a translated-b) 12)]
                 :when do-overlap]
             d))))

;; Now, for a pair of scanners, we simply run the above function against
;; all 24 orientations a scanner, and see if one hits.

(defn adjusted-scanner
  "If scanner-b can be oriented such that it seems to align with
  the given beacons, returns the inferred location of the scanner
  and its adjusted beacons."
  [beacons scanner-b]
  (let [orientations (scanner-orientations scanner-b)]
    (some (fn [oriented-b]
            (when-let [d (scanner-dist beacons oriented-b)]
              (let [new-beacons (map #(translate % d) oriented-b)]
                [d new-beacons])))
          orientations)))

;; (Returning a tuple of related data as opposed to, say, a self-describing
;; hash map, is not super legible; not worth fixing now, though!)

;; Now the final piece. The problem implies that all scanners will ultimately
;; be part of a single connected graph, so we take our head scanner as the
;; ground truth, and add all its beacons to a set of `beacons`. We recursively
;; use our bank of known beacons to test each un-incorporated scanner and
;; see if it can be brought into the fold.

(defn all-scanners-beacons [scanners]
  (let [[true-scanner & more-scanners] scanners]
    (loop [scanner-locations (set [[0 0 0]])
           beacons (set true-scanner)
           scanners more-scanners]
      (let [; tuples of [scanner adjusted-scanner], i.e. [scanner [loc adjusted-beacons]]
            scanner-adjusted-scanners (map (juxt identity (partial adjusted-scanner beacons))
                                           scanners)
            scanner-beacons (map #(update % 1 second) scanner-adjusted-scanners)
            new-beacons (->> (mapcat second scanner-beacons)
                             (filter identity)
                             (filter (complement beacons)))
            new-scanner-locations (->> scanner-adjusted-scanners
                                       (map second)
                                       (map first)
                                       (keep identity))
            unmatched-scanners (->> scanner-beacons
                                    (filter #(nil? (second %)))
                                    (map first))]
        (if (not-empty new-beacons)
          (recur (into scanner-locations new-scanner-locations)
                 (into beacons new-beacons)
                 unmatched-scanners)
          [scanner-locations beacons])))))

(defn max-distance [scanner-locs]
  (->> (combo/combinations scanner-locs 2)
       (map #(apply manhattan-distance %))
       (reduce max)))

(let [[test-scanner-locs test-beacons] (all-scanners-beacons test-scanners)]
  (rcf/tests
    (count test-beacons) := 79
    (max-distance test-scanner-locs) := 3621))

(comment
  (let [[scanner-locs beacons] (time (all-scanners-beacons scanners))]  ; => 153979.417959 msecs"
    (rcf/tests
      (time (count beacons) := 372)
      (time (max-distance scanner-locs))) := 12241))

;; At 2.5 minutes runtime it's nothing to write home about, but then again
;; it's March and time to leave Dec. 2021 behind!

;; ⭐️⭐️






