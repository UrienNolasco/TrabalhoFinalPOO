import java.awt.*;

public class Porta implements ElementoMapa {
    private Color cor;
    private Character simbolo;
    private boolean trancada;

    public Porta(Character simbolo, Color cor) {
        this.simbolo = simbolo;
        this.cor = cor;
        this.trancada = true;
    }

    @Override
    public Character getSimbolo() {
        if(trancada){
            return simbolo;
        }else{
            return ' ';
        }
    }

    @Override
    public Color getCor() {
        return cor;
    }

    @Override
    public boolean podeSerAtravessado() {
        return !trancada;
    }

    @Override
    public boolean podeInteragir() {
        return true;
    }

    @Override
    public String interage() {
        if (trancada) {
            return "A porta está trancada, procure a chave para abrir.";
        } else {
            return "A porta está aberta!";
        }
    }
    public void destrancar() {
        trancada = false;
    }
}