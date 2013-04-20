package com.autoupdater.gui.adapters.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.autoupdater.gui.adapters.Gui2ClientAdapter;

public class CheckUpdateTriggerListener implements MouseListener, ActionListener {
    private final Gui2ClientAdapter adapter;

    public CheckUpdateTriggerListener(Gui2ClientAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        adapter.checkUpdates();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        adapter.checkUpdates();
    }

}
