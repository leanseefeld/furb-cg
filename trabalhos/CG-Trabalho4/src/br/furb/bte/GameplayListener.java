package br.furb.bte;

/**
 * Listener de eventos de gameplay, disparados pela {@link Tela}.
 */
public interface GameplayListener {

    /**
     * Executa depois de cada loop
     * Apenas para testar a IA
     */
    void beforePlay();
    
    void afterInit();

    void onReset();

    void onPause();

    void onResume();
    
    /**
     * Executa no fim da partida. Ou seja, quando algum dos jogadores perdem ou há empate
     */
    void onFinish();
}
