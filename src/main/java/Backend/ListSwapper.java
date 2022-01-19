package Backend;

import kotlin.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListSwapper {
    private ArrayList<ArrayList<Integer>> perm_list = new ArrayList<>();
    private Set<Integer> permutated = new HashSet<>();
    private int listSize, swapeeNo, swaperNo;

    public ListSwapper(int listSize){
        this.listSize = listSize;
    }

    public Pair<Integer, Integer>
    registerSingleSwap(int swapeeNo, int swaperNo)
    throws IndexOutOfBoundsException{
        this.swapeeNo = swapeeNo;
        this.swaperNo = swaperNo;
        checkIndex(swapeeNo);
        checkIndex(swaperNo);
        int first = getPrev(swapeeNo), second = getPrev(swaperNo);
        changePermutations();
        removeSingleElemPerms();
        return new Pair<>(first, second);
    }

    private void removeSingleElemPerms(){
        ArrayList<ArrayList<Integer>> remove = new ArrayList<>();
        for(ArrayList<Integer> l : perm_list){
            if(l.size() <= 1){
                remove.add(l);
            }
        }
        perm_list.removeAll(remove);
    }

    private int getPrev(int arg){
        int prev;
        for(List<Integer> l : perm_list){
            if(l.contains(arg)){
                for(int i = 0; i < l.size(); i++){
                    if(i != 0) prev = l.get(i - 1);
                    else prev = l.get(l.size()- 1);

                    if(l.get(i) == arg){
                        return prev;
                    }
                }
            }
        }
        return arg;
    }

    void changePermutations() throws IndexOutOfBoundsException{

        if(!permutated.contains(swapeeNo) && !permutated.contains(swaperNo)){
            permutated.add(swapeeNo);
            permutated.add(swaperNo);
            ArrayList<Integer> list = new ArrayList<>();
            list.add(swapeeNo);
            list.add(swaperNo);
            perm_list.add(list);
        }
        else if(!permutated.contains(swapeeNo)){
            //wstaw swapeeNo za swaperNo w permutacji
            permutated.add(swapeeNo);
            for(List<Integer> l : perm_list){
                if(l.contains(swaperNo)){
                    for(int i = 0; i < l.size(); i++){
                        if(l.get(i) == swaperNo){
                            l.add(i, swapeeNo);
                            i = l.size();
                        }
                    }
                }
            }
        }
        else if(!permutated.contains(swaperNo)){
            //permutated contains swapeeNo
            int foo = swapeeNo;
            swapeeNo = swaperNo;
            swaperNo = foo;
            changePermutations();
        }
        else{
            List<Integer> lEE = null, lER = null;
            boolean twoLists = false;
            for(List<Integer> l : perm_list){
                if(l.contains(swaperNo) || l.contains(swapeeNo)){
                    if(l.contains(swaperNo) && l.contains(swapeeNo)){
                        lEE = l;
                    }
                    else if(l.contains(swaperNo)){
                        lER = l;
                        twoLists = true;
                    }
                    else{
                        lEE = l;
                        twoLists = true;
                    }
                }
            }
            bothElementsPermutated(twoLists, lEE, lER);
        }
    }

    private void bothElementsPermutated(boolean twoLists, List<Integer> lEE, List<Integer> lER){
        if(twoLists){
            //gdy sa w dwoch oddielnych permutacjach
            //znajdz liste z jednym z elementów
            //zastap ten elem. perm. zaczynajaca sie od 2. elem
            //wstaw elem. po tej perm.
            boolean startAdding = false;
            int whereSwaperNo = -1;
            for(int i = 0; i < lEE.size(); i++){
                if(lEE.get(i) == swapeeNo){
                    for(int j = 0; j < lER.size() || startAdding; j++){
                        if(startAdding){
                            if(j == lER.size())
                                j = 0;
                            if(j == whereSwaperNo)
                                startAdding = false;
                            else
                                lEE.add(i++, lER.get(j));
                        }
                        if(whereSwaperNo < 0 && lER.get(j) == swaperNo){
                            startAdding = true;
                            lEE.add(i++, lER.get(j));
                            whereSwaperNo = j;
                        }

                    }
                    i = lEE.size();
                }
            }
            perm_list.remove(lER);
        }
        else{
            //gdy sa w jednej perm
            //znajdz jeden element
            //wez go i wszystkie elementy przed 2. elem i wylacz je jako oddzielna perm.
            boolean startCopy = false;
            ArrayList<Integer> list = new ArrayList<>();

            for(int i = 0; i < lEE.size() || startCopy; i++) {
                if(i == lEE.size()) {
                    i = 0;
                }
                if (lEE.get(i) == swapeeNo || lEE.get(i) == swaperNo) {
                    if(!startCopy) {
                        startCopy = true;
                    }
                    else{
                        startCopy = false;
                        i = lEE.size();
                    }
                }
                if(startCopy) {
                    list.add(lEE.get(i));
                }
            }
            perm_list.add(list);
            lEE.removeAll(list);
        }
    }

    private void checkIndex(int idx) throws IndexOutOfBoundsException{
        if(idx < 0 || idx >= listSize){
            throw new IndexOutOfBoundsException();
        }
    }

    public String getPermutations(){
        StringBuilder sb = new StringBuilder();
        for(List<Integer> l : perm_list){
            sb.append("( ");
            for(int i : l){
                sb.append(i + " ");
            }
            sb.append(")" + "\n");
        }

        return sb.toString();
    }

}
