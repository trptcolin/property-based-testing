(ns property_based_testing.monoids-test-5
  (:use clojure.test)
  (:require [clojure.core.reducers :as r]
            [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :as ct :refer (defspec)]))

; Let's finish by learning about a very handy generalization of addition,
; a class of data structures called "Monoids". We'll look at examples that
; provide approximate answers with very efficient utilization of space, which
; is a useful tradeoff to make in big data systems.
; We won't have time to go into great detail, but I encourage you to view this
; presentation (http://www.infoq.com/presentations/abstract-algebra-analytics)
; for a detailed look at how important these concepts are for "webscale"
; companies like Twitter (where Avi did this work), Stripe, Google, Facebook, ...

; A Monoid is a set of elements S with a binary operation,
; let's call it `add`, which has the following properties
; Associativity:    `a + (b + c) = (a + b) + c`  (look familiar?)
; Identity Element: `a + 0 = 0 + a = a`  for some 0 element in S.
; (Commutativity is NOT required.)

; First, let's define a protocol:
; (Called "type classes" in Haskell and Scala.)

(defprotocol Monoid
  (add [this b] "Addition, but we would like to constrain all types to be the same.")
  (zero [this]  "Return the zero (identity) element"))

; Associativity.

(defn monoid-addition-associativity-prop-fn [ctor]
  (let [[mgen] [(gen/fmap (fn [i] (ctor i)) gen/int)]]
  (prop/for-all [a mgen b mgen c mgen]
    (= (add a (add b c)) (add (add a b) c)))))

; Addition with the identity (zero)

(defn monoid-addition-with-identity-prop-fn [ctor]
  (let [[mgen] [(gen/fmap (fn [i] (ctor i)) gen/int)]]
    (prop/for-all [a mgen]
      (and (= a (add a (zero a))) (= (add a (zero a)) a)))))

; Example 1: Try it with plain old integer addition
; (Admittedly, it's convoluted wrapping integers in monoids...)

(defrecord IntMonoid [value] Monoid
  (add  [this b] (IntMonoid. (+ value (:value b))))
  (zero [this] (IntMonoid. 0)))

; Examples:

(def one   (IntMonoid. 1))
(def two   (IntMonoid. 2))
(def three (IntMonoid. 3))
(def izero (zero one))       ; a little awkward...

(add one two)
(add two three)
(println izero)
(add one   izero)
(add two   izero)
(add three izero)

(defspec int-monoid-addition-associativity-prop
  (monoid-addition-associativity-prop-fn (fn [i] (IntMonoid. i))))

(defspec int-monoid-addition-with-identity-prop
  (monoid-addition-with-identity-prop-fn (fn [i] (IntMonoid. i))))

; Homework: Is it possible to define a protocol that "int" etc. already supports?

; Example 2: Top K (we'll just do integers for now)

(defrecord TopImpl [k value])

; To simplify things, we COULD treat n as a value, not a Top instance,
; but this actually makes (reduce ...) fail to work below, where both
; arguments should be of the same type!
; Note that we use flatten to avoid nested collections, so that you could do
; (add (Top. 5 [10 5 1]) (Top. 5 [5 4 3 2 1])) and expect to get
; (Top. 5 [10 5 4 3 2]).
; For better efficiency at scale and very large k, use a Priority Queue instead.
; It's ANNOYING that we can't make "value" optional here (try it):
(defrecord Top [k value] Monoid
  (add  [this tn] (Top. k (take k (sort-by > (flatten (cons (:value tn) value))))))
  (zero [this] (Top. k [])))

(def top10 (Top. 10 []))
(reduce (fn [top ti] (add top ti)) (map (fn [i] (Top. 10 [i])) (range 20)))

(defspec top-k-monoid-addition-associativity-prop
  (monoid-addition-associativity-prop-fn (fn [i] (Top. 1 [i]))))

(defspec top-k-monoid-addition-with-identity-prop
  (monoid-addition-with-identity-prop-fn (fn [i] (Top. 1 [i]))))

; So, I hope you notice that reusing the same infrastructure expecting a Monoid
; "just works". There is a fair amount of boilerplate, so the benefits won't
; outweigh the disadvantages until you use a lot of them.

; Exercise: Implement a Bloom Filter
; The math on this page, https://en.wikipedia.org/wiki/Bloom_filter, is scary
; looking but the idea is simple. When you hash a key, you end up with ones and
; zeros, let's say this value for an 8-bit hash: 01010011. What if you treat this
; a bit vector, where counting from the right, entries 0 and 1 have the value 1,
; entries 2 and 3 have the value 0, etc.
; Now hash another key. Suppose you get this 10010101. Set the ones in the same
; bit vector, where you'll have some overlap with the previous key. Now the vector
; is 11010111.
;
; The idea of a bloom filter is that it's fast to calculate these key hashes and
; fast to see if the bit vector already has the same pattern of 1s. If it does,
; there is a high probability YOU'VE ALREADY SEEN THAT KEY. Why is this interesting?
; Because if the key represents a record in a data store that would be "expensive"
; to search (such as one of many files used to store database records), you want
; to skip searching if you can. What a bloom filter tells you is "yes this key is
; in the corresponding storage or a false positive." Worst case, you search that
; file and don't find the record. BUT, a bloom filter never gives you a false
; negative (because we're checking for 1s in the bit vector). So, you'll never
; miss a record by not searching a file you should have, but you might do more
; searches than absolutely necessary.
; The bigger the bit vector, the lower the probability of false positives. (Does
; that seem intuitive from our example?) In practice, you try to choose a small-
; enough bit vector that your errors are tolerably low, but not so big that the
; bit vector itself is expensive. In fact, with a bit vector of about 9.6 bits
; per element, the error is only 1%.

; What if you wanted an approximate count of the number of times a particular key
; appears (say that the keys represent kinds of events on an event stream), while
; bloom filters only have bits, 1s and 0s, representing "present" or "absent", an
; extension is the Count-Min Sketch, where each position is another array, and
; therefore able to represent > 1.


