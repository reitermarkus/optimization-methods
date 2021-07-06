# Optimization Methods [![Code Coverage](https://codecov.io/gh/reitermarkus/optimization-methods/branch/main/graph/badge.svg)](https://codecov.io/github/reitermarkus/optimization-methods)


## Algorithms

### Firefly Swarm Optimization

Implementation of the [Firefly algorithm](https://en.wikipedia.org/wiki/Firefly_algorithm) by Xin-She Yang et al.

To run this algorithm on the DTLZ1 problem, use:

```
./gradlew run firefly
```

### Bee Colony Optimization

Implementation of the [Artificial Bee Colony algorithm](http://www.scholarpedia.org/article/Artificial_bee_colony_algorithm) by Dervis Karaboga.

To run this algorithm on the TSP problem, use:

```
./gradlew run beecolony
```


### References

- Yang, Xin-She, and Adam Slowik. "Firefly algorithm." Swarm Intelligence Algorithms. CRC Press, 2020. 163-174.
- Karaboga, Dervis. "Artificial bee colony algorithm." scholarpedia 5.3 (2010): 6915.
