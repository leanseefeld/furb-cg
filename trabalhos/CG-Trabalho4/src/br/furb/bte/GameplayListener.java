package br.furb.bte;

/**
 * Listener de eventos de gameplay, disparados pela {@link Tela}.
 */
public interface GameplayListener {

    void afterInit();

    void onReset();

    void onPause();

    void onResume();

}
