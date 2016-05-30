import java.util.Enumeration;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.pim.Contact;
import javax.microedition.pim.PIMException;
import javax.microedition.pim.PIMItem;
import javax.microedition.pim.PIMList;
/**
 *
 * @author Henry Salinas
 */
public class ListaContactos extends List implements CommandListener
{
  private final Command cmdAtras = new Command("Salir", 7, 1);
  private final Command cmdAcerca = new Command("Acerca", 1, 2);
  private final Contactos midlet;
  PIMList lista;

  public void commandAction(Command c, Displayable d)
  {
    if (c == this.cmdAtras) {
      this.midlet.salir();
    }
    if (c == this.cmdAcerca) {
      QuienEs();
    }
  }

  public ListaContactos(Contactos midlet, PIMList lista)
    throws PIMException
  {
    super("Contactos Actualizados ", 3);
    this.midlet = midlet;
    this.lista = lista;
    EnlistarContactos();
    addCommand(this.cmdAtras);
    addCommand(this.cmdAcerca);
    setCommandListener(this);
  }

  public void EnlistarContactos()
  {
    new Thread(new Runnable()
    {
      public void run()
      {
        synchronized (ListaContactos.this)
        {
          try
          {
            ListaContactos.this.deleteAll();
            for (Enumeration items = ListaContactos.this.lista.items(); items.hasMoreElements();)
            {
              PIMItem item = (PIMItem)items.nextElement();
              ListaContactos.this.ActualizarAlt(item);
            }
            if (ListaContactos.this.size() <= 0) {
              ListaContactos.this.append("Contactos ya Actualizados ", null);
            }
          }
          catch (PIMException e)
          {
            ListaContactos.this.midlet.reportarError(e, ListaContactos.this);
          }
        }
      }
    }).start();
  }

  public void ActualizarAlt(PIMItem item)
  {
    boolean salir = false;
    int[] fields = item.getPIMList().getSupportedFields();
    for (int i = 0; i < fields.length; i++)
    {
      int field = fields[i];
      int dataType = item.getPIMList().getFieldDataType(field);
      switch (dataType)
      {
      case 4:
        if ((item instanceof Contact))
        {
          String[] nameValues = item.getStringArray(106, 0);
          String Datos = "";
          String firstName = nameValues[1];
          String lastName = nameValues[0];
          Datos = lastName + " , " + firstName;
          int n = item.countValues(115);
          for (int i2 = 0; i2 < n; i2++)
          {
            String NumeroCambiado = null;
            NumeroCambiado = Numero(item.getString(115, i2));
            Datos = Datos + " ,Telf Anterior " + (i2 + 1) + ":" + NumeroCambiado;
            if ((NumeroCambiado != null) &&
              (!NumeroCambiado.equals("null")))
            {
              item.setString(115, i2, 8, NumeroCambiado);
              try
              {
                item.commit();
                Datos = Datos + " ,Telf Actual " + (i2 + 1) + ":" + NumeroCambiado;
              }
              catch (PIMException e)
              {
                this.midlet.reportarError(e, this);
              }
              append(Datos, null);
            }
          }
        }
        salir = true;
      }
      if (salir) {
        break;
      }
    }
  }

  void QuienEs()
  {
    Alert alert = new Alert("Acerca:", "Desarrollado por Salinas Flores Henry ITSCO .: salinashf@gmail.com :.", null, AlertType.INFO);
    alert.setTimeout(-2);
    Display.getDisplay(this.midlet).setCurrent(alert);
  }

  public static String Numero(String paramString)
  {
    String str2 = null;
    String str1 = "";
    for (int i = 0;; i++)
    {
      if (i >= paramString.length())
      {
        if (str1.length() != 9)
        {
          str2 = NumerosNacionales(str1);
          break;
        }
        if (!str1.startsWith("09"))
        {
          str2 = NumeroInternacionales(str1);
          break;
        }
        str2 = "099" + str1.substring(2);
        break;
      }
      char c1 = paramString.charAt(i);
      if ((c1 != '-') && (c1 != ' ') && (c1 != '(') && (c1 != ')')) {
        str1 = str1 + c1;
      }
    }
    return str2;
  }

  public static String NumerosNacionales(String str1)
  {
    if (str1.startsWith("08"))
    {
      String str2 = "098" + str1.substring(2);
      return str2;
    }
    if (str1.startsWith("069"))
    {
      String str2 = "0969" + str1.substring(3);
      return str2;
    }
    if (str1.startsWith("059"))
    {
      String str2 = "0959" + str1.substring(3);
      return str2;
    }
    if (str1.startsWith("039"))
    {
      String str2 = "0939" + str1.substring(3);
      return str2;
    }
    return NumeroInternacionales(str1);
  }

  public static String NumeroInternacionales(String str1)
  {
    String str2 = null;
    if (str1.length() == 12)
    {
      if (str1.startsWith("+5939")) {
        str2 = "+59399" + str1.substring(5);
      } else if (str1.startsWith("+5938")) {
        str2 = "+59398" + str1.substring(5);
      } else if (str1.startsWith("+59369")) {
        str2 = "+593969" + str1.substring(6);
      } else if (str1.startsWith("+59359")) {
        str2 = "+593959" + str1.substring(6);
      } else if (str1.startsWith("+59339")) {
        str2 = "+593939" + str1.substring(6);
      }
    }
    else {
      str2 = null;
    }
    return str2;
  }
}
