package kernel;

public class Timer {

    private int frames;
    public long time;
    private int fps;

    public void init(){
        time = System.currentTimeMillis();
    }

    public void update(){
        frames++;
        if (System.currentTimeMillis() > time + 1000) {
            fps = frames;
            time = System.currentTimeMillis();
            frames = 0;
        }
    }

    public int getFPS(){
        return fps;
    }

}
