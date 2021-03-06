(ns cursor-10.core)

(def offset [448 299])  ;; set to top-left pixel of game window

(def start-pos
     ;; The position of the start button for the game
     [278 309])

(def crystals
     ;; Vector of vectors of [X Y] coordinates (whooo) with crystal locations.
     ;; Each vector cooresponds to a different floor. Floor counting starts at 1
     [[]
      [[275 278] [163 211] [306 116] [407 184]]
      [[63 187] [279 313] [277 187] [275 64]]
      [[164 261] [271 190] [324 157] [377 126]]
      []
      [[276 321] [45 189] [267 59] [499 200]]  ;; floor 5
      [[261 65] [211 92] [149 126] [97 158] [37 194]]
      [[262 62] [267 181] [47 191] [494 191] [265 322]]
      [[262 58] [237 121] [154 170] [485 189] [361 192] [290 241] [279 322]
       [36 193]]
      [[138 250] [267 318] [266 192] [488 190] [405 251] [45 195]]
      [[261 56] [242 94] [351 125] [169 160] [383 178] [35 191] [491 193]
       [183 229] [344 233] [268 256] [265 322]] ;; floor 10
      [[328 120] [266 122] [197 123] [195 175] [329 178] [192 241] [330 241]
       [259 244]]
      [[263 60] [265 123] [263 183] [266 248] [265 320]]
      [[291 126] [244 133] [337 145] [216 167] [279 179] [349 185] [208 200]
       [323 222] [231 225] [278 238]]
      [[260 104] [201 142] [139 181] [378 186] [262 187] [83 218] [311 222]
       [34 189] [442 231] [500 196]]
      [[366 118] [163 121] [145 253] [401 254]]  ;; floor 15
      [[234 145]]
      ])

(def hold-blocks
     [[]
      []
      []
      []
      []
      []  ;; floor 5
      [[274 325]]
      []
      [[310 173]]
      []
      []  ;; floor 10
      []
      []
      []
      []
      [[44 195] [270 66] [499 196]]  ;; floor 15
      ])

(def ladders
     ;; Vector of vectors of [X Y] coordinates for every possible place for
     ;; the ladder on each level. Floor counting starts at 1.
     [[]
      [[171 121]]
      [[508 189]]
      [[227 217]]
      [[189 248] [93 194] [160 177] [191 128] [274 74] [272 121] [282 178]
       [277 240] [273 288] [367 237] [373 178] [350 132] [451 192]]
      [[227 223]]  ;; floor 5
      [[398 247]]
      [[223 228]]
      [[374 122]]
      [[171 117]]
      [[274 176]]  ;; floor 10
      [[502 188]]
      [[41 190]]
      [[271 57]]
      [[306 298]]
      [[270 180]]  ;; floor 15
      ])

(defn click-point
  ([[x y]] (click-point [x y] 20))
  ([[orig-x orig-y] sleep-time]
     (let [[x y] (map + [orig-x orig-y] offset)]
       (dorun [(println orig-x orig-y)
               (.mouseMove (java.awt.Robot.) x y)
               (Thread/sleep 20)
               (.mousePress (java.awt.Robot.)
                            (.. java.awt.event.InputEvent BUTTON1_MASK))
               (Thread/sleep sleep-time)
               (.mouseRelease (java.awt.Robot.)
                              (.. java.awt.event.InputEvent BUTTON1_MASK))
               ]))))

(defn click-points [seq]
  (doseq [point seq] (click-point point)))

(defn clear-floor [floor]
  (click-points (nth crystals floor)))

(defn move-up [floor]
  (click-points (nth ladders floor)))

(defn hold-floor-block
  ([floor] (hold-floor-block floor 0))
  ([floor block-num]
     (click-point (nth (nth hold-blocks floor) block-num) 12000)))

(defn rapidly-click-on-box [floor]
     (dorun (repeatedly 99 #(click-point (first (nth ladders floor))))))

(defn clear-floor-and-move-up [floor]
  (dorun [(clear-floor floor) (move-up floor)]))

(defn run-function-to-floor [function start destination]
  (doseq [floor (take (dec destination) (iterate inc start))] (function floor)))

(defn move-to-floor [start destination]
  (run-function-to-floor move-up start destination))

(defn clear-and-move-to-floor [start destination]
  (run-function-to-floor clear-floor-and-move-up start destination))

(defn run-cursor [num]
  (case num
        1 (dorun  ;; move to 8, clicking 4 twice. hold block on 8
           [(move-to-floor 1 5) (move-to-floor 4 8) (hold-floor-block 8)])
        (2 3) (dorun  ;; move to 10, click rapidly on box
           [(move-to-floor 1 10) (rapidly-click-on-box 10)])
        4 (dorun  ;; move to 15, hold block #0 on 15
           [(move-to-floor 1 15) (hold-floor-block 15 0)])
        5 (dorun  ;; move to 15, hold block #1 on 15
           [(move-to-floor 1 15) (hold-floor-block 15 1)])
        6 (dorun  ;; move to 15, hold block #2 on 15
           [(move-to-floor 1 15) (hold-floor-block 15 2)])
        7 (dorun  ;; move to 6, hold block on box
           [(move-to-floor 1 6) (hold-floor-block 6)])
        8 (dorun  ;; clear all crystals to 16
           [(clear-and-move-to-floor 1 16) (clear-floor 16)])
        ))
