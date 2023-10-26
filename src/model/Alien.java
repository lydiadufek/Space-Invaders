package model;

public class Alien extends Sprite {
    private int width;
    private int height;
    private int scoreAmount;

    @Override
    public void collider() {
        super.collider();
    }

    public int updateScore(int score) {
        return score += scoreAmount;
    }

}
