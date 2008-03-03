/**********************************************************************
 * $Source: /cvsroot/jameica/jameica/src/de/willuhn/jameica/gui/internal/controller/BackupControl.java,v $
 * $Revision: 1.3 $
 * $Date: 2008/03/03 09:43:54 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by willuhn software & services
 * All rights reserved
 *
 **********************************************************************/

package de.willuhn.jameica.gui.internal.controller;

import java.rmi.RemoteException;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.willuhn.datasource.pseudo.PseudoIterator;
import de.willuhn.jameica.backup.BackupEngine;
import de.willuhn.jameica.backup.BackupFile;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.dialogs.BackupRestoreDialog;
import de.willuhn.jameica.gui.formatter.DateFormatter;
import de.willuhn.jameica.gui.formatter.Formatter;
import de.willuhn.jameica.gui.input.CheckboxInput;
import de.willuhn.jameica.gui.input.DirectoryInput;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.IntegerInput;
import de.willuhn.jameica.gui.input.LabelInput;
import de.willuhn.jameica.gui.parts.CheckedContextMenuItem;
import de.willuhn.jameica.gui.parts.ContextMenu;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.messaging.StatusBarMessage;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.Config;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;


/**
 * Controller fuer das Backup.
 */
public class BackupControl extends AbstractControl
{
  private CheckboxInput state = null;
  private Input target        = null;
  private Input count         = null;
  private Input current       = null;
  private TablePart backups   = null;
  
  /**
   * @param view
   */
  public BackupControl(AbstractView view)
  {
    super(view);
  }
  
  /**
   * Liefert eine Checkbox zum Aktivieren, deaktivieren des Backups.
   * @return Checkbox.
   */
  public CheckboxInput getState()
  {
    if (this.state != null)
      return this.state;
    
    this.state = new CheckboxInput(Application.getConfig().getUseBackup());
    this.state.addListener(new Listener()
    {
      /**
       * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
       */
      public void handleEvent(Event event)
      {
        boolean b = ((Boolean)state.getValue()).booleanValue();
        getTarget().setEnabled(b);
        getCount().setEnabled(b);
      }
    
    });
    return this.state;
  }
  
  /**
   * Liefert ein Eingabefeld fuer das Zielverzeichnis des Backups.
   * @return Eingabefeld.
   */
  public Input getTarget()
  {
    if (this.target != null)
      return this.target;
    
    this.target = new DirectoryInput(Application.getConfig().getBackupDir());
    this.target.setEnabled(Application.getConfig().getUseBackup());
    this.target.addListener(new Listener()
    {
      /**
       * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
       */
      public void handleEvent(Event event)
      {
        // wir laden den Inhalt der Tabelle mit den Backups basierend
        // auf dem neuen Verzeichnis neu.
        try
        {
          TablePart table = getBackups();
          table.removeAll();
          BackupFile[] items = BackupEngine.getBackups((String)getTarget().getValue());
          for (int i=0;i<items.length;++i)
            table.addItem(items[i]);
        }
        catch (ApplicationException ae)
        {
          Application.getMessagingFactory().sendMessage(new StatusBarMessage(ae.getMessage(), StatusBarMessage.TYPE_ERROR));
        }
        catch (RemoteException re)
        {
          Logger.error("error while reloading backup list",re);
          Application.getMessagingFactory().sendMessage(new StatusBarMessage(Application.getI18n().tr("Fehler beim Neuladen der Backups"), StatusBarMessage.TYPE_ERROR));
        }
      }
    
    });
    return this.target;
  }
  
  /**
   * Liefert ein Eingabefeld fuer die Anzahl der Backups.
   * @return Eingabefeld.
   */
  public Input getCount()
  {
    if (this.count != null)
      return this.count;
    
    this.count = new IntegerInput(Application.getConfig().getBackupCount());
    this.count.setEnabled(Application.getConfig().getUseBackup());
    return this.count;
  }
  
  /**
   * Liefert eine Tabelle mit den bisher erstellten Backups.
   * @return Tabelle mit den Backups.
   * @throws RemoteException
   * @throws ApplicationException
   */
  public TablePart getBackups() throws RemoteException, ApplicationException
  {
    if (this.backups != null)
      return this.backups;
    
    this.backups = new TablePart(PseudoIterator.fromArray(BackupEngine.getBackups((String)getTarget().getValue())),null);
    this.backups.addColumn(Application.getI18n().tr("Dateiname"),"name");
    this.backups.addColumn(Application.getI18n().tr("Erstellt am"),"created", new DateFormatter(null));
    this.backups.addColumn(Application.getI18n().tr("Gr��e"),"size", new Formatter() {
    
      /**
       * @see de.willuhn.jameica.gui.formatter.Formatter#format(java.lang.Object)
       */
      public String format(Object o)
      {
        if (o == null || !(o instanceof Number))
          return "-";
        long size = ((Number) o).longValue();
        return size == 0 ? "-" : ((int)(size / 1024 / 1024)) + " MB";
      }
    
    });
    this.backups.setMulti(false);
    this.backups.setRememberColWidths(false);
    this.backups.setRememberOrder(false);
    this.backups.setSummary(false);
    
    ContextMenu ctx = new ContextMenu();
    ctx.addItem(new CheckedContextMenuItem(Application.getI18n().tr("Backup wiederherstellen..."),new Action() {
      public void handleAction(Object context) throws ApplicationException
      {
        handleRestore();
      }
    }));
    this.backups.setContextMenu(ctx);
    return this.backups;
  }
  
  /**
   * Liefert ein Label, welches anzeigt, ob derzeit ein Backup fuer die Wiederherstellung vorgemerkt ist.
   * @return das Label.
   */
  public Input getCurrent()
  {
    if (this.current != null)
      return this.current;

    String s = null;
    try
    {
      BackupFile bi = BackupEngine.getCurrentBackup();
      if (bi != null)
        s = bi.getFile().getName();
    }
    catch (ApplicationException ae)
    {
      s = ae.getMessage();
    }
    this.current = new LabelInput(s == null ? "-" : s);
    return this.current;
  }
  
  /**
   * Speichert die Einstellungen.
   */
  public void handleStore()
  {
    Config config = Application.getConfig();
    
    try
    {
      Integer i = (Integer) getCount().getValue();
      config.setBackupCount(i == null ? -1 : i.intValue());
      getCount().setValue(new Integer(config.getBackupCount())); // Reset, falls der User Unsinn eingegeben hat

      config.setBackupDir((String)getTarget().getValue());
      getTarget().setValue(config.getBackupDir()); // Reset, falls der User Unsinn eingegeben hat
      
      config.setUseBackup(((Boolean)getState().getValue()).booleanValue());
      Application.getMessagingFactory().sendMessage(new StatusBarMessage(Application.getI18n().tr("Einstellungen gespeichert"), StatusBarMessage.TYPE_SUCCESS));
    }
    catch (ApplicationException ae)
    {
      Application.getMessagingFactory().sendMessage(new StatusBarMessage(ae.getMessage(), StatusBarMessage.TYPE_ERROR));
    }
  }
  
  /**
   * Markiert ein Backup fuer die Wiederherstellung.
   */
  public void handleRestore()
  {
    try
    {
      BackupFile o = (BackupFile) getBackups().getSelection();
      if (o == null)
      {
        Application.getMessagingFactory().sendMessage(new StatusBarMessage(Application.getI18n().tr("Bitte w�hlen Sie das wiederherzustellende Backup aus"), StatusBarMessage.TYPE_ERROR));
        return;
      }

      BackupRestoreDialog d = new BackupRestoreDialog(BackupRestoreDialog.POSITION_CENTER,o);
      Boolean b = (Boolean) d.open();
      
      if (!b.booleanValue())
        return;
      
      BackupEngine.restoreBackup(o);
      o = BackupEngine.getCurrentBackup();
      getCurrent().setValue(o == null ? "-" : o.getFile().getName());
      Application.getMessagingFactory().sendMessage(new StatusBarMessage(Application.getI18n().tr("Backup f�r Wiederherstellung vorgemerkt. Bitte starten Sie nun Jameica neu."), StatusBarMessage.TYPE_SUCCESS));
    }
    catch (ApplicationException ae)
    {
      Application.getMessagingFactory().sendMessage(new StatusBarMessage(ae.getMessage(), StatusBarMessage.TYPE_ERROR));
    }
    catch (Exception e)
    {
      Logger.error("unable to choose backup file",e);
      Application.getMessagingFactory().sendMessage(new StatusBarMessage(Application.getI18n().tr("Fehler beim Aktivieren der Backup-Datei"), StatusBarMessage.TYPE_ERROR));
    }
  }
  
  /**
   * Macht eine ggf. vorgenommene Auswahl des Backups rueckgaengig.
   */
  public void handleUndo()
  {
    BackupEngine.undoRestore();
    getCurrent().setValue("-");
    Application.getMessagingFactory().sendMessage(new StatusBarMessage(Application.getI18n().tr("Auswahl r�ckg�ngig gemacht."), StatusBarMessage.TYPE_SUCCESS));
  }
}


/**********************************************************************
 * $Log: BackupControl.java,v $
 * Revision 1.3  2008/03/03 09:43:54  willuhn
 * @N DateUtil-Patch von Heiner
 * @N Weiterer Code fuer das Backup-System
 *
 * Revision 1.2  2008/02/29 19:02:31  willuhn
 * @N Weiterer Code fuer Backup-System
 *
 * Revision 1.1  2008/02/29 01:12:30  willuhn
 * @N Erster Code fuer neues Backup-System
 * @N DirectoryInput
 * @B Fixes an FileInput, TextInput
 *
 **********************************************************************/