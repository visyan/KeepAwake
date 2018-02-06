import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Robot;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class KeepAwake {
  public static void main(String[] args) throws Exception {
    installSystemTrayMenu();

    Robot robot = new Robot();
    while (true) {
      Point pObj = MouseInfo.getPointerInfo().getLocation();
      System.out.println(pObj.toString() + "x>>" + pObj.x + "  y>>" + pObj.y);
      robot.mouseMove(pObj.x + 5, pObj.y + 5);  
      robot.delay(100);
      robot.mouseMove(pObj.x - 5, pObj.y - 5);
      robot.delay(100);
      robot.mouseMove(pObj.x, pObj.y);
      pObj = MouseInfo.getPointerInfo().getLocation();
      System.out.println(pObj.toString() + "x>>" + pObj.x + "  y>>" + pObj.y);
      
      robot.delay(60000);
    }
  }

  protected static void installSystemTrayMenu() {
    if (!SystemTray.isSupported()) {
      System.out.println("SystemTray is not supported");
      return;
    }
    PopupMenu popupMenu = new PopupMenu();
    final TrayIcon trayIcon = new TrayIcon(createImage("images/keepawake.png", "tray icon"));
    final SystemTray systemTray = SystemTray.getSystemTray();

    MenuItem aboutMenuItem = new MenuItem("About");
    MenuItem exitMenuItem = new MenuItem("Exit");

    popupMenu.add(aboutMenuItem);
    popupMenu.addSeparator();
    popupMenu.add(exitMenuItem);

    trayIcon.setPopupMenu(popupMenu);
    try {
      systemTray.add(trayIcon);
    } catch (AWTException localAWTException) {
      System.out.println("TrayIcon could not be added.");
      return;
    }
    trayIcon.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        showAbout();
      }
    });
    aboutMenuItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        showAbout();
      }
    });
    exitMenuItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        systemTray.remove(trayIcon);
        System.exit(0);
      }
    });
  }

  protected static void showAbout() {
    JOptionPane.showMessageDialog(null, "KeepAwake avoids your pc from locking by moving the mouse every minute.", "KeepAwake", 1);
  }

  protected static Image createImage(String path, String description) {
    URL localURL = KeepAwake.class.getResource(path);
    if (localURL == null) {
      System.err.println("Resource not found: " + path);
      return null;
    }
    return new ImageIcon(localURL, description).getImage();
  }
}
