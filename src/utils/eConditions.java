package utils;

public class eConditions {
    String Light;
    int Blur;
    boolean Blind;
    public eConditions(String light, int blur, boolean blind){
        this.Light=light;
        this.Blur=blur;
        this.Blind=blind;
    }

    public boolean isBlind() {
        return Blind;
    }

    public int getBlur() {
        return Blur;
    }

    public String getLight() {
        return Light;
    }
}
