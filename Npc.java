import java.awt.*;

public class Npc implements ElementoMapa{
    private Color cor;
    private Character simbolo;

    public Npc(Character simbolo, Color cor) {
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
        return "Parabéns, você conseguiu sair de casa!!" +
                "\nLembre-se, você é um fazendeiro, seu dever é colher tudo que esteja plantado na fazenda" +
                "\nSiga o caminho da floresta e encontrará o seu destino";
    }
}
