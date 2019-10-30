package com.rao.study.algorithms.math;

public class PoissonTest {
    public static void main(String[] args){
        for (int i=0;i<9;i++){
            System.out.println(i+":    "+getPossionProbability(i,0.5));
        }
    }

    /**
     * k是泊松分布变量
     * lamda 为发生某个事件的平均值
     * @param k
     * @param lamda
     * @return
     */
    private static double getPossionProbability(int k, double lamda) {
        //e为低的-lamda次冥
        double c = Math.exp(-lamda), sum = 1;
        for (int i = 1; i <= k; i++) {
            sum *= lamda / i;
        }
        return sum * c;
    }
}
