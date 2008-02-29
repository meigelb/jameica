/**********************************************************************
 * $Source: /cvsroot/jameica/jameica/src/de/willuhn/jameica/backup/Attic/BackupItem.java,v $
 * $Revision: 1.1 $
 * $Date: 2008/02/29 19:02:31 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by willuhn software & services
 * All rights reserved
 *
 **********************************************************************/

package de.willuhn.jameica.backup;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import de.willuhn.datasource.BeanUtil;
import de.willuhn.datasource.GenericObject;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Meta-Container fuer ein einzelnes Backup.
 */
public class BackupItem implements GenericObject
{
  private File file        = null;
  private Properties props = null;

  /**
   * ct
   * @param file die eigentliche ZIP-Datei.
   * @throws ApplicationException
   */
  public BackupItem(File file) throws ApplicationException
  {
    if (file == null)
      throw new ApplicationException(Application.getI18n().tr("Keine Backup-Datei angegeben"));
    
    this.file  = file;
    load();
  }
  
  /**
   * Liefert die eigentliche Backup-Datei.
   * @return die Backup-Datei.
   */
  public File getFile()
  {
    return this.file;
  }
  
  /**
   * Liefert eine Properties-Datei mit den Meta-Daten des Plugins.
   * Die Schluessel und Werte entsprechen der Datei
   * "cfg/de.willuhn.jameica.plugin.PluginLoader.properties"
   * @return Properties des Backups.
   */
  public Properties getProperties()
  {
    return this.props;
  }
  
  /**
   * Laedt die Meta-Daten des Backups.
   * @throws ApplicationException
   */
  private void load() throws ApplicationException
  {
    if (this.props != null)
      return;
    // Wir checken, ob in dem Backup eine Datei
    // cfg/de.willuhn.jameica.plugin.PluginLoader.properties
    // enthalten ist. In der stehen die Versionsnummern der Plugins.
    ZipFile zip    = null;
    InputStream is = null;
    try
    {
      zip = new ZipFile(this.file);
      ZipEntry entry = zip.getEntry("cfg/de.willuhn.jameica.plugin.PluginLoader.properties");
      if (entry == null)
        throw new ApplicationException(Application.getI18n().tr("Ungültiges Backup-Format"));
      
      is = zip.getInputStream(entry);
      if (is == null)
        throw new ApplicationException(Application.getI18n().tr("Ungültiges Backup-Format"));

      this.props = new Properties();
      this.props.load(is);
    }
    catch (ApplicationException ae)
    {
      throw ae;
    }
    catch (Exception e)
    {
      Logger.error("unable to read backup " + this.file.getAbsolutePath(),e);
      throw new ApplicationException(Application.getI18n().tr("Backup nicht lesbar"));
    }
    finally
    {
      if (is != null) {
        try {
          is.close();
        } catch (IOException e) {
          Logger.error("unable to close backup " + this.file.getAbsolutePath(),e);
          throw new ApplicationException(Application.getI18n().tr("Backup nicht lesbar"));
        }
      }

      if (zip != null) {
        try {
          zip.close();
        } catch (IOException e) {
          Logger.error("unable to close backup " + this.file.getAbsolutePath(),e);
          throw new ApplicationException(Application.getI18n().tr("Backup nicht lesbar"));
        }
      }
    }
  }

  /**
   * @see de.willuhn.datasource.GenericObject#equals(de.willuhn.datasource.GenericObject)
   */
  public boolean equals(GenericObject other) throws RemoteException
  {
    if (other == null || !(other instanceof BackupItem))
    return false;
    return this.file.equals(((BackupItem)other).file);
  }

  /**
   * @see de.willuhn.datasource.GenericObject#getAttribute(java.lang.String)
   */
  public Object getAttribute(String name) throws RemoteException
  {
    if ("created".equals(name))
      return new Date(this.file.lastModified());
    if ("size".equals(name))
      return new Long(this.file.length());
    return BeanUtil.get(this.file,name);
  }

  /**
   * @see de.willuhn.datasource.GenericObject#getAttributeNames()
   */
  public String[] getAttributeNames() throws RemoteException
  {
    return new String[]{"name","created"};
  }

  /**
   * @see de.willuhn.datasource.GenericObject#getID()
   */
  public String getID() throws RemoteException
  {
    return this.file.getAbsolutePath();
  }

  /**
   * @see de.willuhn.datasource.GenericObject#getPrimaryAttribute()
   */
  public String getPrimaryAttribute() throws RemoteException
  {
    return "name";
  }
}


/*********************************************************************
 * $Log: BackupItem.java,v $
 * Revision 1.1  2008/02/29 19:02:31  willuhn
 * @N Weiterer Code fuer Backup-System
 *
 **********************************************************************/