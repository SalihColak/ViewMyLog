package com.thk.viewmylog.interfaces;

/**
 * Interface um das Einlesem von Log-Meldungen zu benachrichtigen.
 */
public interface LogListener {
    /**
     * Diese Methode soll beim Lesen einer Log-Meldung aufgerufen werden und andere Systemkomponenten benachrichtigen.
     */
    void onLogRead(String msg);
}
