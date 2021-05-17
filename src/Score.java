import java.util.concurrent.atomic.AtomicInteger; //using atomic integers to avoid synchronization blocking

public class Score {
	private AtomicInteger missedWords;
	private AtomicInteger caughtWords;
	private AtomicInteger incorrectWords;
	private AtomicInteger gameScore;
	
	Score() {
		missedWords=new AtomicInteger(0);
		caughtWords=new AtomicInteger(0);
		incorrectWords=new AtomicInteger(0);
		gameScore=new AtomicInteger(0);
	}
		
	// all getters and setters must be synchronized
	
	public int getMissed() {
		return missedWords.get();
	}

	public int getCaught() {
		return caughtWords.get();
	}

	public int getIncorrect(){
		return incorrectWords.get();
	}
	
	public synchronized int getTotal() {
		return (missedWords.addAndGet(caughtWords.get()));
	}

	public int getScore() {
		return gameScore.get();
	}
	
	public void missedWord() {
		missedWords.incrementAndGet();
	}

	public synchronized void caughtWord(int len) {
		caughtWords.getAndIncrement(); 
		gameScore.addAndGet(len);
	}

	public void incorrectWord() {
		incorrectWords.incrementAndGet();
	}

	public synchronized void resetScore() {
		caughtWords.set(0);
		missedWords.set(0);
		incorrectWords.set(0);
		gameScore.set(0);
	}
}
