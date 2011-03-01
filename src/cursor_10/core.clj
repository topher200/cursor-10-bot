(ns cursor-10.core)

(def sleep-time 10)

(def start-pos
     ;; The position of the start button for the game
     [726.0 608.0])

(def crystals
     ;; Vector of vectors of [X Y] coordinates (whooo) with crystal locations.
     ;; Each vector cooresponds to a different floor. Floor counting starts at 1
     [[]
      [[723.0 577.0] [611.0 510.0] [754.0 415.0] [855.0 483.0]]
      [[511.0 486.0] [727.0 612.0] [725.0 486.0] [723.0 363.0]]
      ])

(def ladders
     ;; Vector of [X Y] coordinates for the ladder on each level.
     ;; Floor counting starts at 1
     [[]
      [619.0 420.0]
      [956.0 488.0]
      ])

(defn click-point
  ([[x y]]
     (dorun [(println x y)
             (.mouseMove (java.awt.Robot.) x y)
             (Thread/sleep sleep-time)
             (.mousePress (java.awt.Robot.)
                          (.. java.awt.event.InputEvent BUTTON1_MASK))
             (.mouseRelease (java.awt.Robot.)
                          (.. java.awt.event.InputEvent BUTTON1_MASK))
             ])))

(defn click-points [seq]
  (doseq [point seq] (click-point point)))

(defn clear-floor [floor]
  (click-points (nth crystals floor)))

(defn move-up [floor]
  (click-point (nth ladders floor)))

(defn clear-floor-and-move-up [floor]
  (dorun [(clear-floor floor) (move-up floor)]))

(defn floor-1 []
  (click-points 
   (conj (nth crystals 0) (nth ladders 0))))

(defn run-game []
  (dorun [(click-point start-pos)
          (Thread/sleep 1000)
          (doseq [floor [1 2]]
            (dorun [(clear-floor floor) (move-up floor)]))]))
