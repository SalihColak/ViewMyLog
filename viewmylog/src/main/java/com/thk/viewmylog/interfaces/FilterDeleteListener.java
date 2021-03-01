package com.thk.viewmylog.interfaces;

import java.util.List;

/**
 * Interface um die Entfernung eines Elements aus der LogFilterPreference zu benachrichtigen.
 */
public interface FilterDeleteListener {

    /**
     * Diese Methode soll beim Löschen eines Elements aus der LogFilterPreference aufgerufen werden und Komponenten benachrichtigen.
     * @param tagList Neue TagList
     */
    void onFilterDeleted(List<String> tagList);
}
