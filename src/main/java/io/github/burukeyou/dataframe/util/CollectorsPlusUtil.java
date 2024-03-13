package io.github.burukeyou.dataframe.util;

import io.github.burukeyou.dataframe.iframe.ToBigDecimalFunction;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 *  对 java.util.stream.Collectors 聚合汇总的扩展实现， 支持BigDecimal 的 max、min、avg、sum
 *
 */
public class CollectorsPlusUtil {
	static final Set<Collector.Characteristics> CH_NOID = Collections.emptySet();

	private CollectorsPlusUtil() {
	}

	@SuppressWarnings("unchecked")
	private static <I, R> Function<I, R> castingIdentity() {
		return i -> (R) i;
	}

	/**
	 * Simple implementation class for {@code Collector}.
	 *
	 * @param <T>
	 *            the type of elements to be collected
	 * @param <R>
	 *            the type of the result
	 */
	static class CollectorImpl<T, A, R> implements Collector<T, A, R> {
		private final Supplier<A> supplier;
		private final BiConsumer<A, T> accumulator;
		private final BinaryOperator<A> combiner;
		private final Function<A, R> finisher;
		private final Set<Characteristics> characteristics;

		CollectorImpl(Supplier<A> supplier, BiConsumer<A, T> accumulator, BinaryOperator<A> combiner,
				Function<A, R> finisher, Set<Characteristics> characteristics) {
			this.supplier = supplier;
			this.accumulator = accumulator;
			this.combiner = combiner;
			this.finisher = finisher;
			this.characteristics = characteristics;
		}

		CollectorImpl(Supplier<A> supplier, BiConsumer<A, T> accumulator, BinaryOperator<A> combiner,
				Set<Characteristics> characteristics) {
			this(supplier, accumulator, combiner, castingIdentity(), characteristics);
		}

		@Override
		public BiConsumer<A, T> accumulator() {
			return accumulator;
		}

		@Override
		public Supplier<A> supplier() {
			return supplier;
		}

		@Override
		public BinaryOperator<A> combiner() {
			return combiner;
		}

		@Override
		public Function<A, R> finisher() {
			return finisher;
		}

		@Override
		public Set<Characteristics> characteristics() {
			return characteristics;
		}
	}

	/**
	 * 求和
	 * @param mapper
	 * @param <T>
	 * @return
	 */
	public static <T> Collector<T, ?, BigDecimal> summingBigDecimal(ToBigDecimalFunction<? super T> mapper) {
		return new CollectorImpl<>(() -> new BigDecimal[1], (a, t) -> {
			if (a[0] == null) {
				a[0] = BigDecimal.ZERO;
			}
			a[0] = a[0].add(mapper.applyAsBigDecimal(t));
		}, (a, b) -> {
			a[0] = a[0].add(b[0]);
			return a;
		}, a -> a[0], CH_NOID);
	}

	/**
	 * 根据value对集合进行逆序排序
	 * @param unorderedList
	 * @return
	 */
	public static List<Map.Entry<String, BigDecimal>> sortByValueAndReverse(List<Map.Entry<String, BigDecimal>> unorderedList) {
		Collections.sort(unorderedList, new Comparator<Map.Entry<String, BigDecimal>>() {
		@Override
		public int compare(Map.Entry<String, BigDecimal> o1, Map.Entry<String, BigDecimal> o2) {
			BigDecimal p = o2.getValue().subtract(o1.getValue());
			if (p.compareTo(BigDecimal.ZERO) > 0) {
				return 1;
			} else if (p.compareTo(BigDecimal.ZERO) == 0) {
				return 0;
			} else {
				return -1;
			}
		}});

		return unorderedList;
	}

	/**
	 * 根据value对集合进行逆序排序 (Long 类型)
	 * @param unorderedList
	 * @return
	 */
	public static List<Map.Entry<String, Long>> sortByValueAndReverseForLong(List<Map.Entry<String, Long>> unorderedList) {
		Collections.sort(unorderedList, new Comparator<Map.Entry<String, Long>>() {
			@Override
			public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
				Long p = o2.getValue() - o1.getValue();
				if (p > 0) {
					return 1;
				} else if (p == 0) {
					return 0;
				} else {
					return -1;
				}
			}});

		return unorderedList;
	}

	/**
	 * 求最大,这里的最小MIN值，作为初始条件判断值，如果某些数据范围超过百亿以后，可以根据需求换成Long.MIN_VALUE或者Double.MIN_VALUE
	 * @param mapper
	 * @param <T>
	 * @return
	 */
	public static <T> Collector<T, ?, BigDecimal> maxBy(ToBigDecimalFunction<? super T> mapper) {
		return new CollectorImpl<>(
				() -> new BigDecimal[]{new BigDecimal(Integer.MIN_VALUE)},
				(a, t) -> {
					a[0] = a[0].max(mapper.applyAsBigDecimal(t));
				},
				(a, b) -> {
					a[0] = a[0].max(b[0]);
					return a;
				},
				a -> a[0], CH_NOID);
	}

	/**
	 * 求最小，这里的最大MAX值，作为初始条件判断值，如果某些数据范围超过百亿以后
	 * ，可以根据需求换成Long.MAX_VALUE或者Double.MAX_VALUE
	 * @param mapper
	 * @param <T>
	 * @return
	 */
	public static <T> Collector<T, ?, BigDecimal> minBy(ToBigDecimalFunction<? super T> mapper) {
		return new CollectorImpl<>(
				() -> new BigDecimal[]{new BigDecimal(Integer.MAX_VALUE)},
				(a, t) -> {
					a[0] = a[0].min(mapper.applyAsBigDecimal(t));
				},
				(a, b) -> {
					a[0] = a[0].min(b[0]);
					return a;
				},
				a -> a[0], CH_NOID);
	}

	/**
	 * 返回一个平均值
	 *
	 * @param newScale     保留小数位数
	 * @param roundingMode 小数处理方式
	 *                     #ROUND_UP 进1
	 *                     #ROUND_DOWN 退1
	 *                     #ROUND_CEILING  进1截取：正数则ROUND_UP，负数则ROUND_DOWN
	 *                     #ROUND_FLOOR  退1截取：正数则ROUND_DOWN，负数则ROUND_UP
	 *                     #ROUND_HALF_UP >=0.5进1
	 *                     #ROUND_HALF_DOWN >0.5进1
	 *                     #ROUND_HALF_EVEN
	 *                     #ROUND_UNNECESSARY
	 */
	public static <T> Collector<T, ?, BigDecimal> averagingBigDecimal(ToBigDecimalFunction<? super T> mapper, int newScale,
																	  int roundingMode) {
		return new CollectorImpl<>(
				() -> new BigDecimal[]{new BigDecimal(0), new BigDecimal(0)},
				(a, t) -> {
					a[0] = a[0].add(mapper.applyAsBigDecimal(t));
					a[1] = a[1].add(BigDecimal.ONE);
				},
				(a, b) -> {
					a[0] = a[0].add(b[0]);
					return a;
				},
				a -> a[0].divide(a[1], MathContext.DECIMAL32).setScale(newScale, roundingMode), CH_NOID);
	}


}