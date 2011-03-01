(ns cursor-10.core)

(def sleep-time 100)

(def start-pos
     ;; The position of the start button for the game
     [726 608])

(def crystals
     ;; Vector of vectors of [X Y] coordinates (whooo) with crystal locations.
     ;; Each vector cooresponds to a different floor. Floor counting starts at 1
     [[]
      [[723 577] [611 510] [754 415] [855 483]]
      [[511 486] [727 612] [725 486] [723 363]]
      [] ;; TODO
      []
      [] ;; TODO
      [] ;; TODO
      [] ;; TODO
      ])

(def ladders
     ;; Vector of vectors of [X Y] coordinates for every possible place for
     ;; the ladder on each level. Floor counting starts at 1.
     [[]
      [[619 420]]
      [[956 488]]
      [[675 516]]
      [[637 547] [541 493] [608 476] [639 427] [722 373] [720 420] [730 477]
       [725 539] [721 587] [815 536] [821 477] [798 431] [899 491]]
      [[675 522]]
      [[846 546]]
      [[671 527]]
      ])

(defn click-point
  ([[x y]]
     (dorun [(println x y)
             (.mouseMove (java.awt.Robot.) x y)
             (Thread/sleep sleep-time)
             (.mousePress (java.awt.Robot.)
                          (.. java.awt.event.InputEvent BUTTON1_MASK))
             (Thread/sleep sleep-time)
             (.mouseRelease (java.awt.Robot.)
                          (.. java.awt.event.InputEvent BUTTON1_MASK))
             (Thread/sleep sleep-time)
             ])))

(defn click-points [seq]
  (doseq [point seq] (click-point point)))

(defn clear-floor [floor]
  (click-points (nth crystals floor)))

(defn move-up [floor]
  (if (not= floor 4) ;; ladders on 4 must be clicked x2
    (click-points (nth ladders floor))
    (dorun [(click-points (nth ladders floor))
            (click-points (nth ladders floor))])))

(defn clear-floor-and-move-up [floor]
  (dorun [(clear-floor floor) (move-up floor)]))

(defn run-game []
  (dorun [(click-point start-pos)
          (Thread/sleep 1000)
          (doseq [floor (take (dec (count ladders)) (iterate inc 1))]
            (dorun [(clear-floor floor) (move-up floor)]))]))
