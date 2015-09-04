# Property Based Testing

This is a simple Clojure project that's designed to introduce **property-based testing**. This is the functional programming take on testing, where you define the properties or _laws_ that instances of your types should obey, and then you use a framework to generate a random sample of instances against which the properties are verified. It's another example of the impact of Mathematics on FP.

For example, integer addition is _associative_, `a + (b + c) == (a + b) + c` for all integers `a`, `b`, and `c`.

A framework can't reasonably test all possible integers, of course, so it either picks randomly, picks _judiciously_ (e.g., around zero and min/max values), or you write a _generator_ that picks values you suspect could be sources of bugs. Hence, it's still not an exhaustive test, but it's much better than the ad hoc way we often write tests that pick a few example instances and hope for the best.

This approach is not only useful for obviously mathematical entities like integers, it's also a fruitful way to think about common domain types we use. American Zip Codes have rules (5 digits or 5+4 digits and not all values are allowed). You might require street addresses to confirm to Postal System standards. URLs, email addresses, IP addresses, and money are other examples where property based testing is applicable.

The first implementation of such a tool was [QuickCheck](https://en.wikipedia.org/wiki/QuickCheck) for Haskell, which has been ported to many languages, such as the following:

* Haskell: [QuickCheck](http://hackage.haskell.org/package/QuickCheck), [Quvid](http://www.quviq.com/index.html).
* Erlang: [Quviq](http://www.quviq.com/products/erlang-quickcheck/).
* Clojure: [test.check](https://github.com/clojure/test.check).
* Scala: [ScalaCheck](http://scalacheck.org/)
* Ruby: [Theft](http://spin.atomicobject.com/2014/09/30/quickcheck-in-ruby/),  [Propr](https://github.com/kputnam/propr), [Rantly](https://github.com/abargnesi/rantly)
* etc.

## About This Project

All the examples are in the `test` directory tree. They are well commented to explain what's going on.

The files are numbered to encourage an order to go through them. Start with the first few files at least, which assume no prior knowledge and explain the API in great detail, then go from there.

* `core_test_0.clj` - A "baseline" example of trivial, TDD-style test.
* `idempotent_sorting_test_1.clj` - Demonstrates that sorting is an _idempotent_ operation, meaning continued "application" doesn't change the results after the first time, so you could safely do it repeatedly. Explains in great detail the mechanics of property based testing frameworks, as implemented in `test.check`.
* `number_addition_test_2.clj` - The properties of addition of integers.
* `string_addition_test_3.clj` - The properties of "addition" of strings.
* Others...

## Disclaimer

I'm not a Clojure expert. Feedback is welcome.
