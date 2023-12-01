import java.awt.*;

public class Chave implements ElementoMapa {
    private Color cor;
    private Character simbolo;
    private boolean coletada;

    public Chave(Character simbolo, Color cor) {
        this.simbolo = simbolo;
        this.cor = cor;
        this.coletada = false;
    }

    @Override
    public Character getSimbolo() {
        return coletada ? ' ' : simbolo;
    }

    @Override
    public Color getCor() {
        return cor;
    }

    @Override
    public boolean podeSerAtravessado() {
        return !coletada;
    }

    @Override
    public boolean podeInteragir() {
        return !coletada;
    }

    @Override
    public String interage() {
        if (!coletada) {
            coletada = true;
            return "Você coletou a chave";
        } else {
            return "A chave já foi coletada";
        }
    }
}