import java.awt.*;

public class Agua implements ElementoMapa{
    private Character simbolo;
    private Color cor;

    public Agua(Character simbolo, Color cor) {
        this.simbolo = simbolo;
        this.cor = cor;
    }
    @Override
    public Character getSimbolo() {
        return simbolo;
    }

    @Override
    public Color getCor() {
        return cor;
    }

    @Override
    public boolean podeSerAtravessado() {
        return false;
    }

    @Override
    public boolean podeInteragir() {
        return true;
    }

    @Override
    public String interage() {
        return "Você é burro demais para saber nadar, utilize o portal.";
    }
}
