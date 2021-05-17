
public class WordThread implements Runnable{

	private WordRecord word;
	private WordControl wControl;

	/**
	WordThread constructor
	@param word WordRecord
	@param wControl WordControl
	*/
	public WordThread(WordRecord word, WordControl wControl){
		super();
		this.word = word;
		this.wControl = wControl;
	}
	/**
	Method resets word
	*/
	public synchronized void reset(){
		word.resetWord();
	}
	/**
	Method run starts thread when called and manages functionality of game
	*/
	@Override 
	public void run(){
		while(!wControl.ended()){//when game is still being played
			if (word.dropped()){
				wControl.missedWord();
				word.resetWord();
				wControl.setChanged();
			}
			else if(wControl.isPaused()){
				continue;
			}

			else{
				word.drop(1);
				wControl.updateLabels();
			}

			try{
				Thread.sleep(word.getSpeed()/15);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}


}