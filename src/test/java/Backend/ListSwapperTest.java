package Backend;

import kotlin.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListSwapperTest {
    private final int listSize = 9 ;
    private int[] list = new int[listSize];
    ListSwapper ls = new ListSwapper(listSize);

    public void swapArray(Pair<Integer, Integer> pair){
        int index1 = pair.getFirst(), index2 = pair.getSecond();
        int foo = list[index1];
        list[index1] = list[index2];
        list[index2] = foo;
        /*System.out.println("Swap index " + index1 + " and " + index2);
        System.out.println("Values " + list[index1] + " and " + list[index2]);*/
    }

    public void swap(int i1, int i2){
        swapArray(ls.registerSingleSwap(i1, i2));
        /*print();*/
    }

    public void print(){
        for(int i : list){
            System.out.print(i +  ", ");
        }
        System.out.println("");
    }

    public void initTest(){
        for(int i = 0; i < listSize; i++){
            list[i] = i;
        }
    }

    @Test
    public void test1(){
        initTest();
        /*print();*/
        swap(1, 8);
        /*System.out.print(ls.getPermutations());*/
        swap(2, 6);
        /*System.out.print(ls.getPermutations());*/
        swap(3, 2);
        /*System.out.print(ls.getPermutations());*/
        swap(3, 1);
        /*System.out.print(ls.getPermutations());*/
        swap(5, 8);
        /*System.out.print(ls.getPermutations());*/
        swap(2, 1);
        /*System.out.print(ls.getPermutations());*/
        swap(5, 3);
        /*System.out.print(ls.getPermutations());*/
        int[] list = {0, 3, 6, 1, 4, 8, 2, 7, 5};

        for(int i = 0; i < listSize; i++){
            assert(this.list[i] == list[i]);/**/
        }

    }


}