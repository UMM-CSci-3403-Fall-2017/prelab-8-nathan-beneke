package search;

import java.util.ArrayList;
import java.util.Random;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    final int ARRAY_SIZE = 10000;
    Random random = new Random();
    ArrayList<Integer> numbers = new ArrayList<Integer>();
    for (int i=0; i<ARRAY_SIZE; ++i) {
      numbers.add(random.nextInt(ARRAY_SIZE));
    }
    System.out.println(searchArray(numbers.get(1), numbers));
    System.out.println(searchArray(numbers.get(5), numbers));
    System.out.println(searchArray(numbers.get(900), numbers));
    System.out.println(searchArray(numbers.get(3200), numbers));
    System.out.println(searchArray(numbers.get(7400), numbers));
    System.out.println(searchArray(numbers.get(9876), numbers));
    System.out.println(searchArray(2000000, numbers));
    System.out.println(searchArray(-45, numbers));
  }

  /*
  * modified searhArray from original code to see if ThreadedSearch is working (i.e. is faster than LinearSearch and
  * outputs correct answer.
  *
  * It seems to be working and is always faster than or the same speed as LinearSearch
   */
  private static boolean searchArray(int target, ArrayList<Integer> list) throws InterruptedException {
    ThreadedSearch<Integer> searcher=new ThreadedSearch<Integer>();
    LinearSearch<Integer> linSearcher = new LinearSearch<Integer>();

    long threadTime = System.currentTimeMillis();
    boolean threadRes = searcher.parSearch(4, target, list);
    threadTime = System.currentTimeMillis() - threadTime;

    long linTime = System.currentTimeMillis();
    boolean linRes = linSearcher.search(target, list);
    linTime = System.currentTimeMillis() - linTime;

    if(linRes == threadRes){
      System.out.println("With Target = " + target + ", linear and threaded searches returned the same result: " + linRes);
      System.out.println("Linear search took   " + linTime + "ms");
      System.out.println("Threaded search took " + threadTime + "ms");
      System.out.println();
    } else {
      System.out.println("Results differ");
      System.out.println();
    }

    return threadRes;
  }

}
