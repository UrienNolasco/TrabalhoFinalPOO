import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mapa {
    private List<String> mapa;
    private Map<Character, ElementoMapa> elementos;
    private int x = 50; // Posição inicial X do personagem
    private int y = 50; // Posição inicial Y do personagem
    private final int TAMANHO_CELULA = 10; // Tamanho de cada célula do mapa
    private boolean[][] areaRevelada; // Rastreia quais partes do mapa foram reveladas
    private final Color brickColor = new Color(153, 76, 0); // Cor marrom para tijolos
    private final Color doorColor = new Color(153, 0, 0); // Cor vermelha para porta
    private final Color vegetationColor = new Color(34, 139, 34); // Cor verde para vegetação
    private final Color keyColor = new Color(255,255,0);//Cor amarela para chave
    private final Color npcColor = new Color(153, 51, 153); //Cor roxa
    private final Color cobraColor = new Color(3, 139, 3); // Cor verde para vegetação
    private final Color aguaColor = new Color(3, 3, 255); // Cor verde para cobra
    private final int RAIO_VISAO = 5; // Raio de visão do personagem
    private boolean temChave = false; //Sem chave iniciado
    private static int cobrasMortas = 0; //contador de cobras mortas

    public Mapa(String arquivoMapa) {
        mapa = new ArrayList<>();
        elementos = new HashMap<>();
        registraElementos();
        carregaMapa(arquivoMapa);
        areaRevelada = new boolean[mapa.size()+1000][mapa.get(0).length()+1000];
        atualizaCelulasReveladas();
        cobrasMortas = 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getTamanhoCelula() {
        return TAMANHO_CELULA;
    }

    public int getNumLinhas() {
        return mapa.size();
    }

    public int getNumColunas() {
        return mapa.get(0).length();
    }

    public ElementoMapa getElemento(int x, int y) {
        Character id = mapa.get(y).charAt(x);
        return elementos.get(id);
    }

    public boolean estaRevelado(int x, int y) {
        return areaRevelada[y][x];
    }

    // Move conforme enum Direcao
    public boolean move(Direcao direcao) {
        int dx = 0, dy = 0;

        switch (direcao) {
            case CIMA:
                dy = -TAMANHO_CELULA;
                break;
            case BAIXO:
                dy = TAMANHO_CELULA;
                break;
            case ESQUERDA:
                dx = -TAMANHO_CELULA;
                break;
            case DIREITA:
                dx = TAMANHO_CELULA;
                break;
            default:
                return false;
        }

        if (!podeMover(x + dx, y + dy)) {
            System.out.println("Não pode mover");
            return false;
        }

        x += dx;
        y += dy;

        // Atualiza as células reveladas
        atualizaCelulasReveladas();
        return true;
    }

    // Verifica se o personagem pode se mover para a próxima posição
    private boolean podeMover(int nextX, int nextY) {
        int mapX = nextX / TAMANHO_CELULA;
        int mapY = nextY / TAMANHO_CELULA - 1;

        if (mapa == null)
            return false;

        if (mapX >= 0 && mapX < mapa.get(0).length() && mapY >= 1 && mapY <= mapa.size()) {
            char id;

            try {
               id = mapa.get(mapY).charAt(mapX);
            } catch (StringIndexOutOfBoundsException e) {
                return false;
            }

            if (id == ' ')
                return true;

            ElementoMapa elemento = elementos.get(id);
            if (elemento != null) {
                //System.out.println("Elemento: " + elemento.getSimbolo() + " " + elemento.getCor());
                return elemento.podeSerAtravessado();
            }
        }

        return false;
    }

    public String interage() {
        int menorDistancia = Integer.MAX_VALUE;
        ElementoMapa elementoMaisProximo = null;

        for (int i = Math.max(0, y / TAMANHO_CELULA - 2); i < Math.min(mapa.size(), y / TAMANHO_CELULA + 3); i++) {
            for (int j = Math.max(0, x / TAMANHO_CELULA - 2); j < Math.min(mapa.get(i).length(), x / TAMANHO_CELULA + 3); j++) {
                char id = mapa.get(i).charAt(j);
                ElementoMapa elemento = elementos.get(id);

                if (elemento != null && elemento.podeInteragir()) {
                    int distancia = Math.abs(x / TAMANHO_CELULA - j) + Math.abs(y / TAMANHO_CELULA - i);

                    if (distancia < menorDistancia) {
                        menorDistancia = distancia;
                        elementoMaisProximo = elemento;
                    }
                }
            }
        }

        if (elementoMaisProximo != null) {
            char idElemento = elementoMaisProximo.getSimbolo();
            System.out.println("Elemento mais próximo: " + idElemento);

            if (idElemento == '⁋' && !temChave) {
                // Coleta a chave se ainda não tiver
                System.out.println("Coletando chave...");
                temChave = true;
                ((Chave) elementoMaisProximo).interage(); // Chama o método interage() da Chave para coleta
                return "Chave coletada!";
            } else if (idElemento == '⩎') {
                if (temChave) {
                    // Abre a porta se tiver a chave
                    System.out.println("Tentando abrir a porta...");
                    Porta porta = (Porta) elementoMaisProximo; // Cast para a classe específica
                    porta.destrancar(); // Destrancar a porta usando a chave
                    return "Porta aberta!";
                } else {
                    System.out.println("Você precisa da chave para abrir a porta.");
                    return "Você precisa da chave para abrir a porta.";
                }
            } else {
                return elementoMaisProximo.interage();
            }
        }

        return "Nenhum elemento interagível próximo.";
    }

    public String ataca() {
        int menorDistancia = Integer.MAX_VALUE;
        ElementoMapa elementoMaisProximo = null;
        int cobraX = -1;
        int cobraY = -1;

        for (int i = Math.max(0, y / TAMANHO_CELULA - 2); i < Math.min(mapa.size(), y / TAMANHO_CELULA + 3); i++) {
            for (int j = Math.max(0, x / TAMANHO_CELULA - 2); j < Math.min(mapa.get(i).length(), x / TAMANHO_CELULA + 3); j++) {
                char id = mapa.get(i).charAt(j);
                ElementoMapa elemento = elementos.get(id);

                if (elemento != null && elemento.podeInteragir()) {
                    int distancia = Math.abs(x / TAMANHO_CELULA - j) + Math.abs(y / TAMANHO_CELULA - i);

                    if (distancia < menorDistancia) {
                        menorDistancia = distancia;
                        elementoMaisProximo = elemento;
                        cobraX = j;
                        cobraY = i;
                    }
                }
            }
        }
        if (elementoMaisProximo instanceof Cobra) {
            cobrasMortas++;
            System.out.println("Cobra morta! Total de cobras mortas: " + cobrasMortas);
            StringBuilder linha = new StringBuilder(mapa.get(cobraY));
            linha.setCharAt(cobraX, ' ');
            mapa.set(cobraY, linha.toString());
            return "Cobra morta! Total de cobras mortas: " + cobrasMortas;
        }
        return "Nenhuma cobra próxima.";
    }




    private void carregaMapa(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                mapa.add(line);
                // Se character 'P' está contido na linha atual, então define a posição inicial do personagem
                if (line.contains("P")) {
                    x = line.indexOf('P') * TAMANHO_CELULA;
                    y = mapa.size() * TAMANHO_CELULA;
                    // Remove o personagem da linha para evitar que seja desenhado
                    mapa.set(mapa.size() - 1, line.replace('P', ' '));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para atualizar as células reveladas
    private void atualizaCelulasReveladas() {
        if (mapa == null)
            return;
        for (int i = Math.max(0, y / TAMANHO_CELULA - RAIO_VISAO); i < Math.min(mapa.size(), y / TAMANHO_CELULA + RAIO_VISAO + 1); i++) {
            for (int j = Math.max(0, x / TAMANHO_CELULA - RAIO_VISAO); j < Math.min(mapa.get(i).length(), x / TAMANHO_CELULA + RAIO_VISAO + 1); j++) {
                areaRevelada[i][j] = true;
            }
        }
    }

    // Registra os elementos do mapa
    private void registraElementos() {
        // Parede
        elementos.put('#', new Parede('▣', brickColor));
        // Vegetação
        elementos.put('V', new Vegetacao('♣', vegetationColor));
        // Porta
        elementos.put('O', new Porta('⩎', doorColor));
        // Chave
        elementos.put('C', new Chave('⁋', keyColor));
        //NPC
        elementos.put('N', new Npc('⛄', npcColor ));
        //Cobra
        elementos.put('S', new Cobra('‱',cobraColor));
        //NPC 2
        elementos.put('L', new Npc2('⛄', npcColor ));
        //Agua
        elementos.put('W', new Agua('♒',aguaColor));
    }
}
