
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;

/**
 *
 * @author Henry
 */
public class Contactos  extends MIDlet {

    public void startApp() {
        boolean isAPIAvailable = System.getProperty("microedition.pim.version") == null;
        if (!isAPIAvailable) {
            Display.getDisplay(this).setCurrent(new ListaAlmacenamiento(this));
        } else {
            Display.getDisplay(this).setCurrent(new Soporte(this));
        }
    }

    protected void destroyApp(boolean param) {
    }

    protected void pauseApp() {
    }

    void salir() {
        destroyApp(false);
        notifyDestroyed();
    }

    void reportarError(Exception e, Displayable d) {
        Alert alert = new Alert("Error", e.getMessage(), null, AlertType.ERROR);
        alert.setTimeout(-2);
        Display.getDisplay(this).setCurrent(alert, d);
        e.printStackTrace();
    }
}
