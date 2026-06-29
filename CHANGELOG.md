# Release Notes

## 0.2.0 (????-??-??)

- Optimize tag and BodyLength(9) encoding. The benchmark results on Apple
  MacBook Pro (M1 Pro, 2021) indicate an average of 4% reduction in the
  operation latency with larger integer values benefiting the most.

- Optimize `FixDate`, `FixTime`, and `FixTimestamp` decoding. The benchmark
  results on Apple MacBook Pro (M1 Pro, 2021) indicate 47–68% reduction in
  operation latency with `FixTimestamp` decoding benefiting the most: before
  the changes it took 14.51 ns/op, after the changes just 4.67 ns/op.

## 0.1.0 (2026-06-20)

- Initial release
