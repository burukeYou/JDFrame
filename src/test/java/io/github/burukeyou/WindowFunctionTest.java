package io.github.burukeyou;

import io.github.burukeyou.data.WebPvDto;
import io.github.burukeyou.dataframe.iframe.SDFrame;
import io.github.burukeyou.dataframe.iframe.window.Window;
import io.github.burukeyou.dataframe.iframe.window.round.Range;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WindowFunctionTest {


    static List<WebPvDto> dataList = new ArrayList<>();

    static {
        dataList.add(new WebPvDto("a",0,1));
        dataList.add(new WebPvDto("a",1,5));
        dataList.add(new WebPvDto("a",2,7));
        dataList.add(new WebPvDto("a",7,7));
        dataList.add(new WebPvDto("a",3,3));
        dataList.add(new WebPvDto("a",4,2));
        dataList.add(new WebPvDto("a",5,4));
        dataList.add(new WebPvDto("a",6,4));
//        dataList.add(new WebPvDto("b",7,4));
//        dataList.add(new WebPvDto("b",8,4));
//        dataList.add(new WebPvDto("b",7,6));
//        dataList.add(new WebPvDto("b",8,2));

    }

    /**
     *  Count
     */
    @Test
    public void testOverCount(){
        SDFrame.read(dataList)
                //.window()
                //.window(Window.roundStartRow2CurrentRowBy())
                //.window(Window.roundCurrentRow2EndRowBy())
                //.window(Window.roundBefore2CurrentRowBy(2))
                //.window(Window.roundCurrentRow2AfterBy(2))
                //.window(Window.sortAscBy(WebPvDto::getScore))
                //.window(Window.roundStartRow2CurrentRowBy())
                .window(Window.roundBetweenBy(Range.BEFORE(1), Range.AFTER(2)))
                .overCount()
                .show(30);

    }

    /**
     *  Sum
     */
    @Test
    public void testOverSum(){
        SDFrame.read(dataList)
                //.window()
                //.window(Window.roundStartRow2CurrentRowBy())
                //.window(Window.roundCurrentRow2EndRowBy())
                //.window(Window.roundBefore2CurrentRowBy(2))
                .window(Window.roundCurrentRow2AfterBy(2))
                //.window(Window.sortAscBy(WebPvDto::getScore))
                //.window(Window.roundBetweenBy(Round.BEFORE(100),Round.AFTER(100)))
                .overSumS(WebPvDto::setSum,Window.roundBetweenBy(Range.BEFORE(1), Range.AFTER(1)),WebPvDto::getPvCount)
                .show(30);

    }

    /**
     *  Avg
     */
    @Test
    public void testOverAvg(){
        SDFrame.read(dataList)
                //.window()
                //.window(Window.roundStartRow2CurrentRowBy())
                //.window(Window.roundCurrentRow2EndRowBy())
                //.window(Window.roundBefore2CurrentRowBy(2))
                //.window(Window.roundCurrentRow2AfterBy(2))
                //.window(Window.sortAscBy(WebPvDto::getScore))
                .window(Window.roundBetweenBy(Range.BEFORE(1), Range.AFTER(2)))
                .overAvgS(WebPvDto::setAvg,WebPvDto::getPvCount)
                .show(30);
    }


    /**
     *  Max 和 Min
     */
    @Test
    public void testOverMax(){
        SDFrame.read(dataList)
                //.window()
                //.window(Window.roundStartRow2CurrentRowBy())
                //.window(Window.roundCurrentRow2EndRowBy())
                //.window(Window.roundBefore2CurrentRowBy(2))
                //.window(Window.roundCurrentRow2AfterBy(2))
                //.window(Window.sortAscBy(WebPvDto::getScore))
                .window(Window.roundBetweenBy(Range.BEFORE(1), Range.AFTER(1)))
                //.overMaxValueS(WebPvDto::setMax,WebPvDto::getPvCount)
                .overMinValueS(WebPvDto::setMax,WebPvDto::getPvCount)
                .show(30);

    }

    /**
     *  Lag(前N行) 、 Lead（后N行）
     */
    @Test
    public void testOverLagLead(){
        SDFrame.read(dataList)
                //.window()
                //.window(Window.roundStartRow2CurrentRowBy())
                .window(Window.roundCurrentRow2EndRowBy())
                //.window(Window.roundBefore2CurrentRowBy(2))
                //.window(Window.roundCurrentRow2AfterBy(2))
                //.window(Window.sortAscBy(WebPvDto::getScore))
                //.window(Window.roundStartRow2CurrentRowBy())
                //.window(Window.roundBetweenBy(Round.BEFORE(1),Round.AFTER(2)))
                .overLead(WebPvDto::getPvCount,2)
                .show(30);
    }

    /**
     *  NthValue 、 FirstValue 、 LastValue
     *  获取窗口范围内第N行的值
     */
    @Test
    public void testOverNthValue(){
        SDFrame.read(dataList)
                //.window()
                //.window(Window.roundStartRow2CurrentRowBy())
                //.window(Window.roundCurrentRow2EndRowBy())
                .window(Window.roundBefore2CurrentRowBy(2))
                //.window(Window.roundCurrentRow2AfterBy(2))
                //.window(Window.sortAscBy(WebPvDto::getScore))
                //.window(Window.roundStartRow2CurrentRowBy())
                //.window(Window.roundBetweenBy(Round.BEFORE(1),Round.AFTER(2)))
                .overNthValue(WebPvDto::getPvCount,1)
                //.overFirstValue(WebPvDto::getPvCount)
                //.overLastValue(WebPvDto::getPvCount)
                .show(30);
    }

    /**
     *  rowNumber:  单纯行号                            如 1，2，3，4，5
     *  rank：  相同值排名一样，排名不连续                 如 1 2 2 2 5 6 7
     *  denseRank:      相同值排名一样，排名连续          如 1 2 2 2 3 4 5
     *  percentRank:   百分比排名：        (rank-1) / (rows-1)
     */
    @Test
    public void testOverRank(){
        SDFrame.read(dataList)
                .defaultScale(8)
                .window(Window.sortAscBy(WebPvDto::getPvCount))
                .overRowNumberS(WebPvDto::setRowNumber)
                .overRankS(WebPvDto::setRank)
                .overDenseRankS(WebPvDto::setDensRank)
                .overPercentRankS(WebPvDto::setPercentRank)
                .show(30);
    }

    /**
     *  Ntile：
     *      给窗口尽量均匀的分成N个桶， 每个桶的编号从1开始。
     *      如果分布不均匀，则优先分配给最小的组，桶之间的大小差值最多不超过1
     */
    @Test
    public void testOverNtile(){
        SDFrame.read(dataList)
                .window(Window.groupBy(WebPvDto::getType))
                .overNtile(3)
                .show(30);
    }

    /**
     *  CumeDist： 累积分布的比率
     *         先生成排名编号（相同值认为排名一样）， 然后计算 （大于等于该排名编号的数量 / 窗口行数） 作为结果
     */
    @Test
    public void testOverCumeDist(){
        SDFrame.read(dataList)
                .defaultScale(8)
                .window(Window.groupBy(WebPvDto::getType).sortDesc(WebPvDto::getPvCount))
                .overCumeDist()
                .show(30);

    }
}
