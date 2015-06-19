package br.furb.bte.comando;

public enum TipoComando {

    DIREITA,
    ESQUERDA,
    COLISAO,
    CONFIG_MOTO,
    READY,
    PAUSE,
    // ===== COMANDOS REMOTOS =====
    HELLO,
    BYE;
    
    public short getCodigo() {
	return (short) ordinal();
    }

}
