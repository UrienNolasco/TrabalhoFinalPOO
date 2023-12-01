import java.awt.*;

public class Npc2 implements ElementoMapa{
    private Color cor;
    private Character simbolo;

    public Npc2(Character simbolo, Color cor) {
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
        return "SOOOOCCOORRRROOOOOOO" +
                "\nOLHA LÁ, SÃO COBRAAASSSSSSS" +
                "\nMate elas e eu vou lhe entregar o controle do portal" +
                "\nPara matar as cobras utilize a ação secundaria (J)";
    }
}
