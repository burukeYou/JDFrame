package io.github.burukeyou.dataframe.util;

import io.github.burukeyou.dataframe.iframe.item.FI2;
import io.github.burukeyou.dataframe.iframe.item.FI3;
import io.github.burukeyou.dataframe.iframe.item.FI4;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class FrameUtil {

    private FrameUtil(){}

    public static <K, V> List<FI2<K, V>> toListFI2(Map<K, V> resultMap) {
        return resultMap.entrySet().stream().map(e -> new FI2<>(e.getKey(), e.getValue())).collect(toList());
    }

    public static <K, J, V> List<FI3<K, J, V>> toListFI3(Map<K, Map<J, V>> map) {
        return map.entrySet().stream()
                .flatMap(et ->
                        et.getValue().entrySet().stream()
                                .map(subEt -> new FI3<>(et.getKey(), subEt.getKey(), subEt.getValue()))
                                .collect(toList())
                                .stream()
                )
                .collect(toList());
    }

    public static <K, J, H, V> List<FI4<K, J, H, V>> toListFI4(Map<K, Map<J, Map<H, V>>> map) {
        return map.entrySet().stream()
                .flatMap(et ->
                        et.getValue().entrySet().stream()
                                .flatMap(subEt -> subEt.getValue().entrySet().stream().map(sub2Et -> new FI4<>(et.getKey(), subEt.getKey(), sub2Et.getKey(), sub2Et.getValue())).collect(toList()).stream())
                                .collect(toList())
                                .stream()
                )
                .collect(toList());
    }
}
