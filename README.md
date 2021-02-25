---
title: Memory Bloomer
description: Memory cache using bloom filter
author: Miko Nukka
created:  2021 Feb 18
modified: 2021 Feb 25

---

Memory Bloomer
=========
This is a memory cache module that supports using bloom filter as an underlying data structure model.

![Bloom filter in action](/readme/bloomfilter_how_it_works.gif?raw=true "How bloom filter works")
 
 * A Bloom filter is a space-efficient probabilistic data structure, conceived by Burton Howard Bloom in 1970, 
 that is used to test whether an element is a member of a set.
 * False positive matches are possible, but false negatives are not – in other words, 
 a query returns either "possibly in set" or "definitely not in set".
 * In general, with bloom filters, elements can be added to the set, but not removed. 
 However, for current implementation the set is built to be immutable.


If each hash function can be computed in a constant time (which
is true for all the most popular hash functions), the time to add a new
element or test an element is a fixed constant O(k) and independent from
the filter’s length ``m`` and the number of elements in the filter.
The performance of the Bloom filter is highly dependent on the chosen hash
functions ``k``. A hash function with a good uniformity will reduce the practically
observed false positive rate. On the other hand, the faster the computation
of each hash function, the smaller the overall time of each operation, and
it is therefore recommended to avoid cryptographic hash functions.[1]



## Hashes
Under the hood we are using two hashing algorithms.
1. [MurmurHash](https://github.com/aappleby/smhasher/wiki/MurmurHash3) by Austin Appleby
2. [SpookyHash](https://burtleburtle.net/bob/hash/spooky.html) by Bob Jenkins

## Dynamic bitmap
When user provides input list which needs to be cached 
then automatically appropriate bitmap size will be calculated, 
using 2 hash functions. With collision probability of p=0.005.

* m - bitmap size
* n - number of elements to be hashed
* k - number of hash functions
* p - collision probability

<img alt="formula" src="https://render.githubusercontent.com/render/math?math=m%20=%20\frac{n\log%20p}{\log%20\left(\frac{1}{2^{\log%202}}\right)}%20=%20\frac{n%20\log%20p}{\log(0.6185)}." />

## Performance
When we run 100 tests with current bloom filter setup on [words](https://en.wikipedia.org/wiki/Words_(Unix)) 
with 10000 randomly generated strings. We will end up with an average collision probability ~0.005.
![False positives](/readme/collision_chart.png?raw=true "How bloom filter works")

How to use this library
---

##### Using MemoryCacheFactory
For quick out of the box use
````
IMemoryCache cache = MemoryCacheFactory.createImmutableCache(Arrays.asList("one", "two"));
assertTrue(cache.isKeyPresent("one"));
assertFalse(cache.isKeyPresent("three"));
````

##### Using CacheBuilder
If you want to switch out one of the hashing algorithms or add one of your own.
````
return new CacheBuilder()
        .addHashFunction(new MurmurHash())
        .addHashFunction(new SpookyHash())
        .addHashFunction(new MyCustomHash())
        .addDataStructure(new BloomFilter32<String>())
        .buildCache(list);
````


### Conventions
* Clean code
* Naming test methods: _methodName_StateUnderTest_ExpectedBehavior_

#### References
1. Probabilistic Data Structures And Algorithms by Andrii Gakhov
2. https://en.wikipedia.org/wiki/Bloom_filter 
