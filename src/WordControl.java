import javax.swing.*;
import java.util.*;

public class WordControl{

	private WordThread[] wThreads;

	private volatile boolean ended;
	private volatile boolean paused;
	private volatile boolean changed;
	private volatile boolean running;
	private final Object locked = new Object();

	public WordControl(){
		super();
		this.wThreads = new WordThread[WordApp.words.length];
		ended = true;
		paused = false;
		running = false;
	}
	/**
	Method updateLabels updates the labels on the GUI when it changes
	*/
	public void updateLabels(){
		synchronized (locked) {
			WordApp.labels[0].setText("Caught: " + WordApp.score.getCaught() + "    ");
			WordApp.labels[1].setText("Missed:" + WordApp.score.getMissed()+ "    ");
			WordApp.labels[2].setText("Incorrect:" + WordApp.score.getIncorrect() + "    ");
			WordApp.labels[3].setText("Score:" + WordApp.score.getScore()+ "    ");
			setChanged();
		}
	}
	/**
	Method wordMatch checks to see if the word entered by the user matched the actual word falling
	@param text String
	@return boolean true if matches, false if word does not match
	*/
	public synchronized boolean wordMatch(String text){
		Arrays.sort(WordApp.words, new Comparator<WordRecord>() {
			@Override
			public  int compare(WordRecord w1, WordRecord w2){
				if (w1.equals(w2)){
					return 0;
				}

				if (w1.getY() > w2.getY()){
					return -1;
				}

				return -1;
			}
		});

		for (WordRecord word : WordApp.words){
			if (word.matchWord(text)) {
				WordApp.score.caughtWord(text.length());
				updateLabels();
				if(WordApp.score.getCaught() >= WordApp.totalWords){
					winGame();
				}

				return true;
			}
		}

		return false;
	}
	/**
	Method beginWords creates seperate threads for all the words in WordRecord
	*/
	public void beginWords(){
		ended = false;
		running =true;
		int i = 0;

		for (WordRecord word : WordApp.words){
			wThreads[i] = new WordThread(word, this);
			new Thread(wThreads[i]).start();
			i++;
		}
	}
	/**
	Method missedWord increments the missed word counter
	*/
	public synchronized void missedWord(){
		WordApp.score.missedWord();
		updateLabels();
		if (WordApp.score.getMissed() >= 10){
			stopGame();
			setChanged();
			JOptionPane.showMessageDialog(WordApp.w, "Game Over\n" + 
				"Words Caught:" + WordApp.score.getCaught() +
				"\nScore:" + WordApp.score.getScore());

			resetScore();
			WordApp.w.repaintOnce();
		}
	}
	/**
	Method resetScore resets score objects score
	*/
	public void resetScore(){
		WordApp.score.resetScore();
		updateLabels();
	}
	/**
	Method stopGame resets the game
	*/
	public void stopGame(){
		for (WordThread wt: wThreads){
			wt.reset();
		}

		ended = true;
		running = false;
	}
	/**
	Method endGame ends current game
	*/
	public void endGame(){
		stopGame();
		resetScore();
		WordApp.w.repaintOnce();
	}
	/**
	Method ended checks whether game has ended
	@return boolean true if ended, false if ongoing
	*/
	public boolean ended(){
		return ended;
	}
	/**
	Method winGame shows pop up message if user wins game
	*/
	public void winGame(){
		stopGame();
		setChanged();
		JOptionPane.showMessageDialog(WordApp.w, "Winner!\n" + 
				"Words Caught:" + WordApp.score.getCaught() +
				"\nScore:" + WordApp.score.getScore());
		resetScore();
		WordApp.w.repaintOnce();

	}
	/**
	Method setPaused sets paused variable to opposite state
	*/
	public void setPaused(){
		paused  =! paused;
	}
	/**
	MethodisPaused determines if game is paused or not
	@return boolean true if paused, false if not paused
	*/
	public boolean isPaused(){
		return paused;
	}
	/**
	Method isChanged checks if model is changed
	@return boolean true if model is changed
	*/
	public boolean isChanged(){
		return changed;

	}
	/**
	Method setUnchanged sets changed variable to unchanged
	*/
	public synchronized void setUnchanged(){
		changed = false;
	}
	/**
	Method setChanged sets changed variable to true
	*/
	public synchronized void setChanged(){
		changed = true;
	}
	/**
	Method isRunning returns boolean that det is game is running or not
	@return boolean true if game is running
	*/
	public boolean isRunning(){
		return running;
	}


}
