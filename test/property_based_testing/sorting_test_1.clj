; Use the clojure.test integration of test.check.
; This example adapted from the test.check README.
;
; Generators: Used to generate "random" instances of a type. There are
;             many generators already included for common types like
;             numbers and strings.
; Properties: Canned property tests for common types. For example, your own
;             checks of a compound type with integers and strings could
;             exploit the built-in properties of integers and strings.
; defspec:    Defined in test.check. Property tests are sometimes called specifications.

; Check properties of sorting
(ns property_based_testing.sorting-test-1
  (:use clojure.test)
  (:require [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :as ct :refer (defspec)]))

; If you sort a vector of integers (or any type) once, then twice,
; does the second sort effectively do nothing? It should, meaning it's
; idempotent.
; This property is extremely useful. In many highly concurrent systems,
; you can't guarantee the order of when operations will be applied. Even worse,
; you can't guarantee that an operation won't be applied more than once, e.g.,
; because the first attempt to apply it over a network appeared to fail or
; timeout, so you try again, but in fact both attempts succeed. If the operation
; is idempotent, YOU DON'T CARE! It's perfectly fine. Hence, idempotent
; operations, when possible, are really valuable for simplifying the design of
; such systems.

(defspec sort-idempotent-prop             ; Define a property
  (prop/for-all [v (gen/vector gen/int)]  ; For all generated vectors of generated integers
    (= (sort v) (sort (sort v)))))        ; Check that sorting once vs. twice are equal

; Exercise: What happens if you use a map instead of vector?


; Drill down; expect an element to be less than its neighbor to the right.

(defn first-less-than-next [s]
  (let [[a b & tail] s]
    (cond
      (nil? a) true
      (nil? b) true
      (> a b) false
      (<= a b) (first-less-than-next(cons b tail)))))

; Not sure why this is failing currently...
(defspec sorted-elements-ordered-prop
  (prop/for-all [v (gen/vector gen/int)]
    (let [s (sort v)]
      ; (println s)
      (first-less-than-next s))))
