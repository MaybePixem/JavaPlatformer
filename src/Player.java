public class Player {

    private float x, y;
    private float momentum = 0;
    private int size = 30;

    public int getSize() {
        return size;
    }

    public float getMomentum() {
        return momentum;
    }

    public void setMomentum(float momentum) {
        this.momentum = momentum;
    }


    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Player(float x, float y) {
        super();
        this.x = x;
        this.y = y;
    }

}