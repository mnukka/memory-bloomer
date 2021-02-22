# Memory Bloomer
This is a memory cache module that supports using bloom filter as an underlying data structure model.
 
 * A Bloom filter is a space-efficient probabilistic data structure, conceived by Burton Howard Bloom in 1970, that is used to test whether an element is a member of a set.
 * False positive matches are possible, but false negatives are not – in other words, a query returns either "possibly in set" or "definitely not in set".
 * In general, with bloom filters, elements can be added to the set, but not removed. However, for current implementation the set is built to be immutable.



### Conventions
* Clean code
* Naming test methods: _MethodName_StateUnderTest_ExpectedBehavior_