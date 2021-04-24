package kernel;

public abstract class Engine implements Runnable {

    public final Thread game;
    public final Window window;
    public final Timer timer;
    public final Input input;
    public final boolean allowTimer, allowInput;

    public Engine(int width, int height, String title, boolean allowTimer, boolean allowInput){
        this.game = new Thread(this, title);
        this.window = new Window(width, height, title);
        this.timer = new Timer();
        this.input = new Input();
        this.allowTimer = allowTimer;
        this.allowInput = allowInput;
        game.start();
    }

    public abstract void init();

    public abstract void input();

    public abstract void update();

    public abstract void render();

    public abstract void close();

    @Override
    public void run() {
        window.init();
        if(allowTimer) timer.init();
        if(allowInput) input.init(window.getHandle());
        init();
        while (window.isRunning()) {
            render();
            update();
            input();
            window.update();
            if(allowTimer) timer.update();
        }

        close();
        if(allowInput) input.close();
        window.close();

    }

}
