package search;

import java.util.ArrayList;
import java.util.List;

/*
 * NOTE: Creating multiple instances of this class inside of itself weirded me out so I made a subclass for the threads
 * and did all the work in that. Then there was no need for this class to be Runnable in itself so I took that out.
 */

public class ThreadedSearch<T> {
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
    Answer answer = new Answer();
    int intervalSize = list.size() / numThreads;
    Thread[] threads = new Thread[numThreads];

    for (int i = 0; i < numThreads; i++) {
      // Giving each thread its one list is easier than messing with start/end indices, but is equivalent.
      List subList = list.subList(i * intervalSize, (i + 1) * intervalSize);
      threads[i] = new SearchThread<T>(target, answer, subList);
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
    private List<T> list;

    public SearchThread(T target, Answer answer, List<T> list){
      this.target = target;
      this.answer = answer;
      this.list = list;
    }

    // Very straight forward linear search. If we find the target, set the answer to true and finish.
    public void run(){
      for (T element: list){
        if (element.equals(target)){
          answer.setAnswer(true);
          break;
        }
      }
    }
  }

  private class Answer {
    private boolean answer = false;

    public boolean getAnswer() {
      return answer;
    }

    // This has to be synchronized to ensure that no two threads modify
    // this at the same time, possibly causing race conditions.
    public synchronized void setAnswer(boolean newAnswer) {
      answer = newAnswer;
    }
  }

}
