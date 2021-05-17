public class WordRecord {
    private String text;
    private int x;
    private int y;
    private int maxY;
    private boolean dropped;

    private int fallingSpeed;
    private static int minWait = 200;
    private static int maxWait = 600;

    public static WordDictionary dict;

    /**
    WordRecord constructor
    */
    public WordRecord() {
        text = "";
        x = 0;
        y = 0;
        maxY = 300;
        dropped = false;
        fallingSpeed = (int) (Math.random() * (maxWait - minWait) + minWait);
    }

    public WordRecord(String text) {
        this();
        this.text = text;
    }

    public WordRecord(String text, int x, int maxY) {
        this(text);
        this.x = x;
        this.maxY = maxY;
    }

    // all getters and setters must be synchronized
    public synchronized void setY(int y) {
        if (y > maxY) {
            y = maxY;
            dropped = true;
        }
        this.y = y;
    }

    public synchronized void setX(int x) {
        this.x = x;
    }

    public synchronized void setWord(String text) {
        this.text=text;
    }

    public synchronized String getWord() {
        return text;
    }

    public synchronized int getX() {
        return x;
    }

    public synchronized int getY() {
        return y;
    }

    public int getSpeed() {
        return fallingSpeed;
    }

    public synchronized void setPos(int x, int y) {
        setX(x);
        setY(y);
    }    

    public synchronized void resetPos() {
        setY(0);
    }

    public void resetWord() {
        resetPos();
        text = dict.getNewWord();
        dropped = false;
        fallingSpeed = (int) (Math.random() * (maxWait - minWait) + minWait);
    }

    public synchronized boolean matchWord(String typedText) {
        if (typedText.equals(this.text)) {
            resetWord();
            return true;
        } else {
            return false;
        }
    }

    public void drop(int inc) {
        setY(y + inc);
    }

    public boolean dropped() {
        return dropped;
    }

    @Override
    public boolean equals(Object obj) {
        if (!obj.getClass().equals(this.getClass())) {
            return false;
        }
        WordRecord wrdObj = ((WordRecord) obj);

        return (this.getWord().equals(wrdObj.getWord()) && (this.getY() == wrdObj.getY()));
    }
}
