package io.github.burukeyou.dataframe.util;

import io.github.burukeyou.dataframe.iframe.function.BigDecimalFunction;
import io.github.burukeyou.dataframe.iframe.function.NumberFunction;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;


public class CollectorsPlusUtil {
	static final Set<Collector.Characteristics> CH_NOID = Collections.emptySet();

	private CollectorsPlusUtil() {
	}

	@SuppressWarnings("unchecked")
	private static <I, R> Function<I, R> castingIdentity() {
		return i -> (R) i;
	}


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

	public static <T,R extends Number> Collector<T, ?, BigDecimal> summingBigDecimalForNumber(NumberFunction<T,R> numberFunction) {
		BigDecimalFunction<? super T> mapper = getBigDecimalFunction(numberFunction);
		return summingBigDecimal(mapper);
	}

	private static <T, R extends Number> BigDecimalFunction<? super T> getBigDecimalFunction(NumberFunction<T, R> numberFunction) {
		return (e) -> {
			Number apply = numberFunction.apply(e);
			if (apply == null){
				return null;
			}
			return apply instanceof BigDecimal ? (BigDecimal)apply : new BigDecimal(apply.toString());
		};
	}

	public static <T> Collector<T, ?, BigDecimal> summingBigDecimal(BigDecimalFunction<? super T> mapper) {
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

	public static <T> Collector<T, ?, BigDecimal> maxBy(BigDecimalFunction<? super T> mapper) {
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

	public static <T> Collector<T, ?, BigDecimal> minBy(BigDecimalFunction<? super T> mapper) {
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

	public static <T,R extends Number> Collector<T, ?, BigDecimal> averagingBigDecimal(NumberFunction<T,R> mapper, int newScale,
																	  int roundingMode){
		return averagingBigDecimal(getBigDecimalFunction(mapper),newScale,roundingMode);
	}

	public static <T> Collector<T, ?, BigDecimal> averagingBigDecimal(BigDecimalFunction<? super T> mapper, int newScale,
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