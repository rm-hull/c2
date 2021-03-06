^:clj (ns c2.scale
        (:use [c2.util :only [c2-obj]]
              [c2.maths :only [expt]])
        (:require [c2.maths :as maths]))
^:cljs (ns c2.scale
         (:use-macros [c2.util :only [c2-obj]])
         (:use [c2.maths :only [expt]])
         (:require [c2.maths :as maths]))

(defprotocol IInvertable
  (invert [scale] "Inverted scale"))



;;Linear scale
;;
;;Kwargs:
;;> *:domain* domain of scale, default [0 1]
;;
;;> *:range* range of scale, default [0 1]
(c2-obj linear {:domain [0 1]
                :range  [0 1]}

        clojure.lang.IFn
        (invoke [_ x] (let [domain-length (- (last domain) (first domain))
                            range-length (- (last range) (first range))]
                        (+ (first range)
                           (* range-length
                              (/ (- x (first domain))
                                 domain-length)))))
        IInvertable
        (invert [this]
                (assoc this
                  :domain range
                  :range domain)))

(declare log)

;;Power scale
;;
;;Kwargs:
;;> *:domain* domain of scale, default [0 1]
;;
;;> *:range* range of scale, default [0 1]
(c2-obj power {:domain [0 1]
               :range  [0 1]}
        
        clojure.lang.IFn
        (invoke [_ x]
                ((comp (linear :domain (map expt domain)
                               :range range)
                       expt) x)))

;;Logarithmic scale
;;
;;Kwargs:
;;> *:domain* domain of scale, default [1 10]
;;
;;> *:range* range of scale, default [0 1]
(c2-obj log {:domain [1 10]
             :range  [0 1]}
        
        clojure.lang.IFn
        (invoke [_ x]
                ((comp (linear :domain (map maths/log domain)
                               :range range)
                       maths/log) x)))

