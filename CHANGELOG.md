# Release Notes

## 0.2.0 (????-??-??)

- Fix Coordinated Omission (CO) in Tagline Initiator by recording the intended
  send time rather than the actual send time. The actual send time lags the
  intended send time when the event loop is busy processing previous messages
  at the intended send time.

- Fix `FixCheckSumCalculator#calculate(ByteBuf)`, which currently fails if the
  reader index is anything but zero.

- Optimize `FixDate`, `FixTime`, and `FixTimestamp` encoding. The benchmark
  results on Apple MacBook Pro (M1 Pro, 2021) indicate 32–51% reduction in
  operation latency with `FixTimestamp` encoding benefiting the most: before
  the changes it took 12.27 ns/op, after the changes just under half of that.

- Optimize tag and BodyLength(9) encoding. The benchmark results on Apple
  MacBook Pro (M1 Pro, 2021) indicate an average of 4% reduction in the
  operation latency with larger integer values benefiting the most.

- Optimize `FixDate`, `FixTime`, and `FixTimestamp` decoding. The benchmark
  results on Apple MacBook Pro (M1 Pro, 2021) indicate 47–68% reduction in
  operation latency with `FixTimestamp` decoding benefiting the most: before
  the changes it took 14.51 ns/op, after the changes just 4.67 ns/op.

## 0.1.0 (2026-06-20)

- Initial release
