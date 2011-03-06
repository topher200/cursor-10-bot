(ns cursor-10.core)

(def offset [448.0 299.0])  ;; top-left pixel of game

(def start-pos
     ;; The position of the start button for the game
     [278 309])

(def crystals
     ;; Vector of vectors of [X Y] coordinates (whooo) with crystal locations.
     ;; Each vector cooresponds to a different floor. Floor counting starts at 1
     [[]
      [[275 278] [163 211] [306 116] [407 184]]
      [[63 187] [279 313] [277 187] [275 64]]
      [] ;; TODO
      []
      [] ;; TODO  ;; floor 5
      [] ;; TODO
      [] ;; TODO
      ])

(def hold-blocks
     [[]
      []
      []
      []
      []
      []  ;; floor 5
      []
      []
      [[310 173]]])

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
      ])

(defn click-point
  ([[x y]] (click-point [x y] 20))
  ([[orig-x orig-y] sleep-time]
     (let [[x y] (map + [orig-x orig-y] offset)]
       (dorun [(println orig-x orig-y)
               (.mouseMove (java.awt.Robot.) x y)
               (Thread/sleep 20) ;; TODO(topher): replace
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

(defn hold-floor-block [floor]
  ;; assumes there is one hold-block per floor
  (click-point (first (nth hold-blocks floor)) 15000))

(defn rapidly-click-on-box [floor]
     (dorun (repeatedly 99 #(click-point (first (nth ladders floor))))))

(defn clear-floor-and-move-up [floor]
  (dorun [(clear-floor floor) (move-up floor)]))

(defn move-to-floor [start destination]
  (doseq [floor (take (dec destination) (iterate inc start))] (move-up floor)))

(defn run-cursor [num]
  (case num
        1 (dorun  ;; move to 8, clicking 4 twice. hold block on 8
           [(move-to-floor 1 5) (move-to-floor 4 8) (hold-floor-block 8)])
        2 (dorun  ;; move to 10, click rapidly on box
           [(move-to-floor 1 10) (rapidly-click-on-box 10)])))

(defn run-game []
  (dorun [(click-point start-pos)
          (Thread/sleep 1500)
          (doseq [cursor (take 1 (iterate inc 1))]
            (run-cursor cursor))]))
