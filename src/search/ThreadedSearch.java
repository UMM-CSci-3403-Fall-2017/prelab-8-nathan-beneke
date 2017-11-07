package search;

import java.util.ArrayList;
import java.util.List;

/*
 * NOTE: Creating multiple instances of this class inside of itself weirded me out so I made a subclass for the threads
 * and did all the work in that. Then there was no need for this class to be Runnable in itself so I took that out.
 */

public class ThreadedSearch<T> {
  public Answer answer;
  public ThreadedSearch(){
    this.answer = new Answer(false);
  }

  public int getCount(){
    return answer.getCounted();
  }
  public void setCount(){
    answer.counted = 0;
  }
  /**
  * Searches `list` in parallel using `numThreads` threads.
  *
  * You can assume that the list size is divisible by `numThreads`
  */
  public boolean parSearch(int numThreads, T target, ArrayList<T> list) throws InterruptedException {
    /*
    * First construct an instance of the `Answer` inner class. This will
    * be how the threads you're about to create will "communicate". They
    * will all have access to this one shared instance of `Answer`, where
    * they can update the `answer` field inside that instance.
    *
    * Then construct numThreads instances of  SearchThread and start them to begin searching for target. Each
    * SearchThread is given a sublishis class as its `Runnable`. Then start each of those
    * threads, wait for them to all terminate, and then return the answer
    * in the shared `Answer` instancest of list of which the union is list and are all pairwise disjoint. Then we wait for
    * all threads to finish with join and return the answer with answer.getAnswer().
    */
    int intervalSize = list.size() / numThreads;
    Thread[] threads = new Thread[numThreads];

    for (int i = 0; i < numThreads; i++) {
      threads[i] = new SearchThread<T>(target, answer, list, i * intervalSize, (i + 1) * intervalSize);
      threads[i].start();
    }

    for(Thread thread: threads){
      thread.join();
    }

    return answer.getAnswer();
  }

  // The thread class
  private class SearchThread<T> extends Thread {
    private T target;
    private Answer answer;
    private ArrayList<T> list;
    private int start;
    private int end;

    public SearchThread(T target, Answer answer, ArrayList<T> list, int start, int end){
      this.target = target;
      this.answer = answer;
      this.list = list;
      this.start = start;
      this.end = end;
    }

    // Very straight forward linear search. If we find the target, set the answer to true and finish.
    public void run(){
      for (int i = start; i < end; i ++){
        answer.counted++;
        if (list.get(i).equals(target)){
          answer.setAnswer(true);
          break;
        } else if (answer.getAnswer()){
          break;
        }
      }
    }
  }

  private class Answer {
    private boolean answer;
    public int counted;

    public Answer(boolean answer){
      this.counted = 0;
      this.answer = answer;
    }

    public int getCounted(){
      return counted;
    }

    public boolean getAnswer() {
      return answer;
    }

    // This has to be synchronized to ensure that no two threads modify
    // this at the same time, possibly causing race conditions.
    // Note: Race conditions irrelevant as any thread should only ever modify answer to true, so no conflicts are
    // possible.
    public synchronized void setAnswer(boolean newAnswer) {
      answer = newAnswer;
    }
  }

}
